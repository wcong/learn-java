package org.wcong.test.leetcode.string;

import org.junit.Assert;
import org.junit.Test;
import org.wcong.test.algorithm.leetcode.string.LongestValidParentheses;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 03/06/2017
 */
public class LongestValidParenthesesTest {

    @Test
    public void testLongestValidParentheses() {
        String s1 = "(()";
        String s2 = ")()())";
        String s3 = ")()()))()()()(())";
        String s4 = "()(()";
        String s5 = "(()(((()";
        LongestValidParentheses solution = new LongestValidParentheses();
        Assert.assertTrue(solution.longestValidParenthesesBruteForce(s1) == 2);
        Assert.assertTrue(solution.longestValidParenthesesBruteForce(s2) == 4);
        Assert.assertTrue(solution.longestValidParenthesesBruteForce(s3) == 10);
        Assert.assertTrue(solution.longestValidParenthesesGreed(s1) == 2);
        Assert.assertTrue(solution.longestValidParenthesesGreed(s2) == 4);
        Assert.assertTrue(solution.longestValidParenthesesGreed(s3) == 10);
        Assert.assertTrue(solution.longestValidParenthesesGreed(s4) == 2);
        Assert.assertTrue(solution.longestValidParenthesesGreed(s5) == 2);
    }

}
