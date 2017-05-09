package org.wcong.test.leetcode.array;

import org.junit.Assert;
import org.junit.Test;
import org.wcong.test.algorithm.leetcode.array.NumbersOfIslands;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 09/05/2017
 */
public class NUmbersOfIslandsTest {

	@Test
	public void testNumIslandsBruteForce() {
		NumbersOfIslands numbersOfIslands = new NumbersOfIslands();
		char[][] chars1 = new char[][] { "11110".toCharArray(), "11010".toCharArray(), "11000".toCharArray(),
				"00000".toCharArray() };
		Assert.assertTrue(numbersOfIslands.numIslandsBruteForce(chars1) == 1);
		char[][] chars2 = new char[][] { "11000".toCharArray(), "11000".toCharArray(), "00100".toCharArray(),
				"00011".toCharArray() };
		Assert.assertTrue(numbersOfIslands.numIslandsBruteForce(chars2) == 3);
	}

}
