package org.wcong.test.algorithm.leetcode.string;

/**
 * Given s1, s2, s3
 * find whether s3 is formed by the interleaving of s1 and s2.
 * For example,
 * Given:
 * s1 = "aabcc",
 * s2 = "dbbca",
 * When s3 = "aadbbcbcac", return true.
 * When s3 = "aadbbbaccc", return false.
 *
 * @author wcong<wc19920415@gmail.com>
 * @since 15/06/2017
 */
public class InterleavingString {
    public boolean isInterleaveBruteForce(String s1, String s2, String s3) {
        if (s1.length() + s2.length() != s3.length()) {
            return false;
        }
        return isInterleaveBruteForce(s1, 0, s2, 0, s3, 0);
    }

    private boolean isInterleaveBruteForce(String s1, int index1, String s2, int index2, String s3, int index3) {
        return (index3 < s3.length()
                && index1 < s1.length()
                && s3.charAt(index3) == s1.charAt(index1) &&
                isInterleaveBruteForce(s1, index1 + 1, s2, index2, s3, index3 + 1))
                || (index3 < s3.length()
                && index2 < s2.length()
                && s3.charAt(index3) == s2.charAt(index2)
                && isInterleaveBruteForce(s1, index1, s2, index2 + 1, s3, index3 + 1))
                || (index3 == s3.length() && index1 + index2 == s3.length());
    }

    public boolean isInterleaveDp(String s1, String s2, String s3) {
        if (s1.length() + s2.length() != s3.length()) {
            return false;
        }
        boolean[][] matchMatrix = new boolean[s1.length() + 1][s2.length() + 1];
        matchMatrix[0][0] = true;
        matchMatrix[1][0] = s3.charAt(0) == s1.charAt(0);
        matchMatrix[0][1] = s3.charAt(0) == s2.charAt(0);
        for (int i = 1; i < s3.length(); i++) {
            int length = i + 1;
            for (int index1 = 0; index1 < length && index1 < s1.length() && length - index1 < s2.length(); index1++) {
                matchMatrix[index1][length - index1] = s3.charAt(i) == s1.charAt(index1) && matchMatrix[index1][length - index1 - 1];
            }
        }
        return matchMatrix[s1.length()][s2.length()];
    }
}
