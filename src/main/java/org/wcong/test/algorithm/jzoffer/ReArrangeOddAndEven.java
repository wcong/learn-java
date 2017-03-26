package org.wcong.test.algorithm.jzoffer;

import org.springframework.util.Assert;

import java.util.Arrays;

/**
 * test for array and odd and even and two pointers
 * give an array of integer rearrange odd number to left and even number to left
 *
 * @author wcong<wc19920415@gmail.com>
 * @since 2017/3/26
 */
public class ReArrangeOddAndEven {

	public static void main(String[] args) {
		Assert.isTrue(
				Arrays.equals(rearrange(new int[] { 1, 2, 3, 4, 5, 6, 7, 8 }), new int[] { 1, 7, 3, 5, 4, 6, 2, 8 }));
	}

	public static int[] rearrange(int[] array) {
		if (array == null || array.length == 0) {
			return array;
		}
		int leftEvenIndex = 0;
		int rightOddIndex = array.length - 1;
		while (leftEvenIndex < rightOddIndex) {
			if ((array[leftEvenIndex] & 1) != 0) {
				leftEvenIndex += 1;
				continue;
			}
			if ((array[rightOddIndex] & 1) == 0) {
				rightOddIndex -= 1;
				continue;
			}
			int temp = array[leftEvenIndex];
			array[leftEvenIndex] = array[rightOddIndex];
			array[rightOddIndex] = temp;
			leftEvenIndex += 1;
			rightOddIndex -= 1;
		}
		return array;
	}

}
