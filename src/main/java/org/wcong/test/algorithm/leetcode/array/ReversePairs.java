package org.wcong.test.algorithm.leetcode.array;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Given an array nums
 * we call (i, j) an important reverse pair if i < j and nums[i] > 2*nums[j].
 * You need to return the number of important reverse pairs in the given array.
 *
 * @author wcong<wc19920415@gmail.com>
 * @since 04/05/2017
 */
public class ReversePairs {

	public int reversePairsBruteForce(int[] nums) {
		int pairs = 0;
		for (int index = 0; index < nums.length; index++) {
			for (int nextIndex = index + 1; nextIndex < nums.length; nextIndex++) {
				if (nums[index] > 2 * nums[nextIndex]) {
					pairs += 1;
				}
			}
		}
		return pairs;
	}

	public int reversePairsDivideAndConquer(int[] nums) {
		return reversePairsDivideAndConquer(nums, 0, nums.length - 1);
	}

	private int reversePairsDivideAndConquer(int[] nums, int start, int end) {
		if (start >= end) {
			return 0;
		}
		int pairs = 0;
		int middle = start + (end - start) / 2;
		pairs += reversePairsDivideAndConquer(nums, start, middle);
		pairs += reversePairsDivideAndConquer(nums, middle + 1, end);
		int leftIndex = start;
		for (int rightIndex = middle + 1; rightIndex <= end; rightIndex++) {
			long currentNum = nums[rightIndex];
			for (; leftIndex <= middle; leftIndex++) {
				if (2 * currentNum < nums[leftIndex]) {
					pairs += middle - leftIndex + 1;
					break;
				}
			}
		}

		Arrays.sort(nums, start, end + 1);
		return pairs;
	}

	public int reversePairsLiner(int[] nums) {
		List<Integer> sorted = new LinkedList<>();
		int pairs = 0;
		for (int soloNum : nums) {
			pairs += countAndInsert(sorted, soloNum);
		}
		return pairs;
	}

	private int countAndInsert(List<Integer> sorted, Integer num) {
		if (sorted.isEmpty()) {
			sorted.add(num);
			return 0;
		}
		int pairsIndex = 0;
		int insertIndex = 0;
		for (int integer : sorted) {
			if (integer < num) {
				insertIndex += 1;
			}
			if (integer > 2 * (long) num) {
				break;
			}
			pairsIndex += 1;
		}
		sorted.add(insertIndex, num);
		return sorted.size() - 1 - pairsIndex;
	}

}
