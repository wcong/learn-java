package org.wcong.test.algorithm.jzoffer;

import org.eclipse.core.runtime.Assert;

/**
 * test for number and divide and conquer
 * give a number n count all the 1 appear from 1 to n;
 * for example n=11 count = 3
 * Created by wcong on 2017/3/28.
 */
public class NumberOf1BeforeN {

    public static void main(String[] args) {
        Assert.isTrue(count1(11) == 4);
        Assert.isTrue(count1(21) == 13);
    }

    // for example 11 have 4 : 1 10 11 , 21 have 13
    public static int count1(int n) {
        if (n < 1) {
            return 0;
        }
        if (n < 10) {
            return 1;
        }
        int current = n;
        int exponent = 0;
        int maxTen = 1;
        while (current >= 10) {
            maxTen *= 10;
            exponent += 1;
            current /= 10;
        }
        return count1(n, maxTen, exponent);
    }

    private static int count1(int n, int maxTen, int exponent) {
        int count = 0;
        if (n / maxTen == 1) {
            count += n - maxTen + 1;
        } else {
            count += maxTen;
        }
        int lowExponent = countExponent(exponent);
        while (n > maxTen) {
            count += lowExponent;
            n -= maxTen;
        }
        count += count1(n);
        return count;
    }

    private static int countExponent(int exponent) {
        if (exponent == 0) {
            return 0;
        }
        if (exponent == 1) {
            return 1;
        }
        return 10 * countExponent(exponent - 1) + (int) Math.pow(10, exponent - 1);
    }

}
