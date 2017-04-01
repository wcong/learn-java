package org.wcong.test.algorithm.leetcode.string;

import org.eclipse.core.runtime.Assert;

/**
 * string also a dynamic programming
 * encode way,A->1,....,Z->26
 * give a string of numbers,count all the ways can be decoded
 * for example 12 can be decode two ways 1->A,2->B or 12->L
 * Created by wcong on 2017/4/1.
 */
public class DecodeWays {

    public static void main(String[] args) {
        Assert.isTrue(decodeWays("123") == 3);
    }

    public static int decodeWays(String encode) {
        int[] result = new int[encode.length() + 1];
        result[0] = 1;
        result[1] = 1;
        for (int i = 1; i < encode.length(); i++) {
            if (Integer.parseInt(encode.substring(i - 1, i + 1)) <= 26) {
                result[i + 1] = result[i - 1] + result[i];
            } else {
                result[i + 1] = result[i];
            }
        }
        return result[encode.length()];
    }

}
