package org.wcong.test.algorithm;

import org.eclipse.core.runtime.Assert;

/**
 * . means anyone char
 * * means none or many preceding
 * Created by hzwangcong on 2017/2/14.
 */
public class StringMatch {

    public static void main(String[] args) {
        Assert.isTrue(matchPart("abcd", "c*abcd"));
        Assert.isTrue(!matchPart("abcdefg", "abcdef*"));
        Assert.isTrue(matchPart("abcdefg", "abcde.*"));
        Assert.isTrue(matchPart("abcdefgbcd", "abcde.*cd"));
        Assert.isTrue(matchPart("aab", "a*b"));
        Assert.isTrue(matchPart("aaaab", "aa*ab"));
        Assert.isTrue(matchPart("abcd", ".bcd"));
        Assert.isTrue(matchPart("abcd", "a.*"));
        Assert.isTrue(matchPart("a", "ab*"));
        Assert.isTrue(matchPart("ab", ".*.."));
        Assert.isTrue(matchPart("abcaaaaaaabaabcabac", ".*ab.a.*a*a*.*b*b*"));
    }

    static boolean matchPart(String a, String b) {
        return match(a, b, 0, 0);
    }

    static boolean match(String a, String b, int aIndex, int bIndex) {
        while (aIndex < a.length() && bIndex < b.length()) {
            char aChar = a.charAt(aIndex);
            char bChar = b.charAt(bIndex);
            if ((bIndex + 1) < b.length() && b.charAt(bIndex + 1) == '*') {
                if (bChar == '.') {
                    if ((bIndex + 2) < b.length()) {
                        bChar = b.charAt(bIndex + 2);
                        while (aIndex < a.length()) {
                            if( bChar == '.' &&  match(a, b, aIndex, bIndex + 2)  ){
                                return true;
                            }
                            aChar = a.charAt(aIndex);
                            if (aChar == bChar && match(a, b, aIndex, bIndex + 2)) {
                                return true;
                            }
                            aIndex += 1;
                        }
                    } else {
                        return true;
                    }
                } else {
                    if (aChar == bChar) {
                        while (aIndex < a.length() && aChar == a.charAt(aIndex)) {
                            if (match(a, b, aIndex, bIndex + 2)) {
                                return true;
                            }
                            aIndex += 1;
                        }
                    }
                    bIndex += 2;
                    continue;
                }
            } else if (bChar == '.') {
                bIndex += 1;
                aIndex += 1;
                continue;
            } else if (aChar == bChar) {
                bIndex += 1;
                aIndex += 1;
                continue;
            }
            return false;
        }
        while (bIndex < b.length()) {
            if ((bIndex + 1) < b.length() && b.charAt(bIndex + 1) == '*') {
                bIndex += 2;
            } else {
                break;
            }
        }
        return aIndex == a.length() && bIndex == b.length();
    }
}
