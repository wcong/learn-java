package org.wcong.test.algorithm.leetcode.array;

/**
 * There are two sorted arrays nums1 and nums2 of size m and n respectively.
 * Find the median of the two sorted arrays.
 * The overall run time complexity should be O(log (m+n)).
 * ......*.......
 * .....*......
 *
 * @author wcong<wc19920415@gmail.com>
 * @since 11/05/2017
 */
public class MedianOfTwoSortedArray {
	public double findMedianSortedArrays(int[] nums1, int[] nums2) {
		boolean isOdd = ((nums1.length + nums2.length) & 1) == 1;
		int middle = (nums1.length + nums2.length) >> 1;
		int nums1Start = 0;
		int nums1End = nums1.length - 1;
		int nums2Start = 0;
		int nums2End = nums2.length - 1;
		while (true) {
			int nums1Middle = nums1Start + (nums1End - nums1Start) / 2;
			int nums2Middle = nums2Start + (nums2End - nums2Start) / 2;
			int currentPosition = nums1Middle + nums2Middle;
			if (nums1[nums1Middle] == nums2[nums2Middle]) {
				if (currentPosition == middle) {
					break;
				} else if (currentPosition < middle) {
					nums1Start = nums1Middle;
					nums2Start = nums2Middle;
				} else {
					nums1End = nums1Middle;
					nums2End = nums2Middle;
				}
			} else if (nums1[nums1Middle] < nums2[nums2Middle]) {
				nums2End = nums2Middle;
				nums1Start = nums1Middle;
			} else {
				nums1End = nums1Middle;
				nums2Start = nums2Middle;
			}
			if (nums1End == nums1Start && nums2End == nums2Start) {
				break;
			}
		}
		if (isOdd) {
			return nums1[nums1End] > nums2[nums2End] ? nums1[nums1End] : nums2[nums2End];
		} else {
			return ((double) (nums1[nums1End] + nums2[nums2End])) / 2;
		}
	}
}
