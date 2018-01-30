package org.wcong.test.algorithm.cracking.graph;

import org.wcong.test.algorithm.BinaryTree;

/**
 * design a algorithm and write code to
 * find the first common ancestor of two nodes in a binary tree,
 * avoid store additional node in data structure
 * 1. has parent link ,it's like two linked list intersection
 * 2. no parent link like tree traversal
 *
 * @author wcong<wc19920415@gmail.com>
 * @since 18/04/2017
 */
public class FirstCommonAncestor {

	public static void main(String[] args) {
	}

	static class Result {
		BinaryTree.Node node;

		boolean isAncestor;

		public Result(BinaryTree.Node node, boolean isAncestor) {
			this.node = node;
			this.isAncestor = isAncestor;
		}
	}

	public static Result firstCommonAncestor(BinaryTree.Node root, BinaryTree.Node node1, BinaryTree.Node node2) {
		if (root == null) {
			return new Result(null, false);
		}
		if (root.value == node1.value && root.value == node2.value) {
			return new Result(root, false);
		}
		Result left = firstCommonAncestor(root.left, node1, node2);
		if (left.isAncestor) {
			return left;
		}
		Result right = firstCommonAncestor(root.right, node1, node2);
		if (right.isAncestor) {
			return right;
		}
		if (left.node != null && right.node != null) {
			return new Result(root, true);
		} else if (root.value == node1.value || root.value == node2.value) {
			boolean isAncestor = left.node != null || right.node != null;
			return new Result(root, isAncestor);
		} else {
			return new Result(null, false);
		}
	}
}
