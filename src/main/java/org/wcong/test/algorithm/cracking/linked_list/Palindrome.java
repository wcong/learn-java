package org.wcong.test.algorithm.cracking.linked_list;

import org.wcong.test.algorithm.basic.LinkedList;

/**
 * implement a function to check if a linked list is palindrome
 *
 * @author wcong<wc19920415@gmail.com>
 * @since 17/04/2017
 */
public class Palindrome {

	public static void main(String[] args) {

	}

	static class Result {
		LinkedList.Node node;

		boolean result = true;

	}

	public static boolean isPalindrome(LinkedList.Node root) {
		int length = length(root);
		return true;
	}

	private static Result judge(LinkedList.Node node, int length) {
		if (length <= 0) {
			Result result = new Result();
			result.node = node.next;
			return result;
		} else if (length == 1) {
			Result result = new Result();
			result.node = node;
			return result;
		}
		Result result = judge(node, length - 2);
		if (!result.result) {
			return result;
		}
		result.result = node.value == result.node.value;
		result.node = node.next;
		return result;
	}

	public static int length(LinkedList.Node root) {
		int length = 0;
		while (root != null) {
			length += 1;
			root = root.next;
		}
		return length;
	}

}
