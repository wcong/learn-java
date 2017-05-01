package org.wcong.test.concurrent;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 29/04/2017
 */
public class Basic {

	public static class MyThread extends Thread {

		private CountDownLatch countDownLatch;

		public MyThread(CountDownLatch countDownLatch) {
			this.countDownLatch = countDownLatch;
		}

		@Override
		public void run() {
			try {
				countDownLatch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("I'm running");
		}
	}

	public static class MyRunner implements Runnable {

		private CountDownLatch countDownLatch;

		public MyRunner(CountDownLatch countDownLatch) {
			this.countDownLatch = countDownLatch;
		}

		@Override
		public void run() {
			try {
				countDownLatch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("I'm running too");
		}
	}

	public static void main(String[] args) throws InterruptedException {

		CountDownLatch countDownLatch = new CountDownLatch(1);
		Thread thread = new MyThread(countDownLatch);
		thread.start();
		Thread secondThread = new Thread(new MyRunner(countDownLatch));
		secondThread.start();
		System.out.println("wait for it");
		thread.join();
		countDownLatch.countDown();
		TimeUnit.MILLISECONDS.sleep(1000);
	}

}
