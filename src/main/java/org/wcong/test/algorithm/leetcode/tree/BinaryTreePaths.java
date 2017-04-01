package org.wcong.test.algorithm.leetcode.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * given a binary tree,return all root-to-leaf path
 * Created by wcong on 2017/4/1.
 */
public class BinaryTreePaths {

    static class TreeNode {
        int value;
        TreeNode left;
        TreeNode right;
    }

    public static void main(String[] args) {

    }

    public List<String> paths(TreeNode treeNode) {
        List<String> path = new ArrayList<>();
        findPath(treeNode, "", path);
        return path;
    }

    private static void findPath(TreeNode treeNode, String prefix, List<String> path) {
        if (treeNode == null) {
            path.add(prefix);
            return;
        }
        prefix += treeNode.value;
        findPath(treeNode.left, prefix, path);
        findPath(treeNode.right, prefix, path);
    }
}
