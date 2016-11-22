package org.wcong.test.autovalue;

/**
 * Created by wcong on 2016/11/22.
 */
public class AutoValueTest {
    public static void main(String[] args) {
        MyClass myClass = MyClass.create("world");
        System.out.println(myClass);
    }
}
