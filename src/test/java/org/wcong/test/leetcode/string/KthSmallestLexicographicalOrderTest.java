package org.wcong.test.leetcode.string;

import org.junit.Assert;
import org.junit.Test;
import org.wcong.test.algorithm.leetcode.string.KthSmallestLexicographicalOrder;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 06/06/2017
 */
public class KthSmallestLexicographicalOrderTest {


    @Test
    public void testFindKthNumber() {
        KthSmallestLexicographicalOrder solution = new KthSmallestLexicographicalOrder();
        Assert.assertTrue(solution.findKthNumberBruteForce(13, 2) == 10);
        Assert.assertTrue(solution.findKthNumberGreed(13, 2) == 10);
        Assert.assertTrue(solution.findKthNumberGreed(132, 4) == 101);
        Assert.assertTrue(solution.findKthNumberGreed(10, 3) == 2);
        Assert.assertTrue(solution.findKthNumberGreed(1000, 1000) == 9999);
    }

}
