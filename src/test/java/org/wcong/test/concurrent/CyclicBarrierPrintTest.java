package org.wcong.test.concurrent;

import org.junit.Test;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 30/04/2017
 */
public class CyclicBarrierPrintTest {

	public static class CyclicBarrierMonitor implements Runnable {

		private int stage;

		@Override
		public void run() {
			System.out.println("we all are ready for stage :" + stage++);
		}
	}

	@Test
	public void test() {
		CyclicBarrier cyclicBarrier = new CyclicBarrier(10, new CyclicBarrierMonitor());
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		for (int threadNum = 0; threadNum < 10; threadNum++) {
			executorService.submit(new CyclicBarrierPrint(cyclicBarrier));
		}
		executorService.shutdown();
		try {
			executorService.awaitTermination(10000L, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
