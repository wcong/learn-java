package org.wcong.test.concurrent;

import java.util.LinkedList;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 29/04/2017
 */
public class ProducerAndComsumer {

	private static class Queue {

		private int maxSize;

		public Queue(int maxSize) {
			this.maxSize = maxSize;
		}

		LinkedList<Integer> integerList = new LinkedList<>();

		public synchronized Integer get() {
			while (integerList.isEmpty()) {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return integerList.pop();

		}

		public synchronized void add(Integer num) {
			while (maxSize == integerList.size()) {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			integerList.push(num);
		}

	}

}
