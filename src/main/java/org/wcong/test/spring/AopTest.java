package org.wcong.test.spring;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
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

		@Pointcut("within(org.wcong.test.spring.AopTest.Test)")
		public void test() {

		}

		@Before("org.wcong.test.spring.AopTest.Aop.test() && args(num)")
		public void testNumBefore(Integer num) {
			System.out.println("aop before:" + num);
		}

		@After("org.wcong.test.spring.AopTest.Aop.test() && args(num)")
		public void testNumAfter(Integer num) {
			System.out.println("aop after:" + num);
		}

		@Around("org.wcong.test.spring.AopTest.Aop.test() && args(num)")
		public void testNumAround(ProceedingJoinPoint pjp, Integer num) throws Throwable {
			System.out.println("aop around before:" + num);
			Object[] objects = new Object[1];
			objects[0] = num;
			pjp.proceed(objects);
			System.out.println("aop around after:" + num);
		}

		@AfterReturning("org.wcong.test.spring.AopTest.Aop.test() && args(num)")
		public void testNumAfterReturn(Integer num) throws Throwable {
			System.out.println("aop after return:" + num);
		}

		@AfterThrowing("org.wcong.test.spring.AopTest.Aop.test() && args(num)")
		public void testNumAfterThrowing(Integer num) throws Throwable {
			System.out.println("aop after throwing:" + num);
		}

	}
}
