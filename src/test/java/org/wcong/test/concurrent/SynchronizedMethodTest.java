package org.wcong.test.concurrent;

import org.junit.Test;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 30/04/2017
 */
public class SynchronizedMethodTest {

	@Test
	public void test() {
		SynchronizedMethod synchronizedMethod = new SynchronizedMethod();
		SynchronizedMethod.MyPutThread myPutThread = new SynchronizedMethod.MyPutThread(synchronizedMethod);
		myPutThread.start();
		SynchronizedMethod.MyGetThread myGetThread = new SynchronizedMethod.MyGetThread(synchronizedMethod);
		myGetThread.start();
		try {
			myGetThread.join();
			myPutThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
