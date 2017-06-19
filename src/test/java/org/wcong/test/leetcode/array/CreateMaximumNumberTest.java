package org.wcong.test.leetcode.array;

import org.junit.Assert;
import org.junit.Test;
import org.wcong.test.algorithm.leetcode.array.CreateMaximumNumber;

import java.util.Arrays;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 13/06/2017
 */
public class CreateMaximumNumberTest {

    @Test
    public void testMaxNumber() {

        CreateMaximumNumber solution = new CreateMaximumNumber();
        int[] e3nums1 = new int[]{3, 9};
        int[] e3nums2 = new int[]{8, 9};
        int[] e3result = new int[]{9, 8, 9};
        Assert.assertTrue(Arrays.equals(solution.maxNumberGreed(e3nums1, e3nums2, 3), e3result));
        int[] e1nums1 = new int[]{3, 4, 6, 5};
        int[] e1nums2 = new int[]{9, 1, 2, 5, 8, 3};
        int[] e1result = new int[]{9, 8, 6, 5, 3};
        Assert.assertTrue(Arrays.equals(solution.maxNumberGreed(e1nums1, e1nums2, 5), e1result));
        int[] e2nums1 = new int[]{6, 7};
        int[] e2nums2 = new int[]{6, 0, 4};
        int[] e2result = new int[]{6, 7, 6, 0, 4};
        Assert.assertTrue(Arrays.equals(solution.maxNumberGreed(e2nums1, e2nums2, 5), e2result));
        try {
            throw new NoClassDefFoundError();
        } catch (Error e) {
            e.printStackTrace();
        }
    }

}
