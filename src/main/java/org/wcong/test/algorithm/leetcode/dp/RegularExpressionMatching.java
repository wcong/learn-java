package org.wcong.test.algorithm.leetcode.dp;

/**
 * test for dynamic programming and string matching
 * <p>
 * Implement regular expression matching with support for '.' and '*'.
 * '.' Matches any single character.
 * '*' Matches zero or more of the preceding element.
 * The matching should cover the entire input string (not partial).
 * The function prototype should be:
 * bool isMatch(const char *s, const char *p)
 * Created by wcong on 2017/3/27.
 */
public class RegularExpressionMatching {

    public static void main(String[] args) {

    }

    public static boolean isMatch(String s, String pattern) {
        if (s == null || s.isEmpty() || pattern == null || pattern.isEmpty()) {
            return false;
        }
        boolean[][] result = new boolean[s.length() + 1][pattern.length() + 1];
        result[0][0] = true;
        for (int i = 0; i < pattern.length(); i++) {
            if (pattern.charAt(i) == '*' && result[0][i - 1]) {
                result[0][i + 1] = true;
            }
        }
        for (int i = 0; i < s.length(); i++) {
            for (int j = 0; j < pattern.length(); j++) {
                if (pattern.charAt(j) == '.') {
                    result[i + 1][j + 1] = result[i][j];
                } else if (pattern.charAt(j) != '*') {
                    result[i + 1][j + 1] = result[i][j] && (s.charAt(i) == pattern.charAt(j));
                } else {
                    if (pattern.charAt(j - 1) != s.charAt(i) && pattern.charAt(j - 1) != '.') {
                        result[i + 1][j + 1] = result[i + 1][j - 1];
                    } else {
                        result[i + 1][j + 1] = (result[i + 1][j] || result[i][j + 1] || result[i + 1][j - 1]);
                    }
                }
            }
        }
        return result[s.length()][pattern.length()];
    }

}
