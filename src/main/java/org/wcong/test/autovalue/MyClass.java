package org.wcong.test.autovalue;

import com.google.auto.value.AutoValue;

/**
 * Created by wcong on 2016/11/22.
 */
@AutoValue
abstract class MyClass {
    static MyClass create(String hello) {
        return new AutoValue_MyClass(hello);
    }

    abstract String hello();
}
