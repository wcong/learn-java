package org.wcong.test.autovalue;

/**
 * Created by wcong on 2016/11/26.
 */
public class MyAutoValueTest {
    public static void main(String[] args) {
        org.wcong.test.autovalue.MyAutoValueClassTest_MyAutoValue myAutoValueClassTest_myAutoValue = new org.wcong.test.autovalue.MyAutoValueClassTest_MyAutoValue();
        myAutoValueClassTest_myAutoValue.setA("a");
        myAutoValueClassTest_myAutoValue.setB("b");
        myAutoValueClassTest_myAutoValue.setC(1);
        System.out.println(myAutoValueClassTest_myAutoValue.toString());
    }
}
