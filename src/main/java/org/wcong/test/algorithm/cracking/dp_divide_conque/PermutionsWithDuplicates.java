package org.wcong.test.algorithm.cracking.dp_divide_conque;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * write a method to compute all permutations of a string are not necessarily unique
 * but not duplicates
 *
 * @author wcong<wc19920415@gmail.com>
 * @since 19/04/2017
 */
public class PermutionsWithDuplicates {

	static class Summarise {
		List<Character> singles = new ArrayList<>();

		List<Duplicate> duplicate = new ArrayList<>();
	}

	static class Duplicate {
		Character character;

		int count;

		public Duplicate(Character character, int count) {
			this.character = character;
			this.count = count;
		}
	}

	public List<String> duplicates(String a) {
		Map<Character, Integer> duplicate = new HashMap<>();
		for (Character character : a.toCharArray()) {
			Integer count = duplicate.get(character);
			if (count == null) {
				duplicate.put(character, 1);
			} else {
				duplicate.put(character, count + 1);
			}
		}
		List<String> result = new LinkedList<>();
		makePermutations(duplicate, "", a.length(), result);
		return result;
	}

	private void makePermutations(Map<Character, Integer> map, String prefix, int remain, List<String> result) {
		if (remain == 0) {
			result.add(prefix);
			return;
		}
		for (Map.Entry<Character, Integer> entry : map.entrySet()) {
			int count = entry.getValue();
			map.put(entry.getKey(), count - 1);
			makePermutations(map, prefix + entry.getKey(), remain - 1, result);
			map.put(entry.getKey(), count);
		}
	}

}
