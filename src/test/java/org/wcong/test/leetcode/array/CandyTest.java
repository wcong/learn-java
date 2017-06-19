package org.wcong.test.leetcode.array;

import org.junit.Assert;
import org.junit.Test;
import org.wcong.test.algorithm.leetcode.array.Candy;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 19/06/2017
 */
public class CandyTest {

    @Test
    public void testCandies() {
        Candy candy = new Candy();
        Assert.assertTrue(candy.candy(new int[]{11111}) == 1);
        Assert.assertTrue(candy.candy(new int[]{1, 2, 2}) == 4);
        Assert.assertTrue(candy.candy(new int[]{5, 4, 3, 2, 1, 0}) == 21);
        Assert.assertTrue(candy.candy(new int[]{5, 4, 4, 2, 1, 0}) == 13);
        Assert.assertTrue(candy.candy(new int[]{4, 2, 3, 4, 1}) == 9);
    }

}
