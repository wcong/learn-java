package org.wcong.test.algorithm.leetcode.string;

/**
 * Given a string S
 * you are allowed to convert it to a palindrome by adding characters in front of it.
 * Find and return the shortest palindrome you can find by performing this transformation.
 * For example:
 * Given "aacecaaa", return "aaacecaaa".
 * Given "abcd", return "dcbabcd".
 *
 * @author wcong<wc19920415@gmail.com>
 * @since 07/06/2017
 */
public class ShortestPalindrome {

    public String shortestPalindromeBruteForce(String s) {
        StringBuilder insert = new StringBuilder();
        int end = s.length() - 1;
        while (end > 0) {
            if (isPalindrome(s, 0, end)) {
                break;
            } else {
                insert.append(s.charAt(end));
                end -= 1;
            }
        }
        return insert + s;
    }

    private boolean isPalindrome(String s, int start, int end) {
        while (start <= end) {
            if (s.charAt(start) == s.charAt(end)) {
                start += 1;
                end -= 1;
            } else {
                return false;
            }
        }
        return true;
    }
}
