package org.wcong.test.algorithm.dp;

/**
 * give two string a,b find the longest common sub sequence
 * Created by wcong on 2017/3/31.
 */
public class LongestCommonSubSequence {

    public static void main(String[] args) {

    }

    public static int longest(String a, String b) {
        int[][] result = new int[a.length() + 1][b.length() + 1];
        if (a.charAt(0) == b.charAt(0)) {
            result[1][1] = 1;
        }
        for (int i = 0; i < b.length(); i++) {
            if (a.charAt(0) == b.charAt(i)) {
                result[1][i + 1] = 1;
            }
        }
        for (int i = 1; i < a.length(); i++) {
            for (int j = 0; j < b.length(); j++) {
                if (a.charAt(i) == b.charAt(j)) {
                    result[i][j] = result[i - 1][j] + 1;
                }
            }
        }
        int max = 0;
        for (int i = 0; i < a.length(); i++) {
            for (int j = 0; j < b.length(); j++) {
                if (result[i][j] > max) {
                    max = result[i][j];
                }
            }
        }
        return max;
    }

}
