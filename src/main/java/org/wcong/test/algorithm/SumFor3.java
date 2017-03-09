package org.wcong.test.algorithm;

import org.eclipse.core.runtime.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Given an array S of n integers, are there elements a, b, c in S such that a + b + c = 0?
 * Find all unique triplets in the array which gives the sum of zero.
 * Note: The solution set must not contain duplicate triplets.
 * For example, given array S = [-1, 0, 1, 2, -1, -4],
 * A solution set is:
 * [
 * [-1, 0, 1],
 * [-1, -1, 2]
 * ]
 * <p>
 * Created by hzwangcong on 2017/3/1.
 */
public class SumFor3 {

    public static void main(String[] args) {

        Assert.isTrue(listListEqual(treeSum(new int[]{-1, 0, 1, 2, -1, -4}), new ArrayList<List<Integer>>(Arrays.asList(Arrays.asList(-1, 0, 1), Arrays.asList(-1, -1, 2)))));
    }

    private static boolean listListEqual(List<List<Integer>> answerList, List<List<Integer>> sumArrays) {
        Iterator<List<Integer>> answerIterator = answerList.iterator();
        while (answerIterator.hasNext()) {
            List<Integer> answer = answerIterator.next();
            Iterator<List<Integer>> sumIterator = sumArrays.iterator();
            while (sumIterator.hasNext()) {
                List<Integer> sum = sumIterator.next();
                if (listEqual(answer, sum)) {
                    sumIterator.remove();
                    answerIterator.remove();
                    break;
                }
            }
        }
        return answerList.isEmpty() && sumArrays.isEmpty();
    }

    private static boolean listEqual(List<Integer> a, List<Integer> b) {
        List<Integer> aTemp = new ArrayList<>(a);
        List<Integer> bTemp = new ArrayList<>(b);
        Iterator<Integer> aIterator = aTemp.iterator();
        while (aIterator.hasNext()) {
            Integer aItem = aIterator.next();
            Iterator<Integer> bIterator = bTemp.iterator();
            while (bIterator.hasNext()) {
                Integer bItem = bIterator.next();
                if (aItem.equals(bItem)) {
                    bIterator.remove();
                    aIterator.remove();
                    break;
                }
            }
        }
        return aTemp.isEmpty() && bTemp.isEmpty();
    }


    public static List<List<Integer>> treeSum(int[] nums) {
        List<Integer> positiveSet = new ArrayList<>();
        List<Integer> negativeSet = new ArrayList<>();
        int zeroNum = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == 0) {
                zeroNum += 1;
            } else if (nums[i] < 0) {
                negativeSet.add(-nums[i]);
            } else {
                positiveSet.add(nums[i]);
            }
        }
        List<List<Integer>> sumList = new ArrayList<>();
        if (zeroNum > 0) {
            if (zeroNum > 2) {
                List<Integer> oneSum = new ArrayList<>(3);
                oneSum.add(0);
                oneSum.add(0);
                oneSum.add(0);
                sumList.add(oneSum);
            }
            for (Integer positive : positiveSet) {
                if (negativeSet.contains(positive)) {
                    List<Integer> oneSum = new ArrayList<>(3);
                    oneSum.add(0);
                    oneSum.add(positive);
                    oneSum.add(-positive);
                    sumList.add(oneSum);
                }
            }
        }
        sumList.addAll(sumInTwo(positiveSet, negativeSet, true));
        sumList.addAll(sumInTwo(negativeSet, positiveSet, false));
        return sumList;
    }

    private static List<List<Integer>> sumInTwo(List<Integer> positiveSet, List<Integer> negativeSet, boolean positive) {
        List<List<Integer>> sumList = new ArrayList<>();
        Integer[] nums = positiveSet.toArray(new Integer[positiveSet.size()]);
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                int sum = nums[i] + nums[j];
                if (negativeSet.contains(sum)) {
                    List<Integer> oneSum = new ArrayList<>(3);
                    oneSum.add(positive ? nums[i] : -nums[i]);
                    oneSum.add(positive ? nums[j] : -nums[j]);
                    oneSum.add(positive ? -sum : sum);
                    sumList.add(oneSum);
                }
            }
        }
        return sumList;
    }


}
