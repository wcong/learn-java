package org.wcong.test;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

/**
 * @author wcong
 * @since 15/12/5
 */
@Configuration
@EnableAspectJAutoProxy
public class AopTest {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(
				AopTest.class);
		Test test = annotationConfigApplicationContext.getBean(Test.class);
		test.test(1);
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

		@Pointcut("within(org.wcong.test.AopTest.Test)")
		public void test() {

		}

		@Before("org.wcong.test.AopTest.Aop.test()")
		public void testNum(Integer num) {
			System.out.println("aop:" + num);
		}

	}
}
