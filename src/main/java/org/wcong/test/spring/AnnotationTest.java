package org.wcong.test.spring;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 15/12/4
 */
@Configuration
@EnableCaching
public class AnnotationTest {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(
				AnnotationTest.class);
		CacheNum cacheNum = annotationConfigApplicationContext.getBean(CacheNum.class);
		for (int i = 0; i < 10; i++) {
			System.out.println(cacheNum.getNum());
		}
	}

	@Bean
	public CacheManager cacheManager() {
		SimpleCacheManager cacheManager = new SimpleCacheManager();
		cacheManager.setCaches(Collections.singletonList(new ConcurrentMapCache("num")));
		return cacheManager;
	}

	@Component
	public static class CacheNum {
		int a = 1;

		@Cacheable("num")
		public Integer getNum() {
			return a++;
		}
	}

}
