/**
 * Copyright 2014-2015, NetEase, Inc. All Rights Reserved.
 * Date: 16/1/15
 */
package org.wcong.test.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

/**
 * test of BeanPostProcessor
 *
 * @author wcong<wc19920415@gmail.com>
 * @since 16/1/15
 */
@Configuration
public class BeanPostProcessorTest {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(
				BeanPostProcessorTest.class);
		PrintSomething printSomething = annotationConfigApplicationContext.getBean(PrintSomething.class);
		printSomething.print();
	}

	public interface PrintSomething {
		void print();
	}

	@Service
	public static class PrintHello implements PrintSomething {

		public void print() {
			System.out.println("hello");
		}
	}

	@Service
	public static class BeanPostProcessorService implements BeanPostProcessor {

		public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
			System.out.println(bean);
			return bean;
		}

		public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
			System.out.println(bean);
			return bean;
		}
	}
}
