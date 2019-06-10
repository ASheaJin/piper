package com.syswin.pipeline.sop;

import com.syswin.pipeline.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
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
import java.util.Arrays;

/**
 * @author:lhz
 * @date:2019/4/5 15:47
 */

@Aspect
@Component
@Slf4j
@SuppressWarnings("all")
public class CheckParamNullAspect {

	@Pointcut("@annotation(com.syswin.pipeline.sop.CheckParamNull)")
	public void dealCacheServiceCut() {
	}


	@Around(value = "dealCacheServiceCut()")
	@SuppressWarnings("all")
	public Object dealCacheService(ProceedingJoinPoint point) throws Throwable {
			Method method = getMethod(point);
			// 获取注解对象
			CheckParamNull checkParamAnnotation = method.getAnnotation(CheckParamNull.class);
			//所有参数
			Object[] args = point.getArgs();
		System.out.println("args" + args);
			String fieldKey = parseKey(checkParamAnnotation.params(), method, args);
		System.out.println("fieldKey" + fieldKey);
			if (StringUtil.isEmpty(fieldKey)) {
				return point.proceed();
			}
			//以，分割参数
			String[] params = fieldKey.split(",");


		Signature signature = point.getSignature();
		MethodSignature methodSignature = (MethodSignature) signature;
		String[] strings = methodSignature.getParameterNames();
		System.out.println(Arrays.toString(strings));


		return point.proceed();
	}

	/**
	 * 解析标签的方法
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
	 * @param fieldKey
	 * @param method
	 * @param args
	 * @return
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

