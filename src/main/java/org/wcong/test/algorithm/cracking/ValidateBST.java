package org.wcong.test.algorithm.cracking;

import org.wcong.test.algorithm.tree.BinaryTree;

/**
 * Created by wcong on 2017/4/14.
 */
public class ValidateBST {

    public static void main(String[] args) {

    }

    static class Result {
        boolean validate = true;
        int maxForNow = Integer.MIN_VALUE;
    }

    public static boolean validate(BinaryTree.Node root) {
        if (root == null) {
            return true;
        }
        Result result = new Result();
        validate(root, result);
        return result.validate;
    }

    public static void validate(BinaryTree.Node root, Result result) {
        if (root == null) {
            return;
        }
        if (root.left != null) {
            validate(root.left, result);
        }
        if (!result.validate) {
            return;
        }
        if (result.maxForNow > root.key) {
            result.validate = false;
            return;
        }
        result.maxForNow = root.key;
        validate(root.right, result);
    }

}
