package org.wcong.test.spring.annotation;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;

/**
 * Created by wcong on 2016/6/16.
 */
@Configuration
public class DefaultAnnotationNameTest {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext();
        annotationConfigApplicationContext.register(DefaultAnnotationNameTest.class);
        annotationConfigApplicationContext.refresh();
    }


}
