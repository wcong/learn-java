package org.wcong.test.algorithm.jzoffer;

import org.springframework.util.Assert;

import java.util.Arrays;

/**
 * test for array and tow pointer
 * give a sorted array return two num sum to a target
 * for example 1,2,4,7,11,15 target 12
 * Created by wcong on 2017/3/28.
 */
public class SortedArraySumTarget {

    public static void main(String[] args) {
        Assert.isTrue(Arrays.equals(sumTarget(new int[]{1, 2, 4, 7, 11, 15}, 12), new int[]{0, 4}));
        Assert.isTrue(Arrays.equals(sumTarget(new int[]{1, 2, 4, 7, 11, 15}, 11), new int[]{2, 3}));
    }

    public static int[] sumTarget(int[] array, int target) {
        if (array == null || array.length < 2) {
            return null;
        }
        int start = 0;
        int end = array.length - 1;
        int sum;
        while (start < end) {
            sum = array[start] + array[end];
            if (sum == target) {
                return new int[]{start, end};
            } else if (sum > target) {
                end -= 1;
            } else {
                start += 1;
            }
        }
        return null;
    }

}
