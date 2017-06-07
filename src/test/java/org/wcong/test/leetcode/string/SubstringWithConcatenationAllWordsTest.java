package org.wcong.test.leetcode.string;

import org.junit.Assert;
import org.junit.Test;
import org.wcong.test.algorithm.leetcode.string.SubstringWithConcatenationAllWords;

import java.util.Arrays;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 17/05/2017
 */
public class SubstringWithConcatenationAllWordsTest {

	@Test
	public void testFindSubstring() {
		SubstringWithConcatenationAllWords test = new SubstringWithConcatenationAllWords();
		Assert.assertTrue(test.findSubstringBruteForce("barfoothefoobarman", new String[] { "foo", "bar" })
				.containsAll(Arrays.asList(0, 9)));

	}

}
