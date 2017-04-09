package org.wcong.test.algorithm.dp;

/**
 * this is a row of n coins whose values are some positive integers c1,c2,....,cn
 * not necessarily distinct,the goal is to pick up the maximum amount of money subject
 * that no two coins adjacent in the initial row can be picked up.
 *
 * @author wcong<wc19920415@gmail.com>
 * @since 2017/4/9
 */
public class CoinRow {

	public static void main(String[] args) {

	}

	public static int maxCoinSum(int[] array) {
		int[] result = new int[array.length];
		result[0] = 0;
		result[1] = array[0];
		for (int i = 1; i < array.length; i++) {
			result[i + 1] = Math.max(result[i], result[i - 1] + array[i]);
		}
		return result[array.length];
	}

}
