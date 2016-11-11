package org.wcong.test.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;

/**
 * Created by wcong on 2016/9/19.
 */
public class TwoModuleTest {

    public interface Print {
        void print();
    }

    @Singleton
    public static class HelloPrint implements Print {

        public void print() {
            System.out.println("hello");
        }
    }


    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(Print.class).to(HelloPrint.class);
            }
        });
        Print print = injector.getInstance(Print.class);
        System.out.println(print);
        print = injector.getInstance(Print.class);
        System.out.println(print);
    }

}
