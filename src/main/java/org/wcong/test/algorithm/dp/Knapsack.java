package org.wcong.test.algorithm.dp;

/**
 * give n items has weight,w1,w2....,wn and values v1,v2....vn,
 * we have a bag contains m weight
 * find the largest value for the bag
 *
 * @author wcong<wc19920415@gmail.com>
 * @since 2017/4/9
 */
public class Knapsack {

	public static void main(String[] args) {

	}

	public static int maxValue(int[] weight, int[] values, int bag) {
		int[] result = new int[bag + 1];
		result[0] = 0;
		for (int i = 1; i <= bag; i++) {
			int max = Integer.MIN_VALUE;
			for (int j = 0; j < weight.length; j++) {
				if (i < weight[j]) {
					break;
				}
				max = Math.max(values[j] + result[i - weight[j]], max);
			}
			result[i] = max;
		}
		return result[bag];
	}

}
