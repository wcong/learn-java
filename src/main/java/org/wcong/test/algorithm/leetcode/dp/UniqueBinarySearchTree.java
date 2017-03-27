package org.wcong.test.algorithm.leetcode.dp;

/**
 * test for binary search tree and dynamic programing
 * <p>
 * // Given n, how many structurally unique BST's (binary search trees) that store values 1...n?
 * <p>
 * // For example,
 * // Given n = 3, there are a total of 5 unique BST's.
 * <p>
 * //    1         3     3      2      1
 * //     \       /     /      / \      \
 * //      3     2     1      1   3      2
 * //     /     /       \                 \
 * //    2     1         2                 3
 * Created by wcong on 2017/3/27.
 */
public class UniqueBinarySearchTree {

    public static void main(String[] args) {

    }


    public static int binarySearchTree(int n) {
        if (n <= 0) {
            return 0;
        }
        int[] rangeNum = new int[n];
        rangeNum[0] = 1;
        rangeNum[1] = 1;
        for (int i = 2; i <= n; i++) {
            for (int j = 1; j <= i; j++) {
                rangeNum[i] += rangeNum[j - 1] * rangeNum[j - i];
            }
        }
        return rangeNum[n];
    }


}
