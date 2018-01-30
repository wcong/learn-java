package org.wcong.test.concurrent;

import java.util.LinkedList;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 30/04/2017
 */
public class SynchronizedMethod {

	LinkedList<Integer> numList = new LinkedList<>();

	public synchronized Integer get() {
		System.out.println("Thread" + Thread.currentThread().getId() + "get the lock and sleep");
		try {
			Thread.sleep(5000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return numList.pop();
	}

	public synchronized void push(Integer num) {
		System.out.println("Thread" + Thread.currentThread().getId() + "get the lock and sleep");
		try {
			Thread.sleep(5000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		numList.push(num);
	}

	public static class MyGetThread extends Thread {

		private SynchronizedMethod synchronizedMethod;

		public MyGetThread(SynchronizedMethod synchronizedMethod) {
			this.synchronizedMethod = synchronizedMethod;
		}

		public void run() {
			System.out.println("get " + synchronizedMethod.get());
		}
	}

	public static class MyPutThread extends Thread {

		private SynchronizedMethod synchronizedMethod;

		public MyPutThread(SynchronizedMethod synchronizedMethod) {
			this.synchronizedMethod = synchronizedMethod;
		}

		public void run() {
			synchronizedMethod.push(1);
			System.out.println("put 1 ");
		}
	}

}
