package org.wcong.test.algorithm.cracking.sort_and_search;

/**
 * given a sorted array of string that is interspersed with empty string
 * write a method to find the location of the string
 *
 * @author wcong<wc19920415@gmail.com>
 * @since 19/04/2017
 */
public class SparseSearch {

	public static void main(String[] args) {

	}

	public static int index(String[] array, String target) {
		return index(array, target, 0, array.length);
	}

	private static int index(String[] array, String target, int start, int end) {
		if (start > end) {
			return -1;
		}
		int middle = start + (end - start) / 2;
		if (array[middle].length() == 0) {
			int leftIndex = index(array, target, start, middle - 1);
			if (leftIndex > 0) {
				return leftIndex;
			}
			return index(array, target, middle + 1, end);
		} else {
			if (target.equals(array[middle])) {
				return middle;
			} else if (target.compareTo(array[middle]) < 0) {
				return index(array, target, start, middle - 1);
			} else {
				return index(array, target, middle + 1, end);
			}
		}
	}

}
