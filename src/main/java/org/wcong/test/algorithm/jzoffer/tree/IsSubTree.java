package org.wcong.test.algorithm.jzoffer.tree;

import org.wcong.test.algorithm.jzoffer.util.Tree;
import org.wcong.test.algorithm.jzoffer.util.TreeNode;

/**
 * test for tree
 * <p/>
 * give tow tree,judge is one tree is other sub tree
 *
 * @author wcong<wc19920415@gmail.com>
 * @since 2017/3/26
 */
public class IsSubTree {

    public static void main(String[] args) {

    }

    public static boolean isSubTree(Tree tree, Tree subTree) {
        if (tree == null || tree.root == null || subTree == null || subTree.root == null) {
            return false;
        }
        TreeNode subNode = subTree.root;
        return isSubTree(tree.root, subNode);
    }

    public static boolean isSubTree(TreeNode node, TreeNode example) {
        if (example == null || node == null) {
            return false;
        }
        if (node.value == example.value) {
            if (isContainTree(node, example)) {
                return true;
            }
        }
        boolean isSub = isSubTree(node.left, example);
        return isSub || isSubTree(node.right, example);
    }

    private static boolean isContainTree(TreeNode node, TreeNode example) {
        if (example == null) {
            return true;
        }
        if (node == null) {
            return false;
        }
        if (node.value == example.value && isContainTree(node.left, example.left) && isContainTree(node.right, example.right)) {
            return true;
        }
        return false;
    }

}
