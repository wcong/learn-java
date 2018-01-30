package org.wcong.test.leetcode.string;

import org.junit.Assert;
import org.junit.Test;
import org.wcong.test.algorithm.leetcode.string.ShortestPalindrome;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 07/06/2017
 */
public class ShortestPalindromeTest {

    @Test
    public void testShortestPalindrome() {
        ShortestPalindrome solution = new ShortestPalindrome();
        Assert.assertTrue(solution.shortestPalindromeBruteForce("aacecaaa").equals("aaacecaaa"));
        Assert.assertTrue(solution.shortestPalindromeBruteForce("abcd").equals("dcbabcd"));
    }

}
