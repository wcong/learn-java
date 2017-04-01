package org.wcong.test.algorithm.leetcode.tree;

import java.util.Stack;

/**
 * sum all of the leaves of the tree
 * Created by wcong on 2017/4/1.
 */
public class SumOfLeftLeaves {

    public static void main(String[] args) {

    }

    public static int sumLeft(TreeNode root) {
        int sum = 0;
        if (root == null) {
            return sum;
        }
        Stack<TreeNode> treeNodeStack = new Stack<>();
        treeNodeStack.push(root);
        while (!treeNodeStack.isEmpty()) {
            TreeNode treeNode = treeNodeStack.pop();
            if (treeNode.right != null) {
                treeNodeStack.push(treeNode.right);
            }
            if (treeNode.left != null) {
                sum += treeNode.left.value;
                treeNodeStack.push(treeNode.left);
            }
        }
        return sum;
    }

}
