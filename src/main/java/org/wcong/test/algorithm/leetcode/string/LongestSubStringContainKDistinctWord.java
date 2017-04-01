package org.wcong.test.algorithm.leetcode.string;

import org.springframework.util.Assert;

/**
 * give a string find the longest sub string that contains k distinct word
 * for example cecba k =2 longest sub string is ece
 * Created by wcong on 2017/4/1.
 */
public class LongestSubStringContainKDistinctWord {

    public static void main(String[] args) {
        Assert.isTrue(subString("cecba", 2).equals("cec"));
    }

    public static String subString(String a, int k) {
        int longestStart = 0;
        int longestEnd = 0;
        int start = 0;
        int distinct = 0;
        for (int i = 0; i < a.length(); i++) {
            if (!a.substring(start, i).contains(a.substring(i, i + 1))) {
                distinct += 1;
            }
            if (distinct == k && i - start > longestEnd - longestStart) {
                longestEnd = i;
                longestStart = start;
            } else if (distinct > k) {
                while (start <= i) {
                    start = start + 1;
                    if (!a.substring(start + 1, i + 1).contains(a.substring(start, start + 1))) {
                        break;
                    }
                }
            }
        }
        return a.substring(longestStart, longestEnd + 1);
    }

}
