package org.wcong.test.algorithm.leetcode.string;

/**
 * Given a string s,
 * partition s such that every substring of the partition is a palindrome.
 * Return the minimum cuts needed for a palindrome partitioning of s.
 * For example, given s = "aab",
 * Return 1 since the palindrome partitioning ["aa","b"] could be produced using 1 cut.
 *
 * @author wcong<wc19920415@gmail.com>
 * @since 07/06/2017
 */
public class PalindromePartitioning2 {

    boolean[][] palindromeMatrix;

    public int minCutDp(String s) {
        int[] minCut = new int[s.length()];
        palindromeMatrix = new boolean[s.length()][s.length()];
        minCut[0] = 0;
        for (int i = 1; i < s.length(); i++) {
            if (isPalindrome(s, 0, i)) {
                minCut[i] = 0;
            } else {
                int minCutNum = minCut[i - 1] + 1;
                for (int j = 1; j < i; j++) {
                    if (isPalindrome(s, j, i)) {
                        int cutNum = minCut[j - 1] + 1;
                        if (cutNum < minCutNum) {
                            minCutNum = cutNum;
                        }
                    }
                }
                minCut[i] = minCutNum;
            }
        }
        return minCut[s.length() - 1];
    }

    private boolean isPalindrome(String s, int start, int end) {
        if (s.charAt(start) == s.charAt(end) && (start + 1 >= end - 1 || palindromeMatrix[start + 1][end - 1])) {
            palindromeMatrix[start][end] = true;
            return true;
        } else {
            return false;
        }
    }
}
