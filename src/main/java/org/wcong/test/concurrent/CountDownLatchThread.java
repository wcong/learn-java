package org.wcong.test.concurrent;

import java.util.concurrent.CountDownLatch;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 30/04/2017
 */
public class CountDownLatchThread implements Runnable {

	private CountDownLatch countDownLatch;

	public CountDownLatchThread(CountDownLatch countDownLatch) {
		this.countDownLatch = countDownLatch;
	}

	public void run() {
		System.out.println("wait for it");
		countDownLatch.countDown();
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("start");
	}
}
