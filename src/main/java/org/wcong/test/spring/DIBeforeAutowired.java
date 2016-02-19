package org.wcong.test.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 16/2/19
 */
@Configuration
public class DIBeforeAutowired {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext();
		annotationConfigApplicationContext.register(DIBeforeAutowired.class);
		annotationConfigApplicationContext.refresh();
		InjectClass injectClass = annotationConfigApplicationContext.getBean(InjectClass.class);
		injectClass.print();
	}

	@Component
	public static class InstantiationAwareBeanPostProcessorTest extends AutowiredAnnotationBeanPostProcessor {

		public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
			if (bean instanceof InjectClass) {
				System.out.println(((InjectClass) bean).getInjectField());
			}
			return true;
		}

		public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
			if (bean instanceof InjectClass) {
				System.out.println(((InjectClass) bean).getInjectField());
			}
			return bean;
		}

		public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
			if (bean instanceof InjectClass) {
				System.out.println(((InjectClass) bean).getInjectField());
			}
			return bean;
		}
	}

	@Component
	public static class InjectField {
		public String getHelloWorld() {
			return "hello world";
		}
	}

	@Component
	public static class InjectClass {

		@Autowired
		InjectField injectField;

		public void print() {
			System.out.println(injectField.getHelloWorld());
		}

		public InjectField getInjectField() {
			return injectField;
		}

	}

}
