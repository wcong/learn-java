package org.wcong.test.algorithm.leetcode.tree;

/**
 * give a binary search tree and a node in it,find the inorder successor of the node in the bst
 * test for inorder tree walk,
 * if right child is not null,find the min node in left sub tree;
 * if right child is null,find the last  right ancestor.
 * Created by wcong on 2017/4/1.
 */
public class InorderSuccessorInBST {

	public static void main(String[] args) {

	}

	public static TreeNode inOrderSuccessor(TreeNode root, TreeNode findNode) {
		TreeNode lastLeftNode = null;
		TreeNode compareNode = root;
		while (compareNode != null) {
			if (findNode.val < compareNode.val) {
				lastLeftNode = compareNode;
				compareNode = compareNode.left;
			} else {
				compareNode = compareNode.right;
			}
		}
		return lastLeftNode;
	}

}
