package org.wcong.test.concurrent;

import java.util.concurrent.Semaphore;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 30/04/2017
 */
public class SemaphorePrint {

	private Semaphore semaphore = new Semaphore(2);

	public void print() {
		try {
			semaphore.acquire();
			System.out.println("acquire the semaphore :" + Thread.currentThread().getId() + ";");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			System.out.println("release the semaphore :" + Thread.currentThread().getId() + ";");
			semaphore.release();
		}
	}

}
