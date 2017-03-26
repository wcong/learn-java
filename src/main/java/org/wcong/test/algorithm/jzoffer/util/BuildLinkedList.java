package org.wcong.test.algorithm.jzoffer.util;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 2017/3/26
 */
public class BuildLinkedList {

	public static LinkedList makeOne(int[] array) {
		LinkedListNode root = new LinkedListNode();
		LinkedList linkedList = new LinkedList();
		linkedList.root = root;
		root.value = array[0];
		LinkedListNode parent = root;
		for (int i = 1; i < array.length; i++) {
			LinkedListNode node = new LinkedListNode();
			node.value = array[i];
			parent.next = node;
			parent = node;
		}
		return linkedList;
	}
}
