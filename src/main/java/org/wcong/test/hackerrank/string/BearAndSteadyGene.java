package org.wcong.test.hackerrank.string;

import java.util.HashMap;
import java.util.Map;

/**
 * given a string consist of A,C,T,G
 * if each of it appear 1/4 time the length it's steady
 * give a string of length n divisible of 4;
 * replace a minimum substring for it make it steady
 * <p/>
 * one loop get all num if
 *
 * @author wcong<wc19920415@gmail.com>
 * @since 03/05/2017
 */
public class BearAndSteadyGene {

	public int minimumReplace(String s) {
		int expectNum = s.length() / 4;
		char[] charArray = new char[] { 'A', 'C', 'T', 'G' };
		Map<Character, Integer> characterIntegerMap = new HashMap<>();
		for (char soloChar : charArray) {
			characterIntegerMap.put(soloChar, 0);
		}
		for (int index = 0; index < s.length(); index++) {
			characterIntegerMap.put(s.charAt(index), characterIntegerMap.get(s.charAt(index)) + 1);
		}
		for (char soloChar : charArray) {
			int count = characterIntegerMap.get(soloChar);
			if (count <= expectNum) {
				characterIntegerMap.remove(soloChar);
			} else {
				characterIntegerMap.put(soloChar, count - expectNum);
			}
		}
		if (characterIntegerMap.isEmpty()) {
			return 0;
		}
		Map<Character, Integer> encounterMap = new HashMap<>();
		for (char soloChar : characterIntegerMap.keySet()) {
			encounterMap.put(soloChar, 0);
		}

		int start = 0;
		int index = 0;
		int minSub = Integer.MAX_VALUE;
		while (!characterIntegerMap.containsKey(s.charAt(index))) {
			index += 1;
			start = index;
		}
		while (index < s.length()) {
			if (characterIntegerMap.containsKey(s.charAt(index))) {
				encounterMap.put(s.charAt(index), encounterMap.get(s.charAt(index)) + 1);
			}
			int compare = compare(characterIntegerMap, encounterMap);
			if (compare == 0) {
				int sub = index - start+1;
				if (sub < minSub) {
					minSub = sub;
				}
			} else if (compare > 0) {
				while (start < index) {
					while (!characterIntegerMap.containsKey(s.charAt(index))) {
						start += 1;
					}
					encounterMap.put(s.charAt(index), encounterMap.get(s.charAt(index)) - 1);
					if (compare(characterIntegerMap, encounterMap) < 0) {
						break;
					}
					start+=1;
				}
			}
			index += 1;
		}
		return minSub;
	}

	private int compare(Map<Character, Integer> characterIntegerMap, Map<Character, Integer> encounterMap) {
		boolean isEqual = true;
		for (Map.Entry<Character, Integer> characterIntegerEntry : characterIntegerMap.entrySet()) {
			int encounter = encounterMap.get(characterIntegerEntry.getKey());
			if (encounter > characterIntegerEntry.getValue()) {
				return 1;
			} else if (encounter < characterIntegerEntry.getValue()) {
				isEqual = false;
			}
		}
		return isEqual ? 0 : -1;
	}

}
