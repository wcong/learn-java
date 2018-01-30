package org.wcong.test.algorithm.cracking.graph;

import org.wcong.test.algorithm.BinaryTree;

import java.util.LinkedList;
import java.util.List;

/**
 * give a binary search tree with distinct elements
 * print all possible arrays that could led to this tree
 *
 * @author wcong<wc19920415@gmail.com>
 * @since 18/04/2017
 */
public class BinarySequences {

	public static void main(String[] args) {

	}

	public static List<List<BinaryTree.Node>> sequences(BinaryTree.Node root) {
		List<List<BinaryTree.Node>> result = new LinkedList<>();
		List<BinaryTree.Node> path = new LinkedList<>();
		List<BinaryTree.Node> nextList = new LinkedList<>();
		nextList.add(root);
		sequences(nextList, path, result);
		return result;
	}

	private static void sequences(List<BinaryTree.Node> nextList, List<BinaryTree.Node> path,
			List<List<BinaryTree.Node>> result) {
		if (nextList.isEmpty()) {
			result.add(path);
			return;
		}
		int i = 0;
		for (BinaryTree.Node next : nextList) {
			List<BinaryTree.Node> soloPath = new LinkedList<>(path);
			path.add(next);
			List<BinaryTree.Node> nextNextList = new LinkedList<>(nextList);
			nextNextList.remove(i);
			if (next.left != null) {
				nextNextList.add(next.left);
			}
			if (next.right != null) {
				nextNextList.add(next.right);
			}
			sequences(nextNextList, soloPath, result);
			i += 1;
		}
	}

}
