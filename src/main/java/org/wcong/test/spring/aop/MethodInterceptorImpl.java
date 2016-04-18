package org.wcong.test.spring.aop;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 16/4/18
 */
public class MethodInterceptorImpl implements MethodInterceptor {
	private Map<Method, List<AbstractAdvisor>> advisorMap;

	public MethodInterceptorImpl(Map<Method, List<AbstractAdvisor>> advisorMap) {
		this.advisorMap = advisorMap;
	}

	public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
		List<AbstractAdvisor> advisorList = advisorMap.get(method);
		if (advisorList == null) {
			return methodProxy.invokeSuper(o, objects);
		} else {
			MethodInvocation methodInvocation = new MethodInvocation(o, method, objects, methodProxy, advisorList);
			return methodInvocation.proceed(methodInvocation);
		}
	}
}
