package org.wcong.test.algorithm.jzoffer;

/**
 * test for array and binary search
 * give a sorted array count the target appear num
 * Created by hzwangcong on 2017/3/28.
 */
public class CountInSortedArray {

    public static void main(String[] args) {

    }

    private static int countInSortedArray(int[] array, int target) {
        int index = index(array, 0, array.length - 1, target);
        if (index == 0) {
            return 0;
        }
        int count = 1;
        for (int i = index - 1; i > 0; i--) {
            if (array[i] == target) {
                count += 1;
            } else {
                break;
            }
        }
        for (int i = index + 1; i < array.length; i++) {
            if (array[i] == target) {
                count += 1;
            } else {
                break;
            }
        }
        return count;
    }

    public static int index(int[] array, int start, int end, int target) {
        if (start < end) {
            return -1;
        }
        int middle = start + (end - start) / 2;
        if (array[middle] == target) {
            return middle;
        } else if (array[middle] > target) {
            return index(array, start, middle - 1, target);
        } else {
            return index(array, middle + 1, end, target);
        }
    }

}
