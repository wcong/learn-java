package org.wcong.test.concurrent;

import java.util.concurrent.TimeUnit;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 29/04/2017
 */
public class ThreadWait {

	public static class MyThread extends Thread {
		public MyThread(String name) {
			setName(name);
		}

		public void run() {
			TimeUnit.MILLISECONDS.toSeconds(1000);
			System.out.println(getName());
		}
	}

	public static void main(String[] args) throws InterruptedException {
		MyThread thread1 = new MyThread("1");
		MyThread thread2 = new MyThread("2");
		thread1.start();
		thread2.start();
		thread1.join();
		thread2.join();
		System.out.println("finished");

	}

}
