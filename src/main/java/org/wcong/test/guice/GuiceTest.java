package org.wcong.test.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matcher;
import com.google.inject.spi.ProvisionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 2016/10/29
 */
public class GuiceTest {

	public static void main(String[] args) {
		Injector injector = Guice.createInjector(new AbstractModule() {
			@Override
			protected void configure() {
				bind(BindClass.class).toInstance(new BindClass());
				bindListener(new MyMatcher(), new MyTypeListener());
				bindListener(new MyMatcher(), new MyProvisionListener());
			}
		});
		System.out.println(injector.getInstance(BindClass.class).message);
	}

	public static class BindClass {
		public String message = "hello world";
	}

	public static class MyMatcher implements Matcher {

		public boolean matches(Object o) {
			return true;
		}

		public Matcher and(Matcher other) {
			return null;
		}

		public Matcher or(Matcher other) {
			return null;
		}
	}

	public static class MyTypeListener implements TypeListener {

		public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
			System.out.println("hehehe");
		}
	}

	public static class MyProvisionListener implements ProvisionListener {

		public <T> void onProvision(ProvisionInvocation<T> provision) {
			System.out.println("hahaha");
		}
	}

}
