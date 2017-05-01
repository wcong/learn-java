package org.wcong.test.concurrent;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 30/04/2017
 */
public class PhaserPrintTest {

	@Test
	public void test() {
		final Phaser phaser = new Phaser(10);   // this will add to 10 emedialy,do not register again;
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		for (int threadNum = 0; threadNum < 10; threadNum++) {
			executorService.submit(new PhaserPrint(phaser));
		}
		executorService.shutdown();
		try {
			executorService.awaitTermination(10000L, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
