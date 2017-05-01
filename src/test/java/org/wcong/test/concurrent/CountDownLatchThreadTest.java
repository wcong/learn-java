package org.wcong.test.concurrent;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 30/04/2017
 */
public class CountDownLatchThreadTest {

	@Test
	public void test() {
		CountDownLatch countDownLatch = new CountDownLatch(10);
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		for (int threadNum = 0; threadNum < 10; threadNum++) {
			executorService.submit(new CountDownLatchThread(countDownLatch));
		}
		executorService.shutdown();
		try {
			executorService.awaitTermination(100000L, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
