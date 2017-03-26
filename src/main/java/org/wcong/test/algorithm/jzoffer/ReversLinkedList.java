package org.wcong.test.algorithm.jzoffer;

import org.springframework.util.Assert;
import org.wcong.test.algorithm.jzoffer.util.BuildLinkedList;
import org.wcong.test.algorithm.jzoffer.util.LinkedList;
import org.wcong.test.algorithm.jzoffer.util.LinkedListNode;

/**
 * test for linked list
 *
 * @author wcong<wc19920415@gmail.com>
 * @since 2017/3/26
 */
public class ReversLinkedList {

	public static void main(String[] args) {
		LinkedList list = BuildLinkedList.makeOne(new int[] { 1, 2, 3, 4, 5, 6, 7 });
		LinkedList reverseList = BuildLinkedList.makeOne(new int[] { 7, 6, 5, 4, 3, 2, 1 });
		Assert.isTrue(reverse(list).equal(reverseList));
	}

	public static LinkedList reverse(LinkedList linkedList) {
		if (linkedList == null || linkedList.root == null) {
			return linkedList;
		}
		LinkedListNode before = null;
		LinkedListNode node = linkedList.root;
		LinkedListNode next = node.next;
		node.next = null;
		while (next != null) {
			before = node;
			node = next;
			next = next.next;
			node.next = before;
		}
		linkedList.root = node;
		return linkedList;
	}

}
