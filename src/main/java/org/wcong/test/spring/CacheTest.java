package org.wcong.test.spring;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 16/2/22
 */
@Configuration
@EnableCaching
public class CacheTest {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext();
		annotationConfigApplicationContext.register(CacheTest.class);
		annotationConfigApplicationContext.refresh();
		ComponentTest componentTest = annotationConfigApplicationContext.getBean(ComponentTest.class);
		System.out.println("get " + componentTest.getHelloWorld());
		System.out.println("get " + componentTest.getHelloWorld());
		System.out.println("get " + componentTest.getHelloWorld());
	}

	@Bean(name = "defaultCache")
	public CacheManager getCacheManeger() {
		return new ConcurrentMapCacheManager();
	}

	@Component
	public static class ComponentTest {
		@Cacheable("defaultCache")
		public String getHelloWorld() {
			String helloWorld = "hello world";
			System.out.println(helloWorld);
			return helloWorld;
		}
	}
}
