package org.wcong.test.algorithm.leetcode.string;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * You are given a string, s, and a list of words, words,
 * that are all of the same length.
 * Find all starting indices of substring(s) in s
 * that is a concatenation of each word in words
 * exactly once and without any intervening characters.
 * For example, given:
 * s: "barfoothefoobarman"
 * words: ["foo", "bar"]
 * You should return the indices: [0,9].
 * (order does not matter).
 *
 * @author wcong<wc19920415@gmail.com>
 * @since 17/05/2017
 */
public class SubstringWithConcatenationAllWords {

	public List<Integer> findSubstringBruteForce(String s, String[] words) {
		List<Integer> indices = new LinkedList<>();
		if (words == null || words.length == 0) {
			return indices;
		}
		int len = words[0].length();
		Map<String, Integer> map = new HashMap<>();
		for (String word : words) {
			if (map.containsKey(word)) {
				map.put(word, map.get(word) + 1);
			} else {
				map.put(word, 1);
			}
		}
		for (int index = 0; index < s.length(); index++) {
			if (isConcatenation(s, index, new HashMap<>(map), len)) {
				indices.add(index);
			}
		}
		return indices;
	}

	private boolean isConcatenation(String s, int index, Map<String, Integer> map, int len) {
		if (map.isEmpty()) {
			return true;
		}
		if (index + len > s.length()) {
			return false;
		}
		String nextWord = s.substring(index, index + len);
		Integer num = map.get(nextWord);
		if (num == null) {
			return false;
		}
		if (num == 1) {
			map.remove(nextWord);
		} else {
			map.put(nextWord, num - 1);
		}
		return isConcatenation(s, index + len, map, len);
	}

}
