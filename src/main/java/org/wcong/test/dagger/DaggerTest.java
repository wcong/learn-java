package org.wcong.test.dagger;

import dagger.Binds;
import dagger.Component;
import dagger.Lazy;
import dagger.Module;
import dagger.Provides;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 2016/11/2
 */
public class DaggerTest {
	public static void main(String[] args) {
		Coffee coffee = DaggerDaggerTest_Coffee.builder().build();
		coffee.maker().brew();
	}

	@Singleton
	@Component(modules = { DripCoffeeModule.class })
	public interface Coffee {
		CoffeeMaker maker();
	}

	@Module(includes = PumpModule.class)
	static class DripCoffeeModule {
		@Provides
		@Singleton
		Heater provideHeater() {
			return new ElectricHeater();
		}
	}

	@Module
	abstract class PumpModule {
		@Binds
		abstract Pump providePump(Thermosiphon pump);
	}

	static class CoffeeMaker {
		Lazy<Heater> heater;

		Pump pump;

		@Inject
		CoffeeMaker(Lazy<Heater> heater, Pump pump) {
			this.heater = heater;
			this.pump = pump;
		}

		public void brew() {
			pump.pump();
		}
	}

	interface Heater {
	}

	interface Pump {
		void pump();
	}

	static class Thermosiphon implements Pump {
		private final Heater heater;

		@Inject
		Thermosiphon(Heater heater) {
			this.heater = heater;
		}

		public void pump() {
			System.out.println(heater);
		}
	}

	static class ElectricHeater implements Heater {
	}
}
