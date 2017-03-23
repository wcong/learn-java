package org.wcong.test.algorithm.jzoffer;

import org.springframework.util.Assert;

/**
 * test for string
 * give a string replace all blank by %20
 * Created by wcong on 2017/3/23.
 */
public class ReplaceBlank {

    public static void main(String[] args) {
        Assert.isTrue(replaceString("abs as  das ").equals("abs%20as%20%20das%20"));
    }

    public static String replaceString(String oldString) {
        int length = oldString.length();
        for (int i = 0; i < oldString.length(); i++) {
            if (oldString.charAt(i) == ' ') {
                length += 2;
            }
        }
        char[] newChar = new char[length];
        int newCharIndex = 0;
        for (int i = 0; i < oldString.length(); i++) {
            if (oldString.charAt(i) == ' ') {
                newChar[newCharIndex++] = '%';
                newChar[newCharIndex++] = '2';
                newChar[newCharIndex++] = '0';
            } else {
                newChar[newCharIndex++] = oldString.charAt(i);
            }
        }
        return new String(newChar);
    }

}
