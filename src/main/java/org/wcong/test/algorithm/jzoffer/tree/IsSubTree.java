package org.wcong.test.algorithm.jzoffer.tree;

import org.wcong.test.algorithm.jzoffer.util.Tree;
import org.wcong.test.algorithm.jzoffer.util.TreeNode;

/**
 * test for tree
 * <p/>
 * give tow tree,judge is one tree is other sub tree
 *
 * @author wcong<wc19920415@gmail.com>
 * @since 2017/3/26
 */
public class IsSubTree {

	public static void main(String[] args) {

	}

	public static boolean isSubTree(Tree tree, Tree subTree) {
		if (tree == null || tree.root == null || subTree == null || subTree.root == null) {
			return false;
		}
		TreeNode subNode = subTree.root;
		TreeNode fromNode = findSameNode(tree.root, subNode);
		return false;
	}

	public static TreeNode findSameNode(TreeNode node, TreeNode example) {
		if (node == null || example == null) {
			return null;
		}
		if (node.value == example.value) {
			return node;
		}
		TreeNode leftResult = findSameNode(node.left, example);
		if (leftResult != null) {
			return leftResult;
		}
		return findSameNode(node.right, example);
	}

}
