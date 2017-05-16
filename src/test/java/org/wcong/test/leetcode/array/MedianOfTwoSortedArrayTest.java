package org.wcong.test.leetcode.array;

import org.junit.Assert;
import org.junit.Test;
import org.wcong.test.algorithm.leetcode.array.MedianOfTwoSortedArray;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 11/05/2017
 */
public class MedianOfTwoSortedArrayTest {

	@Test
	public void testFindMedianSortedArrays() {
		MedianOfTwoSortedArray medianOfTwoSortedArray = new MedianOfTwoSortedArray();
		Assert.assertTrue(medianOfTwoSortedArray.findMedianSortedArrays(new int[] { 1, 3 }, new int[] { 2 }) == 2);
		Assert.assertTrue(medianOfTwoSortedArray.findMedianSortedArrays(new int[] { 1, 3 }, new int[] { 3, 4 }) == 2.5);
	}

}
