package org.wcong.test.algorithm.dp;

import org.eclipse.core.runtime.Assert;

/**
 * give a string find the longest palindrome sub sequence
 * Created by wcong on 2017/3/31.
 */
public class LongestPalindromeSubsequence {

    public static void main(String[] args) {
        Assert.isTrue(palindrome("cabdbae") == 5);
    }

    public static int palindrome(String a) {
        int[][] result = new int[a.length()][a.length()];
        for (int i = 0; i < a.length(); i++) {
            result[i][i] = 1;
        }
        for (int i = 1; i < a.length(); i++) {
            for (int j = 0; j < i; j++) {
                if (a.charAt(i) == a.charAt(j)) {
                    if (i - j - 1 == 1) {
                        result[i][j] = 3;
                    } else if (i - 1 > 0 && j + 1 < i - 1) {
                        if (result[i - 1][j + 1] > 0) {
                            result[i][j] = result[i - 1][j + 1] + 2;
                        }
                    } else {
                        result[i][j] = 1;
                    }
                }
            }
        }
        int max = 1;
        for (int i = 0; i < a.length(); i++) {
            for (int j = 0; j < a.length(); j++) {
                if (result[i][j] > max) {
                    max = result[i][j];
                }
            }
        }
        return max;
    }

}
