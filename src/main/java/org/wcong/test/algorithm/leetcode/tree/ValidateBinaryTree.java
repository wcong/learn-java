package org.wcong.test.algorithm.leetcode.tree;

/**
 * give a binary tree,test if it is a binary tree
 * Created by wcong on 2017/4/1.
 */
public class ValidateBinaryTree {

	public static void main(String args) {
	}

	static int max = Integer.MIN_VALUE;

	public static boolean validate(TreeNode treeNode) {
		if (treeNode == null) {
			return true;
		}
		if (!validate(treeNode.left)) {
			return false;
		}
		if (treeNode.val >= max) {
			max = treeNode.val;
		} else {
			return false;
		}
		return validate(treeNode.right);
	}

}
