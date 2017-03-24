package org.wcong.test.algorithm.jzoffer;

import org.springframework.util.Assert;

import java.util.Arrays;

/**
 * test for sort and search
 * give a sorted array and rotate some num,find the min num in it
 * Created by wcong on 2017/3/24.
 */
public class MinNumInRotateArray {

    public static void main(String[] args) {
        Assert.isTrue(Arrays.equals(rotateArray(new int[]{1, 2, 3, 4, 5, 6, 7}, 2), new int[]{6, 7, 1, 2, 3, 4, 5}));
        Assert.isTrue(Arrays.equals(rotateArray(new int[]{1, 2, 3, 4, 5, 6, 7}, 5), new int[]{3, 4, 5, 6, 7, 1, 2}));
        Assert.isTrue(Arrays.equals(rotateArray(new int[]{1, 2, 3, 4, 5, 6, 7, 8}, 4), new int[]{5, 6, 7, 8, 1, 2, 3, 4}));
        Assert.isTrue(findMinNumForRotateArray(rotateArray(new int[]{1, 2, 3, 4, 5, 6, 7, 8}, 4))==1);
        Assert.isTrue(findMinNumForRotateArray(rotateArray(new int[]{1, 2, 3, 4, 5, 6, 7}, 5))==1);
        Assert.isTrue(findMinNumForRotateArray(rotateArray(new int[]{1, 2, 3, 4, 5, 6, 7}, 2))==1);
    }

    //  example 12345678 34567812
    private static int[] rotateArray(int[] array, int rotate) {
        if (array == null || array.length <= 1 || rotate <= 0) {
            return array;
        }
        int n = array.length;
        int start = 0;
        while (n > 0 && (rotate = rotate % n) > 0) {
            for (int i = 0; i < rotate; i++) {
                int index = start + i;
                int swapIndex = n - rotate + i + start;
                int temp = array[index];
                array[index] = array[swapIndex];
                array[swapIndex] = temp;
            }
            start += rotate;
            n -= rotate;
        }
        return array;
    }

    private static int findMinNumForRotateArray(int[] array) {
        if (array == null || array.length == 0) {
            throw new RuntimeException("wrong array");
        }
        int first = array[0];
        int left = 0, right = array.length - 1;
        while (left <= right) {
            int middle = left + (right - left) / 2;
            if (array[middle] > first) {
                left = middle + 1;
            } else {
                right = middle - 1;
            }
        }
        return array[left];
    }

}
