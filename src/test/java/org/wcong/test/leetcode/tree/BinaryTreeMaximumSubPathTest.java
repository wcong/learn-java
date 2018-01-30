package org.wcong.test.leetcode.tree;

import org.junit.Assert;
import org.junit.Test;
import org.wcong.test.algorithm.leetcode.tree.BinaryTreeMaximumSubPath;
import org.wcong.test.algorithm.leetcode.tree.TreeNode;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 05/05/2017
 */
public class BinaryTreeMaximumSubPathTest {

	@Test
	public void test() {
		BinaryTreeMaximumSubPath maximumSubPath = new BinaryTreeMaximumSubPath();
		TreeNode treeNode = new TreeNode();
		treeNode.val = 1;
		TreeNode left = new TreeNode();
		left.val = 2;
		treeNode.left = left;
		TreeNode right = new TreeNode();
		right.val = 3;
		treeNode.right = right;
		Assert.assertTrue(maximumSubPath.maxPathSum(treeNode) == 6);
	}

}
