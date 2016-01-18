package org.wcong.test.spring;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 16/1/15
 */
@Configuration
public class ApplicationListenerTest {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext();
		annotationConfigApplicationContext.register(ApplicationListenerTest.class);
		annotationConfigApplicationContext.refresh();
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

	@Component
	public static class ApplicationListenerComponent implements ApplicationListener {

		public void onApplicationEvent(ApplicationEvent event) {
			System.out.println(event);
		}
	}

}
