package org.wcong.test.algorithm.jzoffer;

import java.util.Stack;

/**
 * test for stack and its operation
 * implement a push pop and min all O(1) time use
 *
 * @author wcong<wc19920415@gmail.com>
 * @since 2017/3/26
 */
public class O1MinStack {

	public static class MinO1Stack {

		private Stack<Integer> normal;

		private Stack<Integer> min;

		public void push(int num) {
			normal.push(num);
			if (min.isEmpty() || min.lastElement() > num) {
				min.push(num);
			} else {
				min.push(min.lastElement());
			}
		}

		public int pop() {
			min.pop();
			return normal.pop();
		}

		public int min() {
			return min.lastElement();
		}

	}

}
