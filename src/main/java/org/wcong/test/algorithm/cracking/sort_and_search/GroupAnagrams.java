package org.wcong.test.algorithm.cracking.sort_and_search;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * write a method to sort an array of strings so that all the anagrams are next to each other
 *
 * @author wcong<wc19920415@gmail.com>
 * @since 19/04/2017
 */
public class GroupAnagrams {

	public static void main(String[] args) {

	}

	public static String[] groupAnagrams(String[] array) {
		Map<String, List<String>> map = new HashMap<>();
		for (String solo : array) {
			char[] charArray = solo.toCharArray();
			Arrays.sort(charArray);
			String sortedArray = new String(charArray);
			List<String> stringList = map.get(sortedArray);
			if (stringList == null) {
				stringList = new LinkedList<>();
				map.put(sortedArray, stringList);
			}
			stringList.add(solo);
		}
		String[] anagrams = new String[array.length];
		int index = 0;
		for (List<String> stringList : map.values()) {
			for (String solo : stringList) {
				anagrams[index] = solo;
				index += 1;
			}
		}
		return anagrams;
	}

}
