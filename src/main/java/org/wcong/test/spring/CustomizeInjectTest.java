package org.wcong.test.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

@Configuration
public class CustomizeInjectTest {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext();
		annotationConfigApplicationContext.register(CustomizeInjectTest.class);
		annotationConfigApplicationContext.refresh();
		BeanClass beanClass = annotationConfigApplicationContext.getBean(BeanClass.class);
		beanClass.print();
		FullInjectBeanClass fullInjectBeanClass = annotationConfigApplicationContext.getBean(FullInjectBeanClass.class);
		fullInjectBeanClass.print();
		fullInjectBeanClass.superPrint();
	}

	@Component
	public static class BeanClass {

		@MyInject
		private FieldClass fieldClass;

		public void print() {
			fieldClass.print();
		}

	}

	@Component
	public static class FieldClass {
		public void print() {
			System.out.println("hello world");
		}
	}

	public static class FullInjectSuperBeanClass {
		private FieldClass superFieldClass;

		public void superPrint() {
			superFieldClass.print();
		}

	}

	@Component
	@FullInject
	public static class FullInjectBeanClass extends FullInjectSuperBeanClass {
		private FieldClass fieldClass;

		public void print() {
			fieldClass.print();
		}
	}

	@Component
	public static class MyInjectBeanPostProcessor implements BeanPostProcessor, ApplicationContextAware {

		private ApplicationContext applicationContext;

		public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
			if (hasAnnotation(bean.getClass().getAnnotations(), FullInject.class.getName())) {
				Class beanClass = bean.getClass();
				do {
					Field[] fields = beanClass.getDeclaredFields();
					for (Field field : fields) {
						setField(bean, field);
					}
				} while ((beanClass = beanClass.getSuperclass()) != null);
			} else {
				processMyInject(bean);
			}
			return bean;
		}

		private void processMyInject(Object bean) {
			Class beanClass = bean.getClass();
			do {
				Field[] fields = beanClass.getDeclaredFields();
				for (Field field : fields) {
					if (!hasAnnotation(field.getAnnotations(), MyInject.class.getName())) {
						continue;
					}
					setField(bean, field);
				}
			} while ((beanClass = beanClass.getSuperclass()) != null);
		}

		private void setField(Object bean, Field field) {
			if (!field.isAccessible()) {
				field.setAccessible(true);
			}
			try {
				field.set(bean, applicationContext.getBean(field.getType()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private boolean hasAnnotation(Annotation[] annotations, String annotationName) {
			if (annotations == null) {
				return false;
			}
			for (Annotation annotation : annotations) {
				if (annotation.annotationType().getName().equals(annotationName)) {
					return true;
				}
			}
			return false;
		}

		public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
			return bean;
		}

		public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
			this.applicationContext = applicationContext;
		}
	}

}
