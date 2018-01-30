package org.wcong.test.concurrent;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 29/04/2017
 */
public class ExceptionHandler {

	public static class MyThread extends Thread {
		public void run() {
			throw new RuntimeException("hello exception");
		}
	}

	public static class MyExceptionHandler implements Thread.UncaughtExceptionHandler {

		@Override
		public void uncaughtException(Thread t, Throwable e) {
			System.out.println(e.toString());
		}
	}

	public static void main(String[] args) {
		MyThread myThread = new MyThread();
		myThread.setUncaughtExceptionHandler(new MyExceptionHandler());
		myThread.start();
		System.out.println("haha");
	}

}
