package org.wcong.test.algorithm.jzoffer.array;

import org.eclipse.core.runtime.Assert;

/**
 * test for array
 * give a array and there is a num appear more than a half find it
 * Created by hzwangcong on 2017/3/27.
 */
public class MoreThanHalfInArray {

    public static void main(String[] args) {
        Assert.isTrue(halfNum(new int[]{1, 2, 1, 1, 3, 1, 4}) == 1);
    }

    public static int halfNum(int[] array) {
        int num = array[0];
        int count = 1;
        for (int i = 1; i < array.length; i++) {
            if (array[i] == num) {
                count += 1;
            } else {
                count -= 1;
            }
            if (count <= 0) {
                num = array[i];
                count = 1;
            }
        }
        return num;
    }

}
