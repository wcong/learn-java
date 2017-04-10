package org.wcong.test.algorithm.jzoffer.tree;

import org.wcong.test.algorithm.jzoffer.util.Tree;
import org.wcong.test.algorithm.jzoffer.util.TreeNode;

/**
 * test for binary tree
 * <p/>
 * give a binary tree return a mirror of it,left to right and right to left
 *
 * @author wcong<wc19920415@gmail.com>
 * @since 2017/3/26
 */
public class MirrorOfBinaryTree {

	public static void main(String[] args) {

	}

	public static void mirrorOfBinaryTree(Tree tree) {
		mirrorOfNode(tree.root);
	}

	private static void mirrorOfNode(TreeNode treeNode) {
		if (treeNode == null) {
			return;
		}
		TreeNode temp = treeNode.left;
		treeNode.left = treeNode.right;
		treeNode.right = temp;
		mirrorOfNode(treeNode.left);
		mirrorOfNode(treeNode.right);

	}

}
