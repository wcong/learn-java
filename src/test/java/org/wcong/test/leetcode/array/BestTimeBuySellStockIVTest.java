package org.wcong.test.leetcode.array;

import org.junit.Assert;
import org.junit.Test;
import org.wcong.test.algorithm.leetcode.string.BestTimeBuySellStockIV;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 10/06/2017
 */
public class BestTimeBuySellStockIVTest {

    @Test
    public void testMaxProfit() {
        BestTimeBuySellStockIV solution = new BestTimeBuySellStockIV();
        int[] simple = new int[]{0, 1};
        int[] oneMax = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8};
        int[] transaction = new int[]{1, 2};
        int[] time = new int[]{48, 12, 60, 93, 97, 42, 25, 64, 17, 56, 85, 93, 9, 48, 52, 42, 58, 85, 81, 84, 69, 36, 1, 54, 23, 15, 72, 15, 11, 94};
        int[] border = new int[]{1, 4, 2};
        Assert.assertTrue(solution.maxProfitDp(2, simple) == 1);
        Assert.assertTrue(solution.maxProfitDp(9, oneMax) == 8);
        Assert.assertTrue(solution.maxProfitDp(1, transaction) == 1);
        Assert.assertTrue(solution.maxProfitDp(2, border) == 3);
    }


}
