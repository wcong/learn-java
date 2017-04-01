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
        TreeNode treeNode = changeToDQueue(tree.root, null);
        while (treeNode.left != null) {
            treeNode = treeNode.left;
        }
        return treeNode;
    }

    private static TreeNode changeToDQueue(TreeNode treeNode, TreeNode lastInQueue) {
        if (treeNode.left != null) {
            lastInQueue = changeToDQueue(treeNode.left, lastInQueue);
        }
        if (lastInQueue != null) {
            lastInQueue.right = treeNode;
            treeNode.left = lastInQueue;
        }
        lastInQueue = treeNode;
        if (treeNode.right != null) {
            lastInQueue = changeToDQueue(treeNode.right, lastInQueue);
        }
        return lastInQueue;
    }

}
