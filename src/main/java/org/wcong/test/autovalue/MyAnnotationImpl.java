package org.wcong.test.autovalue;

import java.lang.annotation.Annotation;

/**
 * Created by hzwangcong on 2016/11/22.
 */
public class MyAnnotationImpl implements MyAnnotation {
    public String value() {
        return null;
    }

    public Class<? extends Annotation> annotationType() {
        return null;
    }
}
