package org.wcong.test.leetcode.string;

import org.junit.Assert;
import org.junit.Test;
import org.wcong.test.algorithm.leetcode.array.ReversePairs;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 04/05/2017
 */
public class ReversePiarsTest {

	@Test
	public void test() {
		int[] nums = new int[] { 1, 3, 4, 6, 8, 2 };
		int[] maxInt = new int[] { 2147483647, 2147483647, 2147483647, 2147483647, 2147483647, 2147483647 };
		ReversePairs reversePairs = new ReversePairs();
		Assert.assertTrue(reversePairs.reversePairsBruteForce(nums) == 2);
		Assert.assertTrue(reversePairs.reversePairsDivideAndConquer(nums) == 2);
		Assert.assertTrue(reversePairs.reversePairsDivideAndConquer(maxInt) == 0);
		Assert.assertTrue(reversePairs.reversePairsLiner(new int[] { 1, 3, 4, 6, 8, 2 }) == 2);
		Assert.assertTrue(reversePairs.reversePairsLiner(maxInt) == 0);
	}

}
