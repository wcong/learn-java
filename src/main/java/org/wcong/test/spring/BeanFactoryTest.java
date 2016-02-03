package org.wcong.test.spring;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 16/2/3
 */
public class BeanFactoryTest {

	public static void main(String[] args) {
		DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
		BeanDefinition beanDefinition = new GenericBeanDefinition();
		beanDefinition.setBeanClassName(BeanTest.class.getName());
		beanFactory.registerBeanDefinition(beanDefinition.getBeanClassName(), beanDefinition);
		BeanTest beanTest = beanFactory.getBean(beanDefinition.getBeanClassName(), BeanTest.class);
		beanTest.hello();
	}

	public static class BeanTest {

		public void hello() {
			System.out.println("hello");
		}

	}

}
