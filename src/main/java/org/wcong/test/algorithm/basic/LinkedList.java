package org.wcong.test.algorithm.basic;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 17/04/2017
 */
public class LinkedList {

	public Node root;

	public static class Node {

		public int value;

		public Node next;

		public String toString() {
			return String.valueOf(value);
		}
	}

}
