package org.wcong.test.concurrent;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 30/04/2017
 */
public class ThreadWithStop extends Thread {

	private boolean stop;

	public void setStop(boolean stop) {
		this.stop = stop;
	}

	public void run() {
		while (!stop) {
			try {
				sleep(1000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("I'm running");
		}
		System.out.println("I stopped");
	}

}
