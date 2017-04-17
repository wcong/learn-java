package org.wcong.test.algorithm.jzoffer.linked_list;

import org.springframework.util.Assert;
import org.wcong.test.algorithm.jzoffer.util.BuildLinkedList;
import org.wcong.test.algorithm.jzoffer.util.LinkedList;
import org.wcong.test.algorithm.jzoffer.util.LinkedListNode;

/**
 * test linked list and merge
 *
 * @author wcong<wc19920415@gmail.com>
 * @since 2017/3/26
 */
public class MergeTwoSortedLinkedList {

	public static void main(String[] args) {
		LinkedList list1 = BuildLinkedList.makeOne(new int[] { 1, 3, 4, 5, 6 });
		LinkedList list2 = BuildLinkedList.makeOne(new int[] { 2, 4, 7, 8, 9 });
		LinkedList merge = BuildLinkedList.makeOne(new int[] { 1, 2, 3, 4, 4, 5, 6, 7, 8, 9 });
		Assert.isTrue(mergeSortedList(list1, list2).equal(merge));
	}

	public static LinkedList mergeSortedList(LinkedList list1, LinkedList list2) {
		if (list1 == null || list1.root == null) {
			return list2;
		}
		if (list2 == null || list2.root == null) {
			return list1;
		}
		LinkedListNode node1 = list1.root;
		LinkedListNode node2 = list2.root;
		LinkedList root = node1.value > node2.value ? list2 : list1;
		while (node1 != null && node2 != null) {
			if (node1.value < node2.value) {
				while (node1.next != null && node1.next.value < node2.value) {
					node1 = node1.next;
				}
				LinkedListNode temp = node1.next;
				node1.next = node2;
				node1 = temp;
			} else if (node1.value >= node2.value) {
				while (node2.next != null && node2.next.value <= node1.value) {
					node2 = node2.next;
				}
				LinkedListNode temp = node2.next;
				node2.next = node1;
				node2 = temp;
			}
		}
		return root;
	}

}
