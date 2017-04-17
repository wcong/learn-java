package org.wcong.test.algorithm.cracking;

import org.wcong.test.algorithm.tree.BinaryTree;

/**
 * Created by wcong on 2017/4/14.
 */
public class Successor {

    public static void main(String[] args) {

    }

    public static BinaryTree.Node successor(BinaryTree.Node root, BinaryTree.Node target) {
        if (root == null || target == null) {
            return null;
        }
        BinaryTree.Node successor = null;
        while (root != null) {
            if (target.key < root.key) {
                successor = root;
                root = root.left;
            } else {
                root = root.right;
            }
        }
        return successor;
    }

}
