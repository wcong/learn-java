package org.wcong.test.algorithm.leetcode.tree;

import java.util.LinkedList;
import java.util.List;

/**
 * give a binary tree, find the lowest common ancestor of given two node
 * Created by wcong on 2017/4/1.
 */
public class LowestCommonAncestorOfABinaryTree {

    public static void main(String[] args) {

    }

    public static TreeNode lowestCommonAncestor(TreeNode root, TreeNode node1, TreeNode node2) {
        List<TreeNode> path1 = new LinkedList<>();
        List<TreeNode> path2 = new LinkedList<>();
        path1.add(root);
        path2.add(root);
        findNode(root, node1, path1);
        findNode(root, node2, path2);
        for (int i = 0; i < path1.size() && i < path2.size(); i++) {
            if (path1.get(i) != path2.get(i)) {
                return path1.get(i - 1);
            }
        }
        return null;
    }

    private static boolean findNode(TreeNode treeNode, TreeNode findNode, List<TreeNode> path) {
        if (treeNode.value == findNode.value) {
            return true;
        } else {
            if (treeNode.left != null) {
                path.add(treeNode.left);
                if (findNode(treeNode.left, findNode, path)) {
                    return true;
                } else {
                    path.remove(path.size() - 1);
                }
            }
            if (treeNode.right != null) {
                path.add(treeNode.right);
                if (findNode(treeNode.right, findNode, path)) {
                    return true;
                } else {
                    path.remove(path.size() - 1);
                }
            }
            return false;
        }
    }
}
