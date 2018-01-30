package org.wcong.test.algorithm.cracking.linked_list;

import org.wcong.test.algorithm.basic.LinkedList;

/**
 * give a linked list delete the middle node of it
 *
 * @author wcong<wc19920415@gmail.com>
 * @since 17/04/2017
 */
public class DeleteMiddle {

	public static void main(String[] args) {

	}

	public static void deleteMiddle(LinkedList.Node root) {
		if (root == null) {
			return;
		}
		LinkedList.Node current = root;
		LinkedList.Node middle = root;
		int middleCount = 1;
		int count = 0;
		while (current != null) {
			count += 1;
			current = current.next;
			while (middleCount < (count / 2)) {
				middle = middle.next;
				middleCount += 1;
			}
		}
		middle.next = middle.next.next;
	}

}
