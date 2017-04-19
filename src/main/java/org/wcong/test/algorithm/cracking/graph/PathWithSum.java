package org.wcong.test.algorithm.cracking.graph;

import org.wcong.test.algorithm.BinaryTree;

import java.util.HashMap;
import java.util.Map;

/**
 * you are given a binary tree which every node contains an integer value(positive or negative)
 * design an algorithm to count the numbers of path that sum to a given value
 * the path does need to start or end at the root or leaf but it must go downwards
 * depth first traversal, it can be solved by a algorithm for count subList sum to a target
 *
 * @author wcong<wc19920415@gmail.com>
 * @since 18/04/2017
 */
public class PathWithSum {

	public static void main(String[] args) {

	}

	public static int count(BinaryTree.Node root, int target) {
		return count(root, 0, target, new HashMap<Integer, Integer>());
	}

	private static int count(BinaryTree.Node root, int sum, int target, Map<Integer, Integer> sumMap) {
		if (root == null) {
			return 0;
		}
		sum += root.value;
		int left = target - sum;
		Integer leftNum = sumMap.get(left);
		leftNum = leftNum == null ? 0 : leftNum;
		int count = leftNum;
		if (sum == target) {
			count += 1;
		}

		sumMap.put(sum, sumMap.get(sum) == null ? 1 : sumMap.get(sum) + 1);

		int leftCount = count(root.left, sum, target, sumMap);
		int rightCount = count(root.right, sum, target, sumMap);
		sumMap.put(sum, sumMap.get(sum) - 1);
		return count + leftCount + rightCount;
	}

}
