package org.wcong.test.algorithm.jzoffer;

import org.wcong.test.algorithm.jzoffer.util.Tree;
import org.wcong.test.algorithm.jzoffer.util.TreeNode;

import java.util.LinkedList;
import java.util.Queue;

/**
 * test for binary tree
 *
 * @author wcong<wc19920415@gmail.com>
 * @since 2017/3/26
 */
public class PrintBinaryTreeTopDown {

	public static void main(String[] args) {

	}

	public static void printTreeTopDown(Tree tree) {
		if (tree == null || tree.root == null) {
			return;
		}
		Queue<TreeNode> treeNodeQueue = new LinkedList<>();
		treeNodeQueue.add(tree.root);
		while (!treeNodeQueue.isEmpty()) {
			TreeNode node = treeNodeQueue.poll();
			System.out.println(node.value);
			if (node.left != null) {
				treeNodeQueue.add(node.left);
			}
			if (node.right != null) {
				treeNodeQueue.add(node.right);
			}
		}
	}

}
