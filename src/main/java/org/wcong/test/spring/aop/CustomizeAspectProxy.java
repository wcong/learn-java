package org.wcong.test.spring.aop;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.core.SpringNamingPolicy;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 16/4/18
 */
public class CustomizeAspectProxy implements BeanPostProcessor, ApplicationContextAware {

	private ApplicationContext applicationContext;

	private List<AbstractAdvisor> advisorList;

	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		buildAdvisor();
		Map<Method, List<AbstractAdvisor>> matchAdvisorMap = matchAdvisor(bean);
		if (matchAdvisorMap.isEmpty()) {
			return bean;
		} else {
			Enhancer enhancer = new Enhancer();
			enhancer.setSuperclass(bean.getClass());
			enhancer.setNamingPolicy(SpringNamingPolicy.INSTANCE);
			enhancer.setCallback(new MethodInterceptorImpl(matchAdvisorMap));
			return enhancer.create();
		}
	}

	private Map<Method, List<AbstractAdvisor>> matchAdvisor(Object bean) {
		Class<?> beanClass = bean.getClass();
		Method[] methods = beanClass.getMethods();
		if (methods == null) {
			return Collections.emptyMap();
		}
		Map<Method, List<AbstractAdvisor>> methodListMap = new HashMap<Method, List<AbstractAdvisor>>();
		for (Method method : methods) {
			for (AbstractAdvisor abstractAdvisor : advisorList) {
				if (!abstractAdvisor.isMatch(bean.getClass(), method)) {
					continue;
				}
				List<AbstractAdvisor> advisorList = methodListMap.get(method);
				if (advisorList == null) {
					advisorList = new LinkedList<AbstractAdvisor>();
					methodListMap.put(method, advisorList);
				}
				advisorList.add(abstractAdvisor);
			}
		}
		return methodListMap;
	}

	private void buildAdvisor() {
		if (advisorList != null) {
			return;
		}
		synchronized (this) {
			if (advisorList != null) {
				return;
			}
			String[] beanNames = applicationContext.getBeanDefinitionNames();
			advisorList = new ArrayList<AbstractAdvisor>();
			for (String beanName : beanNames) {
				Class<?> beanClass = applicationContext.getType(beanName);
				MyAspect myAspect = beanClass.getAnnotation(MyAspect.class);
				if (myAspect == null) {
					continue;
				}
				Method[] methods = beanClass.getDeclaredMethods();
				if (methods == null) {
					continue;
				}
				Object bean = applicationContext.getBean(beanName);
				List<AbstractAdvisor> beanAdvisorList = new ArrayList<AbstractAdvisor>(methods.length);
				for (Method method : methods) {
					if (method.getName().equals("before")) {
						beanAdvisorList.add(new MethodInvocation.BeforeAdvisor(bean, method));
					} else if (method.getName().equals("around")) {
						beanAdvisorList.add(new MethodInvocation.AroundAdvisor(bean, method));
					} else if (method.getName().equals("after")) {
						beanAdvisorList.add(new MethodInvocation.AfterAdvisor(bean, method));
					}
				}
				advisorList.addAll(beanAdvisorList);
			}
			Collections.sort(advisorList, new Comparator<AbstractAdvisor>() {
				public int compare(AbstractAdvisor o1, AbstractAdvisor o2) {
					return o1.getOrder() - o2.getOrder();
				}
			});
		}
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
