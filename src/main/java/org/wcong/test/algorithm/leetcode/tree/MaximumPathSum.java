package org.wcong.test.algorithm.leetcode.tree;

/**
 * give a binary tree,find the maximum path sum for any path ,not have to include the root,just path
 * Created by wcong on 2017/4/1.
 */
public class MaximumPathSum {

    public static void main(String[] args) {

    }

    static class TreeNode {
        int value;
        TreeNode left;
        TreeNode right;
    }

    static int max = Integer.MIN_VALUE;

    public static int maximumPathSum(TreeNode root) {
        sumTree(root);
        return max;
    }

    private static int sumTree(TreeNode treeNode) {
        if (treeNode == null) {
            return 0;
        }
        int sum = treeNode.value;
        sum += Math.max(sumTree(treeNode.left), 0);
        sum += Math.max(sumTree(treeNode.right), 0);
        if (sum > max) {
            max = sum;
        }
        return sum;
    }

}
