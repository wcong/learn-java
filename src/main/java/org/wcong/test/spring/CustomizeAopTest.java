package org.wcong.test.spring;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

@Configuration
public class CustomizeAopTest {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext();
		annotationConfigApplicationContext.register(CustomizeAopTest.class);
		annotationConfigApplicationContext.refresh();
		Test test = annotationConfigApplicationContext.getBean(Test.class);
		test.test(1);
	}

	@Bean
	public AnnotationAwareAspectJAutoProxyCreator makeAnnotationAwareAspectJAutoProxyCreator() {
		return new AnnotationAwareAspectJAutoProxyCreator();
	}

	@Bean
	public Aop aop() {
		return new Aop();
	}

	@Component
	public static class Test {

		public void test(Integer num) {
			System.out.println(num);
		}

	}

	@Aspect
	public static class Aop {

		@Pointcut("within(org.wcong.test.spring.CustomizeAopTest.Test)")
		public void test() {

		}

		@Before("org.wcong.test.spring.CustomizeAopTest.Aop.test() && args(num)")
		public void testNum(Integer num) {
			System.out.println("aop:" + num);
		}

	}
}
