package org.wcong.test.algorithm.cracking.dp_divide_conque;

import java.util.LinkedList;
import java.util.List;

/**
 * implement an algorithm to print all valid combinations of n pairs  of parenthesis
 *
 * @author wcong<wc19920415@gmail.com>
 * @since 19/04/2017
 */
public class Parenthesis {

	public List<String> parenthesis(int n) {
		String[] combinations = new String[n + 1];
		combinations[0] = "";
		for (int i = 1; i <= n; i++) {
			StringBuilder stringBuilder = new StringBuilder(i * 2);
			for (int j = 0; j < i; j++) {
				stringBuilder.append("(");
			}
			for (int j = 0; j < i; j++) {
				stringBuilder.append(")");
			}
			combinations[i] = stringBuilder.toString();
		}
		List<String> result = new LinkedList<>();
		parenthesisCombination(combinations, "", n, result);
		return result;
	}

	private void parenthesisCombination(String[] combinations, String prefix, int remain, List<String> result) {
		if (remain == 0) {
			result.add(prefix);
		}
		for (int i = 1; i <= remain; i++) {
			parenthesisCombination(combinations, prefix + combinations[i], remain - 1, result);
		}
	}

}
