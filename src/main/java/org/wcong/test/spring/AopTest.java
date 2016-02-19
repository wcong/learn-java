package org.wcong.test.spring;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @author wcong
 * @since 15/12/5
 */
@Configuration
@EnableAspectJAutoProxy
@EnableWebMvc
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

		@Before("org.wcong.test.spring.AopTest.Aop.test()")
		public void testNum(Integer num) {
			System.out.println("aop:" + num);
		}

	}
}
