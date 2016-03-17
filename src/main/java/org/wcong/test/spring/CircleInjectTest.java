package org.wcong.test.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

/**
 * how spring resolve circle singleton object inject
 *
 * DefaultSingletonBeanRegistry->singletonObjects cache all  singleton object reference
 *
 * if sharedInstance != null ,we think we just want use the reference,so skip write field .
 * if sharedInstance == null ,we think we are create the object so, goto else,write field .
 *
    Object sharedInstance = getSingleton(beanName);
    if (sharedInstance != null && args == null) {
        if (logger.isDebugEnabled()) {
            if (isSingletonCurrentlyInCreation(beanName)) {
                logger.debug("Returning eagerly cached instance of singleton bean '" + beanName +
                "' that is not fully initialized yet - a consequence of a circular reference");
            }
        else {
            logger.debug("Returning cached instance of singleton bean '" + beanName + "'");
        }
    }
    bean = getObjectForBeanInstance(sharedInstance, name, beanName, null);
    }else{
    	......
    }
 *
 *
 *
 * @author hzwangcong<hzwangcong@corp.netease.com>
 * @since 15/12/15
 */
@Configuration
public class CircleInjectTest {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(
				CircleInjectTest.class);
		Service1 service1 = annotationConfigApplicationContext.getBean(Service1.class);
		service1.printService2();
		Service2 service2 = annotationConfigApplicationContext.getBean(Service2.class);
		service2.printService1();
	}

	@Service
	public static class Service1 {

		@Autowired
		private Service2 service2;

		public void printService2() {
			System.out.println(service2);
		}

		public String toString() {
			return "Service1";
		}

	}

	@Service
	public static class Service2 {

		@Autowired
		private Service1 service1;

		public void printService1() {
			System.out.println(service1);
		}

		public String toString() {
			return "Service2";
		}

	}

}
