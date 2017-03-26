package org.wcong.test.algorithm.jzoffer.util;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 2017/3/26
 */
public class LinkedList {

	public LinkedListNode root;

	public boolean equal(LinkedList other) {
		if (other == null) {
			return false;
		}
		LinkedListNode otherNode = other.root;
		LinkedListNode thisNode = root;
		while (true) {
			if ((otherNode == null && thisNode == null) || (otherNode != null && thisNode != null
					&& thisNode.value == otherNode.value)) {
				if (otherNode == null) {
					break;
				}
				otherNode = otherNode.next;
				thisNode = thisNode.next;
			} else {
				return false;
			}
		}
		return true;
	}

}
