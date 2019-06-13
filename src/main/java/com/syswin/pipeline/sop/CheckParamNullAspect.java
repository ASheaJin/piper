package com.syswin.pipeline.sop;

import com.syswin.pipeline.psservice.SendMessegeService;
import com.syswin.pipeline.service.exception.BusinessException;
import com.syswin.pipeline.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author:lhz
 * @date:2019/4/5 15:47
 */

@Aspect
@Component
@Slf4j
@SuppressWarnings("all")
public class CheckParamNullAspect {

	@Autowired
	private SendMessegeService sendMessegeService;

	@Pointcut("@annotation(com.syswin.pipeline.sop.CheckParamNull)")
	public void dealCacheServiceCut() {
	}


	@Before(value = "dealCacheServiceCut()")
	@SuppressWarnings("all")
	public void dealCacheService(JoinPoint point) throws Throwable {
		Method method = getMethod(point);
		// 获取注解对象
		CheckParamNull checkParamAnnotation = method.getAnnotation(CheckParamNull.class);
		//所有参数
		Object[] args = point.getArgs();

		parseCheckKey(checkParamAnnotation.params(), method, args);

		//以，分割参数
//		return point.proceed();
	}

	/**
	 * 解析标签的方法
	 *
	 * @param joinPoint
	 * @return
	 * @throws Exception
	 */
	private Method getMethod(JoinPoint joinPoint) throws Exception {

		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		Method method = methodSignature.getMethod();

		return method;
	}

	/**
	 * 解析key的值
	 * 调用Spring的方法
	 *
	 * @param fieldKey
	 * @param method
	 * @param args
	 * @return
	 */
	private void parseCheckKey(String fieldKey, Method method, Object[] args) {
		//获取被拦截方法参数名列表(使用Spring支持类库)
		LocalVariableTableParameterNameDiscoverer u =
						new LocalVariableTableParameterNameDiscoverer();
		String[] paraNameArr = u.getParameterNames(method);
		Map<String, Object> paramMap = new HashMap<>();
		//将参数名和参数绑定
		for (int i = 0; i < paraNameArr.length; i++) {
			paramMap.put(paraNameArr[i], args[i]);
		}
		if (StringUtil.isEmpty(fieldKey)) {
			return;
		}
		if ("all".equals(fieldKey)) {
			for (int i = 0; i < paraNameArr.length; i++) {
				if (StringUtil.isEmpty(args[i])) {
					log.error("方法："+method.getName()+"--"+paraNameArr[i] + "不能为null");
					throw new BusinessException("方法："+method.getName()+"--"+paraNameArr[i] + "不能为null");
				}
			}
		}
		//对注入的参数判断做解析出来
		String[] ks = fieldKey.split(",");
		for (String k : ks) {
			Object v = "1";
			try {
				v = paramMap.get(k);
			} catch (Exception e) {
				log.error("方法："+method.getName()+"--"+k + " check  error ");
				e.printStackTrace();
			}
			if (StringUtil.isEmpty(v)) {
				log.error("方法："+method.getName()+"--"+k + "不能为null");
				throw new BusinessException("方法："+method.getName()+"--"+k + "不能为null");
			}
			if ((v instanceof Integer)) {
				if ((Integer) v == 0) {
					log.error("方法："+method.getName()+"--"+k + "不能为 0 ");
					throw new BusinessException("方法："+method.getName()+"--"+k + "不能为 0 ");
				}
			}
		}


	}
}

