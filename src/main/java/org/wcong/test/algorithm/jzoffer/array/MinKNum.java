package org.wcong.test.algorithm.jzoffer.array;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * test for array and partition and sorting
 * Created by hzwangcong on 2017/3/27.
 */
public class MinKNum {

    public static void main(String[] args) {
        int[] array = new int[]{4, 5, 7, 1, 2, 4, 5, 6};
        minKNumPartition(array, 2);
        minKNumSort(array, 2);
    }


    public static int[] minKNumPartition(int[] array, int k) {
        if (array == null || k <= 0) {
            return null;
        }
        if (array.length <= k) {
            return array;
        }
        int start = 0, end = array.length - 1;
        while (end != k - 1) {
            int middle = start + (end - start) / 2;
            int middleValue = array[middle];
            array[middle] = array[end];
            array[end] = middleValue;
            int i = start - 1;
            for (int j = start; j < end; j++) {
                if (array[j] <= middleValue) {
                    i += 1;
                    if (i != j) {
                        int temp = array[i];
                        array[i] = array[j];
                        array[j] = temp;
                    }
                }
            }
            i += 1;
            int temp = array[i];
            array[i] = array[end];
            array[end] = temp;
            if (i == k - 1) {
                end = i;
            } else if (i > k - 1) {
                end = k - 1;
            } else {
                start = i + 1;
            }
        }
        return Arrays.copyOf(array, k);
    }

    public static int[] minKNumSort(int[] array, int k) {
        if (array == null || k <= 0) {
            return null;
        }
        if (array.length <= k) {
            return array;
        }
        PriorityQueue<Integer> minKQueue = new PriorityQueue<>(k, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2.compareTo(o1);
            }
        });
        for (int i = 0; i < array.length; i++) {
            if (minKQueue.size() < k) {
                minKQueue.add(array[i]);
            } else {
                minKQueue.add(array[i]);
                minKQueue.poll();
            }
        }
        int[] result = new int[k];
        int index = 0;
        for (int num : minKQueue) {
            result[index] = num;
            index += 1;
        }
        return result;
    }


}
