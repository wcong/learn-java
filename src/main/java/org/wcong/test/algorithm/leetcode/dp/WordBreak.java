package org.wcong.test.algorithm.leetcode.dp;

/**
 * test for dynamic programming
 * <p>
 * // Given a non-empty string s and a dictionary wordDict containing a list of non-empty words,
 * determine if s can be segmented into a space-separated sequence of one or more dictionary words.
 * You may assume the dictionary does not contain duplicate words.
 * <p>
 * // For example, given
 * // s = "leetcode",
 * // dict = ["leet", "code"].
 * <p>
 * // Return true because "leetcode" can be segmented as "leet code".
 * Created by wcong on 2017/3/27.
 */
public class WordBreak {

    public static void main(String[] args) {

    }

    // suppose it has none blank char
    public static boolean wordBreak(String s, String[] dict) {
        if (dict == null || dict.length == 0 || s == null || s.length() == 0) {
            return false;
        }
        boolean[] result = new boolean[s.length()];
        result[0] = true;
        for (int i = 0; i < s.length(); i++) {
            for (int j = i; j >= 0; j--) {
                if (isInDict(s.substring(j, i + 1), dict) && result[j]) {
                    result[i + 1] = true;
                    break;
                }
            }
        }
        return result[s.length()];
    }


    private static boolean isInDict(String s, String[] dict) {
        for (String word : dict) {
            if (word.equals(s)) {
                return true;
            }
        }
        return false;
    }
}
