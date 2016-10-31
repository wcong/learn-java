package org.wcong.test.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.multibindings.MapBinder;

import java.util.Map;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 2016/10/31
 */
public class MapBinderTest {
	public static void main(String[] args) {
		Injector injector = Guice.createInjector(new AbstractModule() {
			@Override
			protected void configure() {
				MapBinder<String, String> mapBinder = MapBinder.newMapBinder(binder(), String.class, String.class);
				mapBinder.addBinding("hello").toInstance("world");
				binder().bind(MapContainer.class).asEagerSingleton();
			}
		});
		MapContainer mapContainer = injector.getInstance(MapContainer.class);
		System.out.println(mapContainer.toString());
	}

	public static class MapContainer {

		private Map<String, String> data;

		@Inject
		public MapContainer(Map<String, String> data) {
			this.data = data;
		}

		public String toString() {
			return data.toString();
		}
	}
}
