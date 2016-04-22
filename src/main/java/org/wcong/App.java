package org.wcong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Hello world!
 */
@Configuration
public class App {
	public static void main(String[] args) {
		BigDecimal test = new BigDecimal("00.0001");
		test = test.setScale(3,BigDecimal.ROUND_DOWN);
		System.out.println(test);
		System.out.println(test.multiply(new BigDecimal(1000)).divide(new BigDecimal(1000)));
		System.out.println(test.unscaledValue().equals(BigInteger.ZERO));
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

		@Autowired
		private Test test;

		private String helloWorld = "Hello World!";

		public String getHelloWorld() {
			return helloWorld;
		}

	}
}
