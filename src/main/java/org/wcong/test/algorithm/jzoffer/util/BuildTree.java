package org.wcong.test.algorithm.jzoffer.util;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 2017/3/26
 */
public class BuildTree {

	public static Tree buildOne(int[] array) {
		Tree tree = new Tree();
		TreeNode treeNode = new TreeNode();
		treeNode.value = array[0];
		tree.root = treeNode;
		buildTree(treeNode, array, 0);
		return tree;
	}

	private static void buildTree(TreeNode treeNode, int[] array, int h) {
		int leftNode = h * 2 + 1;
		if (leftNode < array.length) {
			TreeNode left = new TreeNode();
			left.value = array[leftNode];
			treeNode.left = left;
			buildTree(left, array, leftNode);
		}
		int rightNode = h * 2 + 2;
		if (rightNode < array.length) {
			TreeNode right = new TreeNode();
			right.value = array[rightNode];
			treeNode.right = right;
			buildTree(right, array, rightNode);
		}
	}

}
