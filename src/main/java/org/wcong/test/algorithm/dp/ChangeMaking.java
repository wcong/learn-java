package org.wcong.test.algorithm.dp;

/**
 * give change for amount n using the minimum number of coin of denomination d1<d2<d3....<dn
 *
 * @author wcong<wc19920415@gmail.com>
 * @since 2017/4/9
 */
public class ChangeMaking {

	public static void main(String[] args) {

	}

	public static int minimumNums(int[] array, int amount) {
		int[] result = new int[amount + 1];
		result[0] = 0;
		for (int i = 1; i <= amount; i++) {
			int min = i;
			for (int j = 0; j < array.length; j++) {
				if (array[j] > i) {
					break;
				}
				min = Math.min(min, result[i - array[j]] + 1);
			}
			result[i] = min;
		}
		return result[amount];
	}

}
