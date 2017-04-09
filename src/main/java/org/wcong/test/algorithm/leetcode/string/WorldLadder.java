package org.wcong.test.algorithm.leetcode.string;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 2017/4/3
 */
public class WorldLadder {

	public static void main(String[] args) {
		new WorldLadder().findLadders("hot", "dog",
				Arrays.asList(new String[] { "hot", "cog", "dog", "tot", "hog", "hop", "pot", "dot" }));
	}

	private int minLength = Integer.MAX_VALUE;

	public List<List<String>> findLadders(String beginWord, String endWord, List<String> wordList) {
		List<List<String>> resultList = new LinkedList<>();
		List<String> result = new LinkedList<>();
		result.add(beginWord);
		findLadders(beginWord, endWord, wordList, result, resultList);
		List<List<String>> minResult = new LinkedList<>();
		for (List<String> soloResult : resultList) {
			if (soloResult.size() == minLength) {
				minResult.add(soloResult);
			}
		}
		return minResult;
	}

	private void findLadders(String beginWord, String endWord, List<String> wordList, List<String> result,
			List<List<String>> resultList) {
		if (beginWord.equals(endWord)) {
			if (result.size() <= minLength) {
				resultList.add(result);
				minLength = result.size();
			}
			return;
		}
		if (resultList.size() > minLength) {
			return;
		}
		for (int i = 0; i < wordList.size(); i++) {
			if (isOneWordDiffrent(beginWord, wordList.get(i))) {
				List<String> leftWordList = new ArrayList();
				leftWordList.addAll(wordList);
				leftWordList.remove(i);
				List<String> leftResult = new ArrayList(result);
				leftResult.add(wordList.get(i));
				findLadders(wordList.get(i), endWord, leftWordList, leftResult, resultList);
			}
		}
	}

	private boolean isOneWordDiffrent(String word, String compare) {
		int different = 0;
		for (int i = 0; i < word.length(); i++) {
			if (word.charAt(i) != compare.charAt(i)) {
				different += 1;
			}
		}
		return different == 1;
	}
}
