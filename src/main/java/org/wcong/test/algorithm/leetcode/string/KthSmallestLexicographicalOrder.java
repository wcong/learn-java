package org.wcong.test.algorithm.leetcode.string;

import java.util.Arrays;

/**
 * Given integers n and k
 * find the lexicographically k-th smallest integer in the range from 1 to n.
 * Note: 1 ≤ k ≤ n ≤ 10^9.
 * Example:
 * Input: n: 13 k: 2
 * Output:10
 * Explanation:
 * The lexicographical order is [1, 10, 11, 12, 13, 2, 3, 4, 5, 6, 7, 8, 9],
 * so the second smallest number is 10.
 *
 * @author wcong<wc19920415@gmail.com>
 * @since 05/06/2017
 */
public class KthSmallestLexicographicalOrder {

    public int findKthNumberBruteForce(int n, int k) {
        String[] numbers = new String[n];
        for (int i = 1; i <= n; i++) {
            numbers[i - 1] = String.valueOf(i);
        }
        Arrays.sort(numbers);
        return Integer.parseInt(numbers[k - 1]);
    }

    public static class Result {
        boolean found = false;
        int number = 1;
        int order = 1;
    }

    public int findKthNumberGreed(int n, int k) {
        Result result = new Result();
        findKthNumberGreed(n, k, result);
        return result.number;
    }

    private void findKthNumberGreed(int n, int k, Result result) {
        int number = result.number;
        if (result.found) {
            return;
        }
        for (int i = 0; i < 9; i++) {
            if (result.order < k && number * 10 <= n) {
                result.number = number * 10;
                result.order += 1;
                result.found = result.order == k;
                findKthNumberGreed(n, k, result);
            }
            if (result.found) {
                return;
            }
            number += 1;
            if (number > n) {
                return;
            }
            result.order += 1;
            result.number = number;
            result.found = result.order == k;
        }
    }

}
