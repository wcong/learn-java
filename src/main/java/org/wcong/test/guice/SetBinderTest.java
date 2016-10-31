package org.wcong.test.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;

import java.util.List;
import java.util.Set;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 2016/10/31
 */
public class SetBinderTest {

	public static void main(String[] args) {
		Injector injector = Guice.createInjector(new AbstractModule() {
			@Override
			protected void configure() {
				Multibinder<String> multibinder = Multibinder.newSetBinder(binder(), String.class);
				multibinder.addBinding().toInstance("hello");
				multibinder.addBinding().toInstance("world");
			}
		});
		Set<String> stringList = injector.getInstance(Key.get(new TypeLiteral<Set<String>>() {
		}));
		System.out.println(stringList);
	}

}
