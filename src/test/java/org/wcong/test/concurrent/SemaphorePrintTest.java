package org.wcong.test.concurrent;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 30/04/2017
 */
public class SemaphorePrintTest {

	static class PrintRunner implements Runnable {

		private SemaphorePrint semaphorePrint;

		public PrintRunner(SemaphorePrint semaphorePrint) {
			this.semaphorePrint = semaphorePrint;
		}

		@Override
		public void run() {
			semaphorePrint.print();
		}
	}

	@Test
	public void test() {
		SemaphorePrint semaphorePrint = new SemaphorePrint();
		ExecutorService executor = Executors.newFixedThreadPool(10);
		for (int threadNum = 0; threadNum < 10; threadNum++) {
			executor.submit(new PrintRunner(semaphorePrint));
		}
		executor.shutdown();
		try {
			executor.awaitTermination(10000L, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
