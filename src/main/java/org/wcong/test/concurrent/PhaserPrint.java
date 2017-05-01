package org.wcong.test.concurrent;

import java.util.concurrent.Phaser;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 30/04/2017
 */
public class PhaserPrint implements Runnable {

	private Phaser phaser;

	public PhaserPrint(Phaser phaser) {
		this.phaser = phaser;
	}

	@Override
	public void run() {
		System.out.println("thread:" + Thread.currentThread().getId() + ":print 1");
		phaser.arriveAndAwaitAdvance();
		System.out.println("thread:" + Thread.currentThread().getId() + ":print 2");
		phaser.arriveAndAwaitAdvance();
		System.out.println("thread:" + Thread.currentThread().getId() + ":print 3");
		phaser.arriveAndAwaitAdvance();
		System.out.println("thread:" + Thread.currentThread().getId() + ":print 4");
		phaser.arriveAndAwaitAdvance();
		System.out.println("thread:" + Thread.currentThread().getId() + ":finished");
		phaser.arriveAndDeregister();
	}
}
