package org.wcong.test.algorithm.leetcode.tree;

/**
 * give a binary search tree and a node in it,find the inorder successor of the node in the bst
 * test for inorder tree walk,
 * if left child of parent the successor is parent
 * if right child of parent the successor is last inorder of left sub tree
 * Created by wcong on 2017/4/1.
 */
public class InorderSuccessorInBST {

    public static void main(String[] args) {

    }

    public static TreeNode inOrderSuccessor(TreeNode treeNode, TreeNode findNode) {
        return successor(treeNode, null, findNode);
    }

    private static TreeNode successor(TreeNode treeNode, TreeNode parent, TreeNode findNode) {
        if (treeNode.value == findNode.value) {
            if (parent == null) {
                return null;
            } else if (parent.left == treeNode) {
                return parent;
            } else {
                TreeNode successor = parent.left;
                while (!(successor.left == null && successor.right == null)) {
                    if (successor.right != null) {
                        successor = successor.right;
                        continue;
                    }
                    successor = successor.left;
                }
                return successor;
            }
        } else if (findNode.value < treeNode.value) {
            return successor(treeNode.left, treeNode, findNode);
        } else {
            return successor(treeNode.right, treeNode, findNode);
        }
    }

}
