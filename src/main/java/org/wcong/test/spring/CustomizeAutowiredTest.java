package org.wcong.test.spring;

import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class CustomizeAutowiredTest {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext();
		annotationConfigApplicationContext.register(CustomizeAutowiredTest.class);
		annotationConfigApplicationContext.refresh();
		BeanClass beanClass = annotationConfigApplicationContext.getBean(BeanClass.class);
		beanClass.print();
	}

	@Component
	public static class BeanClass {

		@MyInject
		private FieldClass fieldClass;

		public void print() {
			fieldClass.print();
		}

	}

	@Component
	public static class FieldClass {
		public void print() {
			System.out.println("hello world");
		}
	}

	@Bean
	public AutowiredAnnotationBeanPostProcessor getAutowiredAnnotationBeanPostProcessor() {
		AutowiredAnnotationBeanPostProcessor autowiredAnnotationBeanPostProcessor = new AutowiredAnnotationBeanPostProcessor();
		autowiredAnnotationBeanPostProcessor.setAutowiredAnnotationType(MyInject.class);
		return autowiredAnnotationBeanPostProcessor;
	}

}
