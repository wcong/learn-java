package org.wcong.test.algorithm.leetcode.array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Given two arrays of length m and n with digits 0-9 representing two numbers.
 * Create the maximum number of length k <= m + n from digits of the two.
 * The relative order of the digits from the same array must be preserved.
 * Return an array of the k digits.
 * You should try to optimize your time and space complexity
 *
 * @author wcong<wc19920415@gmail.com>
 * @since 13/06/2017
 */
public class CreateMaximumNumber {

    private int[] maxNumber;

    public int[] maxNumberBruteForce(int[] nums1, int[] nums2, int k) {
        int total = nums1.length + nums2.length;
        k = total > k ? k : total;
        maxNumber = new int[k];
        iterateMaxNumber(nums1, 0, nums2, 0, new int[k], k);
        return maxNumber;
    }

    private void iterateMaxNumber(int[] nums1, int index1, int[] nums2, int index2, int[] number, int left) {
        if (index1 >= nums1.length && index2 >= nums2.length && left > 0) {
            return;
        }
        if (left <= 0) {
            if (arrayCompare(number, maxNumber)) {
                maxNumber = Arrays.copyOf(number, number.length);
            }
            return;
        }
        if (index1 < nums1.length) {
            number[number.length - left] = nums1[index1];
            iterateMaxNumber(nums1, index1 + 1, nums2, index2, number, left - 1);
            iterateMaxNumber(nums1, index1 + 1, nums2, index2, number, left);
        }
        if (index2 < nums2.length) {
            number[number.length - left] = nums2[index2];
            iterateMaxNumber(nums1, index1, nums2, index2 + 1, number, left - 1);
            iterateMaxNumber(nums1, index1, nums2, index2 + 1, number, left);
        }
    }

    private boolean arrayCompare(int[] array1, int[] array2) {
        for (int i = 0; i < array2.length; i++) {
            if (array1[i] > array2[i]) {
                return true;
            } else if (array1[i] < array2[i]) {
                return false;
            }
        }
        return false;
    }

    static class Index {
        public Index(boolean first, int index) {
            this.first = first;
            this.index = index;
        }

        boolean first = false;
        int index = 0;
    }

    public int[] maxNumberGreed(int[] nums1, int[] nums2, int k) {
        int total = nums1.length + nums2.length;
        k = total > k ? k : total;
        maxNumber = new int[k];
        maxNumberGreed(nums1, 0, nums2, 0, k, new int[k]);
        return maxNumber;
    }

    private void maxNumberGreed(int[] nums1, int index1, int[] nums2, int index2, int left, int[] current) {
        if (left <= 0) {
            if (arrayCompare(current, maxNumber)) {
                maxNumber = Arrays.copyOf(current, current.length);
            }
            return;
        }
        List<Index> maxIndexList = findMaxIndex(nums1, index1, nums2, index2, left);
        for (Index index : maxIndexList) {
            if (index.first) {
                current[current.length - left] = nums1[index.index];
                maxNumberGreed(nums1, index.index + 1, nums2, index2, left - 1, current);
            } else {
                current[current.length - left] = nums2[index.index];
                maxNumberGreed(nums1, index1, nums2, index.index + 1, left - 1, current);
            }
        }
    }

    private List<Index> findMaxIndex(int[] nums1, int index1, int[] nums2, int index2, int left) {
        List<Index> maxList = new ArrayList<>();
        int firstEnd = nums1.length - left + nums2.length - index2;
        firstEnd = firstEnd >= nums1.length ? nums1.length - 1 : firstEnd;
        int max = 0;
        for (int i = index1; i <= firstEnd; i++) {
            if (nums1[i] > max) {
                maxList.clear();
                maxList.add(new Index(true, i));
                max = nums1[i];
            } else if (nums1[i] == max) {
                maxList.add(new Index(true, i));
            }
        }
        int secondEnd = nums2.length - left + nums1.length - index1;
        secondEnd = secondEnd >= nums2.length ? nums2.length - 1 : secondEnd;
        for (int i = index2; i <= secondEnd; i++) {
            if (nums2[i] > max) {
                maxList.clear();
                maxList.add(new Index(false, i));
                max = nums2[i];
            } else if (nums2[i] == max) {
                maxList.add(new Index(false, i));
            }
        }
        return maxList;
    }
}
