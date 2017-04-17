package org.wcong.test.algorithm.cracking.array_and_string;

import java.util.HashMap;
import java.util.Map;

/**
 * implement an algorithm to determine if a string has all unique characters
 * what if you cannot use additional data structures
 * <p/>
 * 1. none additional space and cannot change original string O(n2)
 * 2. none additional space and can change original string O(n log n)
 * 3. additional space hashMap and O(n)
 *
 * @author wcong<wc19920415@gmail.com>
 * @since 17/04/2017
 */
public class IsUnique {

	public static void main(String[] args) {

	}

	public boolean isUnique(String string) {
		Map<Character, Integer> count = new HashMap<>();
		for (char inner : string.toCharArray()) {
			if (count.containsKey(inner)) {
				return false;
			}
			count.put(inner, 1);
		}
		return true;
	}

}
