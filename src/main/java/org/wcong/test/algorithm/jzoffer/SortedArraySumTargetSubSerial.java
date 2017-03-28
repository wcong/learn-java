package org.wcong.test.algorithm.jzoffer;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * test for array and two pointer
 * give a sorted array return all serial sub array sum to target
 * for example a array 1,2,3,4,5,6,7,8 target 15 will give {1,2,3,4,5} {4,5,6} {7,8}
 * Created by wcong on 2017/3/28.
 */
public class SortedArraySumTargetSubSerial {


    public static void main(String[] args) {
        subSerial(new int[]{1, 2, 3, 4, 5, 6, 7, 8}, 15);
    }

    public static List<int[]> subSerial(int[] array, int target) {
        if (array == null || array.length < 2) {
            return null;
        }
        List<int[]> resultList = new LinkedList<>();
        int left = 0, right = 1;
        int sum = array[left] + array[right];
        while (right < array.length) {
            if (sum > target) {
                if (left == right) {
                    break;
                }
                sum -= array[left++];
            } else if (sum < target) {
                if (right == array.length - 1) {
                    break;
                }
                sum += array[++right];
            }
            if (sum == target) {
                resultList.add(Arrays.copyOfRange(array, left, right + 1));
                sum -= array[left++];
            }
        }
        return resultList;
    }
}
