package org.wcong.test.algorithm.leetcode.tree;

/**
 * Given a binary tree, find the maximum path sum.
 * For this problem,
 * a path is defined as any sequence of nodes from some starting node to any node in the tree along the parent-child connections.
 * The path must contain at least one node and does not need to go through the root.
 *
 * @author wcong<wc19920415@gmail.com>
 * @since 05/05/2017
 */
public class BinaryTreeMaximumSubPath {

	private int maxPath;

	public int maxPathSum(TreeNode root) {
		maxPath = Integer.MIN_VALUE;
		maxSubPathSum(root);
		return maxPath;
	}

	private int maxSubPathSum(TreeNode node) {
		if (node == null) {
			return 0;
		}
		int left = maxSubPathSum(node.left);
		int right = maxSubPathSum(node.right);
		int maxSum = node.val;
		if (left > 0) {
			maxSum += left;
		}
		if (right > 0) {
			maxSum += right;
		}
		if (maxPath < maxSum) {
			maxPath = maxSum;
		}
		int max = left > right ? left : right;
		return max > 0 ? max + node.val : node.val;
	}
}
