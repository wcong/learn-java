package org.wcong.test.leetcode;

import org.junit.Assert;
import org.junit.Test;
import org.wcong.test.algorithm.leetcode.numbers.NumberToWords;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 16/05/2017
 */
public class NumberToWordsTest {

	@Test
	public void testNumberToWords() {
		NumberToWords numberToWords = new NumberToWords();
		Assert.assertTrue(numberToWords.numberToWords(123).equals("One Hundred Twenty Three"));
		Assert.assertTrue(numberToWords.numberToWords(12345).equals("Twelve Thousand Three Hundred Forty Five"));
		Assert.assertTrue(numberToWords.numberToWords(1234567)
				.equals("One Million Two Hundred Thirty Four Thousand Five Hundred Sixty Seven"));
	}

}
