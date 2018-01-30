package org.wcong.test.algorithm.cracking.dp_divide_conque;

/**
 * a magic index in an array A[1....n-1] is defined to be index such that A[i]=i
 * given a sorted array of distinct integers
 * write a method to find the magic index,if one exist in array an index
 *
 * @author wcong<wc19920415@gmail.com>
 * @since 19/04/2017
 */
public class MagicIndex {

	public int magicIndexDistinct(int[] array) {
		int start = 0, end = array.length - 1;
		while (start < end) {
			int middle = start + (end - start) / 2;
			if (array[middle] == middle) {
				return middle;
			} else if (array[middle] > middle) {
				end = middle - 1;
			} else {
				start = middle + 1;
			}
		}
		return -1;
	}

	public int magicIndexNormal(int[] array) {
		int i = 0;
		while (i < array.length) {
			if (array[i] == i) {
				return i;
			} else if (array[i] > i) {
				i += 1;
			} else {
				i = array[i] + 1;
			}
		}
		return -1;
	}

}
