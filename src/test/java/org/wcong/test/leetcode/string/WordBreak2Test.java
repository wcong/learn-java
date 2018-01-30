package org.wcong.test.leetcode.string;

import org.junit.Assert;
import org.junit.Test;
import org.wcong.test.algorithm.leetcode.string.WordBreak2;

import java.util.Arrays;
import java.util.List;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 24/05/2017
 */
public class WordBreak2Test {

	@Test
	public void testWordBreak2() {
		List<String> dict = Arrays.asList("cat", "cats", "and", "sand", "dog");
		WordBreak2 wordBreak2 = new WordBreak2();
		List<String> result = wordBreak2.wordBreakBruteForce("catsanddog", dict);
		Assert.assertTrue(result.get(0).equals("cat sand dog"));
		Assert.assertTrue(result.get(1).equals("cats and dog"));
		List<String> resultDp = wordBreak2.wordBreakDp("catsanddog", dict);
		Assert.assertTrue(resultDp.get(1).equals("cat sand dog"));
		Assert.assertTrue(resultDp.get(0).equals("cats and dog"));
		List<String> dictNone = Arrays.asList("aaaa", "aa");
		List<String> resultDpNone = wordBreak2.wordBreakDp("aaaaaaa", dictNone);
		Assert.assertTrue(resultDpNone.isEmpty());
	}

}
