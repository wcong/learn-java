package org.wcong.test.algorithm.leetcode.string;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Given a non-empty string s and a dictionary wordDict containing a list of non-empty words,
 * add spaces in s to construct a sentence where each word is a valid dictionary word.
 * You may assume the dictionary does not contain duplicate words.
 * Return all such possible sentences.
 * For example, given
 * s = "catsanddog",
 * dict = ["cat", "cats", "and", "sand", "dog"].
 * A solution is ["cats and dog", "cat sand dog"].
 *
 * @author wcong<wc19920415@gmail.com>
 * @since 24/05/2017
 */
public class WordBreak2 {

	public List<String> wordBreakBruteForce(String s, List<String> wordDict) {
		Set<String> wordSet = new HashSet<>();
		for (String word : wordDict) {
			wordSet.add(word);
		}
		List<String> wordBreakList = new LinkedList<>();
		breakWord(s, wordSet, 0, "", wordBreakList);
		return wordBreakList;
	}

	public List<String> wordBreakDp(String s, List<String> wordDict) {
		Set<String> wordSet = new HashSet<>();
		int maxLength = 0;
		for (String word : wordDict) {
			wordSet.add(word);
			if (word.length() > maxLength) {
				maxLength = word.length();
			}
		}
		List<List<String>> breakWordList = new ArrayList<>(s.length());
		List<String> firstChar = new LinkedList<>();
		breakWordList.add(firstChar);

		Map<String,List<String>> map = new HashMap<>();
		if (wordSet.contains(s.substring(0, 1))) {
			firstChar.add(s.substring(0, 1));
		}
		map.put(s.substring(0, 1),firstChar);
		for (int index = 1; index < s.length(); index++) {
			List<String> currentList = new ArrayList<>();
			for (int last = index; last >= 0 && (index - last) < maxLength; last--) {
				String tempString = s.substring(last, index + 1);
				if (wordSet.contains(tempString)) {
					if (last == 0) {
						currentList.add(tempString);
					} else {
						List<String> lastBreakList = breakWordList.get(last - 1);
						for (String lastBreak : lastBreakList) {
							currentList.add(lastBreak + " " + tempString);
						}
					}
				}
			}
			breakWordList.add(currentList);
		}
		return breakWordList.get(s.length() - 1);
	}

	private void breakWord(String s, Set<String> wordSet, int index, String currentWord, List<String> wordBreakList) {
		if (index >= s.length()) {
			wordBreakList.add(currentWord);
			return;
		}
		for (int nextIndex = index; nextIndex < s.length(); nextIndex++) {
			String tempString = s.substring(index, nextIndex + 1);
			if (wordSet.contains(tempString)) {
				String temp = currentWord.length() == 0 ? tempString : currentWord + " " + tempString;
				breakWord(s, wordSet, nextIndex + 1, temp, wordBreakList);
			}
		}
	}

}
