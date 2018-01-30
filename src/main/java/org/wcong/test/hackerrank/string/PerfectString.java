package org.wcong.test.hackerrank.string;

import java.util.ArrayList;
import java.util.List;

/**
 * give a string consisting of a,b,c,d
 * perfect string
 * 1 num of a equal num of b
 * 2 num of c equal num of d
 * subsequent of string
 *
 * @author wcong<wc19920415@gmail.com>
 * @since 03/05/2017
 */
public class PerfectString {

	public long perfectNum(String originString) {
		int[] count = new int[4];
		for (char soloChar : originString.toCharArray()) {
			count[soloChar - 'a'] += 1;
		}
		long perfectNum = 0;
		long abNum = 0;
		int minAb = count[0] < count[1] ? count[0] : count[1];
		for (int i = 1; i <= minAb; i++) {
			abNum += combination(count[0], i) * combination(count[1], i);
		}
		perfectNum += abNum % 1000000007;
		int minCD = count[2] < count[3] ? count[2] : count[3];
		long cdNum = 0;
		for (int i = 1; i <= minCD; i++) {
			cdNum += combination(count[2], i) * combination(count[3], i);
		}
		perfectNum += cdNum % 1000000007;
		perfectNum = perfectNum % 1000000007;
		perfectNum += (abNum * cdNum) % 1000000007;
		perfectNum = perfectNum % 1000000007;
		return perfectNum;
	}

	static List<Integer> factorialList = new ArrayList<>();

	static {
		factorialList.add(0);
		factorialList.add(1);
	}

	private int combination(int total, int num) {
		if (total == 0 || num == 0) {
			return 0;
		}
		if (total == num) {
			return 1;
		}
		if (factorialList.size() - 1 < total) {
			for (int currentNum = factorialList.size(); currentNum <= total; currentNum++) {
				factorialList.add(factorialList.get(currentNum - 1) * currentNum);
			}
		}
		return factorialList.get(total) / factorialList.get(total - num) / factorialList.get(num);
	}

}
