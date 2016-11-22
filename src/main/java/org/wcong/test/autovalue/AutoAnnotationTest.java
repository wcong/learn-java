package org.wcong.test.autovalue;

import com.google.auto.value.AutoAnnotation;

/**
 * Created by hzwangcong on 2016/11/22.
 */
public class AutoAnnotationTest {

    public static void main(String[] args) {
        MyAnnotation myAnnotation = new MyAnnotationImpl();
        System.out.println(myAnnotation("hello"));
    }

    @AutoAnnotation
    public static MyAnnotation myAnnotation(String value) {
        return new AutoAnnotation_AutoAnnotationTest_myAnnotation(value);
    }
}
