package org.wcong.test.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * Integrate into SpringBeanFactory
 *
 * @author wcong<wc19920415@gmail.com>
 * @since 16/1/15
 */
@Configuration
public class IntegrateSpringBeanFactory {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext();
		annotationConfigApplicationContext.addBeanFactoryPostProcessor(new BeanFactoryPostProcessorTest());
		annotationConfigApplicationContext.register(PrintHello.class);
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

	public static class BeanFactoryPostProcessorTest implements BeanFactoryPostProcessor {

		public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
			System.out.println(Arrays.asList(beanFactory.getBeanDefinitionNames()));
		}
	}

}
