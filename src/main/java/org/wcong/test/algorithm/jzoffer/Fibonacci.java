package org.wcong.test.algorithm.jzoffer;

import org.eclipse.core.runtime.Assert;

/**
 * test for recursive and numbers
 * Created by wcong on 2017/3/24.
 */
public class Fibonacci {

    public static void main(String[] args) {
        Assert.isTrue(fibonacci(2) == 2);
        Assert.isTrue(fibonacci(4) == 5);
        Assert.isTrue(fibonacci(6) == 13);
    }

    static int fibonacci(int n) {
        if (n < 2) {
            return 1;
        }
        int last = 1;
        int lastOfLast = 1;
        for (int i = 2; i < n; i++) {
            int now = last + lastOfLast;
            lastOfLast = last;
            last = now;
        }
        return last + lastOfLast;
    }
}
