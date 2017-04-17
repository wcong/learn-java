package org.wcong.test.algorithm.jzoffer.array;

import org.springframework.util.Assert;

/**
 * test for array
 * give a array find the max sum of sub array
 * for example 1,-2,3,10,-4,7,2,-5 max 18
 * Created by hzwangcong on 2017/3/28.
 */
public class MaxSumSubArray {


    public static void main(String[] args) {
        Assert.isTrue(maxSum(new int[]{1, -2, 3, 10, -4, 7, 2, -5}) == 18);
    }

    public static int maxSum(int[] array) {
        int maxSum = array[0];
        int currentSum = array[0];
        for (int i = 1; i < array.length; i++) {
            if (currentSum <= 0) {
                currentSum = array[i];
            }else{
                currentSum+=array[i];
            }
            if (currentSum > maxSum) {
                maxSum = currentSum;
            }
        }
        return maxSum;
    }

}
