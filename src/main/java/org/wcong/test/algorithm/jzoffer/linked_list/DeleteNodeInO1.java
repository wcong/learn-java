package org.wcong.test.algorithm.jzoffer.linked_list;

import org.springframework.util.Assert;

/**
 * test for linked list basic operate,search,add,delete
 * give a linked list root and delete node,delete this node in O(1) time
 *
 * @author wcong<wc19920415@gmail.com>
 * @since 2017/3/26
 */
public class DeleteNodeInO1 {

	public static void main(String[] args) {
	}

	public static class LinkedNode {
		int value;

		LinkedNode next;
	}

	public static LinkedNode deleteInO1(LinkedNode root, LinkedNode delete) {
		if (root == null || delete == null) {
			return null;
		}
		if (delete.next != null) {
			delete.value = delete.next.value;
			delete.next = delete.next.next;
		} else if (root == delete) {
			return null;
		} else {
			LinkedNode parent = root;
			while (parent.next != delete) {
				parent = parent.next;
			}
			parent.next = null;
		}
		return root;
	}

}
