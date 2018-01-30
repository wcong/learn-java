package org.wcong.test.leetcode.string;

import org.junit.Assert;
import org.junit.Test;
import org.wcong.test.algorithm.leetcode.string.InterleavingString;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 15/06/2017
 */
public class InterleavingStringTest {

    @Test
    public void testIsInterleave() {
        String s1 = "aabcc";
        String s2 = "dbbca";
        String s3 = "aadbbcbcac";
        String s4 = "aadbbbaccc";
        InterleavingString solution = new InterleavingString();
        Assert.assertTrue(solution.isInterleaveBruteForce(s1, s2, s3));
        Assert.assertTrue(!solution.isInterleaveBruteForce(s1, s2, s4));
    }

}
