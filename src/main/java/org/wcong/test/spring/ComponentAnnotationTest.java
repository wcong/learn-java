package org.wcong.test.spring;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 16/1/21
 */
@Configuration
public class ComponentAnnotationTest {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext();
		annotationConfigApplicationContext.register(ComponentAnnotationTest.class);
		annotationConfigApplicationContext.refresh();
		InjectClass injectClass = annotationConfigApplicationContext.getBean(InjectClass.class);
		injectClass.print();
	}

	@MyComponent
	public static class InjectClass {

		public void print() {
			System.out.println("hello world");
		}
	}
}
