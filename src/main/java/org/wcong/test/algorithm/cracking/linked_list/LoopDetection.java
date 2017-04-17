package org.wcong.test.algorithm.cracking.linked_list;

import org.wcong.test.algorithm.basic.LinkedList;

/**
 * given a circle linked list
 * implement an algorithm that return the node at the beginning of the loop
 *
 * @author wcong<wc19920415@gmail.com>
 * @since 17/04/2017
 */
public class LoopDetection {

	public static void main(String[] args) {

	}

	public static LinkedList.Node begining(LinkedList.Node list) {
		LinkedList.Node step1 = list;
		LinkedList.Node step2 = list;
		while (true) {
			if (step1 == null || step2 == null) {
				return null;
			}
			step1 = step1.next;
			if (step2.next == null) {
				return null;
			}
			step2 = step2.next.next;
			if (step1 == step2) {
				break;
			}
		}
		step1 = list;
		while (true) {
			if (step1 == step2) {
				return step1;
			}
			step1 = step1.next;
			step2 = step2.next;
		}
	}

}
