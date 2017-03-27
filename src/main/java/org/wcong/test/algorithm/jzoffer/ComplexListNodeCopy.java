package org.wcong.test.algorithm.jzoffer;

import org.wcong.test.algorithm.jzoffer.util.ComplexListNode;

/**
 * test for linked list     TODO
 *
 * @author wcong<wc19920415@gmail.com>
 * @since 2017/3/26
 */
public class ComplexListNodeCopy {

	public static ComplexListNode copyComplexList(ComplexListNode head) {
		if (head == null) {
			return null;
		}
		ComplexListNode node = head;
		while (node != null) {
			ComplexListNode copy = new ComplexListNode();
			copy.value = node.value;
			copy.next = node.next;
			node.next = copy;
			node = copy.next;
		}
		node = head;
		while (node != null) {
			ComplexListNode copy = node.next;
			if (node.other != null) {
				copy.other = node.other.next;
			}
			node = copy.next;
		}
		node = head;
		return node;
	}

}
