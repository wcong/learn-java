package org.wcong.test.algorithm.jzoffer.array;

import org.springframework.util.Assert;

/**
 * test for array and recursive  TODO
 * Created by wcong on 2017/3/28.
 */
public class ReverseOrderPareInArray {

    public static void main(String[] args) {
        Assert.isTrue(reversPairsCount(new int[]{7, 5, 6, 4}) == 5);
    }

    public static int reversPairsCount(int[] array) {
        if (array == null || array.length < 2) {
            return 0;
        }
        int[] copy = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            copy[i] = array[i];
        }
        return count(array, copy, 0, array.length - 1);
    }

    private static int count(int[] original, int[] copy, int start, int end) {
        if (start == end) {
            copy[start] = original[start];
            return 0;
        }
        int length = (end - start) / 2;
        int left = count(copy, original, start, start + length);
        int right = count(copy, original, start + length + 1, end);
        int merge = 0;
        int leftEnd = start + length;
        int rightEnd = end;
        int current = end;
        while (leftEnd >= start && rightEnd > (start + length)) {
            if (original[leftEnd] > original[rightEnd]) {
                merge += rightEnd - start - length;
                copy[current--] = original[leftEnd--];
            } else {
                copy[current--] = original[rightEnd--];
            }
        }
        while (leftEnd >= start) {
            copy[current--] = original[leftEnd--];
        }
        while (rightEnd > start + length) {
            copy[current--] = original[rightEnd--];
        }
        return left + right + merge;
    }

}
