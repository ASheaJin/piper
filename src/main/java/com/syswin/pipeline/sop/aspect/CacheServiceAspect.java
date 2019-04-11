package com.syswin.pipeline.sop.aspect;

import com.alibaba.druid.util.StringUtils;
import com.syswin.pipeline.sop.EnableCacheService;
import com.syswin.pipeline.utils.LanguageCacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author:lhz
 * @date:2019/4/5 15:47
 */

@Aspect
@Component
@Slf4j
@SuppressWarnings("all")
public class CacheServiceAspect {

	@Pointcut("@annotation(com.syswin.pipeline.sop.EnableCacheService)")
	public void dealCacheServiceCut() {
	}


	@Around(value = "dealCacheServiceCut()")
	@SuppressWarnings("all")
	public Object dealCacheService(ProceedingJoinPoint point) throws Throwable {
		try {
			Method method = getMethod(point);
			// 获取注解对象
			EnableCacheService cacheServiceAnnotation = method.getAnnotation(EnableCacheService.class);
			//所有参数
			Object[] args = point.getArgs();
			String fieldKey = parseKey(cacheServiceAnnotation.fieldKey(), method, args);
			if (StringUtils.isEmpty(fieldKey)) {
				return point.proceed();
			}
			String cacheKey = cacheServiceAnnotation.keyPrefix() + fieldKey;
			EnableCacheService.CacheOperation cacheOperation = cacheServiceAnnotation.cacheOperation();
			if (cacheOperation == EnableCacheService.CacheOperation.QUERY) {
				return processQuery(point, cacheServiceAnnotation, cacheKey);
			}
			if (cacheOperation == EnableCacheService.CacheOperation.UPDATE) {
				return processUpdate(point, cacheKey);
			}
		} catch (Exception e) {
			log.error("dealCacheService error,JoinPoint:{}", point.getSignature(), e);
		}
		return point.proceed();
	}

	/**
	 * 查询处理
	 */
	private Object processQuery(ProceedingJoinPoint point, EnableCacheService cacheServiceAnnotation, String cacheKey)
					throws Throwable {
		String languageValue = LanguageCacheUtil.get(cacheKey);
		if (LanguageCacheUtil.get(cacheKey) != null) {
			log.info("{} enable cache service,has cacheKey:{} , return", point.getSignature(), cacheKey);
			return languageValue;
		} else {
			Object result = null;
			try {
				return result = point.proceed();
			} finally {
				LanguageCacheUtil.put(cacheKey, String.valueOf(result));
				log.info("after {} proceed,save result to cache,:{},save content:{}", point.getSignature(), cacheKey, result);
			}
		}
	}

	/**
	 * 删除和修改处理
	 */
	private Object processUpdate(ProceedingJoinPoint point, String cacheKey)
					throws Throwable {
		//通常来讲,数据库update操作后,只需删除掉原来在缓存中的数据,下次查询时就会刷新
		Object result = null;
		try {
			return result = point.proceed();
		} finally {
			String languageValue = LanguageCacheUtil.get(cacheKey);
			if(result ==null || !result.equals(languageValue)) {
				LanguageCacheUtil.put(cacheKey, String.valueOf(result));
			}
		}
	}


	private Method getMethod(JoinPoint joinPoint) throws Exception {

		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		Method method = methodSignature.getMethod();

		return method;
	}

	/**
	 * 获取redis的key
	 */
	private String parseKey(String fieldKey, Method method, Object[] args) {
		//获取被拦截方法参数名列表(使用Spring支持类库)
		LocalVariableTableParameterNameDiscoverer u =
						new LocalVariableTableParameterNameDiscoverer();
		String[] paraNameArr = u.getParameterNames(method);

		//使用SPEL进行key的解析
		ExpressionParser parser = new SpelExpressionParser();
		//SPEL上下文
		StandardEvaluationContext context = new StandardEvaluationContext();
		//把方法参数放入SPEL上下文中
		for (int i = 0; i < paraNameArr.length; i++) {
			context.setVariable(paraNameArr[i], args[i]);
		}
		return parser.parseExpression(fieldKey).getValue(context, String.class);
	}
}

