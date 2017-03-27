package org.wcong.test.algorithm.jzoffer;

import org.wcong.test.algorithm.jzoffer.util.BuildTree;
import org.wcong.test.algorithm.jzoffer.util.Tree;
import org.wcong.test.algorithm.jzoffer.util.TreeNode;

/**
 * test for tree and dqueue TODO
 * give a binary tree change it to dqueue
 *
 * @author wcong<wc19920415@gmail.com>
 * @since 2017/3/26
 */
public class ChangeBinaryTreeToDQueue {


    public static void main(String[] args) {
        changeToDQueue(BuildTree.buildOne(new int[]{4, 2, 6, 1, 3, 5, 7}));
    }

    public static TreeNode changeToDQueue(Tree tree) {
        return changeToDQueue(tree.root);
    }

    private static TreeNode changeToDQueue(TreeNode treeNode) {
        if (treeNode.left == null && treeNode.right == null) {
            return treeNode;
        }
        if (treeNode.left != null) {
            TreeNode node = changeToDQueue(treeNode.left);
            node.right = treeNode;
            treeNode.left = node;
        }
        TreeNode right = treeNode;
        if (treeNode.right != null) {
            TreeNode node = changeToDQueue(treeNode.right);
            treeNode.right = node;
            node.left = treeNode;
        }
        return right;
    }

}
