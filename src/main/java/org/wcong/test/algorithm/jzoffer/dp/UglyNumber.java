package org.wcong.test.algorithm.jzoffer.dp;

import org.springframework.util.Assert;

/**
 * test for number
 * ugly number only contains 2,3,4 for multiply count target number of ugly number
 * Created by wcong on 2017/3/29.
 */
public class UglyNumber {

    public static void main(String[] args) {
        Assert.isTrue(nThUglyNumber(9) == 12);
        Assert.isTrue(nThUglyNumber(10) == 15);
    }

    public static int nThUglyNumber(int n) {
        int[] ugly = new int[n + 1];
        ugly[0] = 1;
        int index2 = 0;
        int index3 = 0;
        int index5 = 0;
        for (int i = 1; i <= n; i++) {
            int min = Math.min(Math.min(ugly[index2] * 2, ugly[index3] * 3), ugly[index5] * 5);
            ugly[i] = min;
            while (ugly[index2] * 2 <= min) {
                index2 += 1;
            }
            while (ugly[index3] * 3 <= min) {
                index3 += 1;
            }
            while (ugly[index5] * 5 <= min) {
                index5 += 1;
            }
        }
        return ugly[n];
    }

}
