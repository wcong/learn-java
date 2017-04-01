package org.wcong.test.algorithm.leetcode.tree;

import java.util.Stack;

/**
 * as the name says
 * Created by wcong on 2017/4/1.
 */
public class InvertBinaryTree {

    public static void main(String[] args) {

    }

    public static void invert(TreeNode root) {
        if (root == null) {
            return;
        }
        TreeNode temp = root.left;
        root.left = root.right;
        root.right = temp;
        invert(root.left);
        invert(root.right);
    }

    public static void invertLoop(TreeNode root) {
        if (root == null) {
            return;
        }
        Stack<TreeNode> treeNodeStack = new Stack<>();
        treeNodeStack.push(root);
        while (!treeNodeStack.isEmpty()) {
            TreeNode treeNode = treeNodeStack.pop();
            TreeNode temp = treeNode.left;
            treeNode.left = treeNode.right;
            treeNode.right = temp;
            if (treeNode.left != null) {
                treeNodeStack.push(treeNode.left);
            }
            if (treeNode.right != null) {
                treeNodeStack.push(treeNode.right);
            }
        }
    }

}
