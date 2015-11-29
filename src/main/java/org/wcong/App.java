package org.wcong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Hello world!
 */
@Configuration
public class App {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(
				App.class);
		Test test = annotationConfigApplicationContext.getBean(Test.class);
		System.out.println(test.getHelloWorld());
	}

	@Component
	public static class Test {

		@Autowired
		private TestField testField;

		public String getHelloWorld() {
			return testField.getHelloWorld();
		}

	}

	@Component
	public static class TestField {

		private String helloWorld = "Hello World!";

		public String getHelloWorld() {
			return helloWorld;
		}

	}
}
