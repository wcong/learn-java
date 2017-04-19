package org.wcong.test.algorithm.cracking.sort_and_search;

/**
 * you are given two sorted arrays A and B
 * where A has a large enough buffer at the end to hold b
 * write a method merge B into A in sorted order
 *
 * @author wcong<wc19920415@gmail.com>
 * @since 19/04/2017
 */
public class MergeSorted {

	public static void main(String[] args) {

	}

	public static void merge(int[] array1, int[] array2) {
		int length = array1.length + array2.length;
		int array1Index = array1.length - 1;
		int array2Index = array2.length - 1;
		for (int j = length - 1; j >= 0; j--) {
			if (array1Index < 0 && array2Index < 0) {
				break;
			} else if (array1Index < 0) {
				array1[j] = array2[array2Index];
				array2Index -= 1;
			} else if (array2Index < 0) {
				array1[j] = array1[array1Index];
				array1Index -= 1;
			} else {
				if (array1[array1Index] < array2[array2Index]) {
					array1[j] = array2[array2Index];
					array2Index -= 1;
				} else {
					array1[j] = array1[array1Index];
					array1Index -= 1;
				}
			}
		}
	}

}
