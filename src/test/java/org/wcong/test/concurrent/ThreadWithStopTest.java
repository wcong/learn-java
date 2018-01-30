package org.wcong.test.concurrent;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 30/04/2017
 */
public class ThreadWithStopTest {

	@Test
	public void testStop() {
		ThreadWithStop thread = new ThreadWithStop();
		thread.start();
		try {
			TimeUnit.MILLISECONDS.sleep(5000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("i will set you stop");
		thread.setStop(true);
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
