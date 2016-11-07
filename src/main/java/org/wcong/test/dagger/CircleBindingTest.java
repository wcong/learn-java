package org.wcong.test.dagger;

import dagger.Binds;
import dagger.Component;
import dagger.Module;
import dagger.Provides;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 2016/11/7
 */
public class CircleBindingTest {
	public static void main(String[] args) {
		MyComponent myComponent = DaggerCircleBindingTest_MyComponent.builder().build();
		myComponent.getExportClass().print();
	}

	@Singleton
	@Component(modules = { MyBindModule.class, MyProvidesModule.class })
	interface MyComponent {
		ExportClass getExportClass();
	}

	static class ExportClass {
		private final MyClass myClass;


		@Inject
		public ExportClass(MyClass myClass) {
			this.myClass = myClass;
		}

		public void print(){

		}
	}

	@Module
	static class MyProvidesModule {
		@Provides
		MyParam getMyParam() {
			return new MyParam();
		}
	}

	@Module
	abstract class MyBindModule {

		@Binds
		abstract MyClass myClass(MySubClass a);
	}

	static class MyClass {

	}

	static class MySubClass extends MyClass {

		private final MyParam myParam;

		@Inject
		public MySubClass(MyParam myParam) {
			this.myParam = myParam;
		}
	}

	static class MyParam {
	}
}
