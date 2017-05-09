package org.wcong.test.algorithm.leetcode.string;

/**
 * A password is considered strong if below conditions are all met:
 * 1. It has at least 6 characters and at most 20 characters.
 * 2. It must contain at least one lowercase letter, at least one uppercase letter, and at least one digit.
 * 3. It must NOT contain three repeating characters in a row ("...aaa..." is weak, but "...aa...a..." is strong, assuming other conditions are met).
 * Write a function strongPasswordChecker(s), that takes a string s as input, and return the MINIMUM change required to make s a strong password. If s is already strong, return 0.
 * Insertion, deletion or replace of any one character are all considered as one change.
 *
 * @author wcong<wc19920415@gmail.com>
 * @since 07/05/2017
 */
public class StrongPassword {

	public int strongPasswordDP(String s) {
		int[][] distance = new int[s.length()][s.length()];

		return distance[s.length()][s.length()];
	}

}
