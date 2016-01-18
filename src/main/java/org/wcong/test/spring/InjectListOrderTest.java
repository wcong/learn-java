/**
 * Copyright 2014-2015, NetEase, Inc. All Rights Reserved.
 * Date: 15/12/15
 */
package org.wcong.test.spring;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * test for inject list
 *
 * @author wcong<wc19920415@gmail.com>
 * @since 15/12/15
 */
@Configuration
public class InjectListOrderTest {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(
				InjectListOrderTest.class);
		String[] beanNames = annotationConfigApplicationContext.getBeanNamesForType(PrintSomething.class);
		List<PrintSomething> printSomethingList = new ArrayList<PrintSomething>(beanNames.length);
		for (String beanName : beanNames) {
			System.out.println(beanName);
			printSomethingList.add((PrintSomething) annotationConfigApplicationContext.getBean(beanName));
		}
		for (PrintSomething printSomething : printSomethingList) {
			printSomething.print();
		}
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
	public static class PrintWorld implements PrintSomething {

		public void print() {
			System.out.println("world");
		}
	}

}
