package org.wcong.test.spring.aop;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 16/4/12
 */
@Configuration
public class CustomizeAspectTest {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext();
		annotationConfigApplicationContext.register(CustomizeAspectTest.class);
		annotationConfigApplicationContext.refresh();
		Test test = annotationConfigApplicationContext.getBean(Test.class);
		test.test();
	}

	@Component
	public static class Test {
		public void test() {
			System.out.println("hello world");
		}
	}

	@MyAspect(pointCut = "org.wcong.test.spring.aop.CustomizeAspectTest.Test.test")
	public static class MyAspectClass {

		void before(Object[] args) {
			System.out.println("aop before");
		}

		void after(Object[] args) {
			System.out.println("aop after");
		}

		void around(MethodInvocation methodInvocation, Object[] args) throws Throwable {
			System.out.println("aop around before");
			methodInvocation.proceed(methodInvocation);
			System.out.println("aop around after");
		}
	}

	@Bean
	public CustomizeAspectProxy getCustomizeAspectScan() {
		return new CustomizeAspectProxy();
	}

}
