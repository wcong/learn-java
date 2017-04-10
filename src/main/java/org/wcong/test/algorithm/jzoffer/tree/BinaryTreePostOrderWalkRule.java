package org.wcong.test.algorithm.jzoffer.tree;

import org.springframework.util.Assert;

/**
 * test for binary tree post-order walk analysis
 * give a array of integer judge is binary search tree post-order walk
 * for example 5,7,6,9,11,10,8 is true,7,4,6,5 is not
 *
 * @author wcong<wc19920415@gmail.com>
 * @since 2017/3/26
 */
public class BinaryTreePostOrderWalkRule {

    public static void main(String[] args) {
        Assert.isTrue(isBinarySearchTree(new int[]{5, 7, 6, 9, 11, 10, 8}));
        Assert.isTrue(!isBinarySearchTree(new int[]{7, 4, 6, 5}));
    }

    public static boolean isBinarySearchTree(int[] array) {
        if (array == null || array.length == 0) {
            return false;
        }
        return isBinarySearchTree(array, 0, array.length - 1);
    }

    private static boolean isBinarySearchTree(int[] array, int start, int end) {
        if (start > end || start < 0 || end > array.length) {
            return true;
        }
        int middle = array[end];
        int leftEnd = start;
        for (; leftEnd < end; leftEnd++) {
            if (array[leftEnd] >= middle) {
                break;
            }
        }
        leftEnd -= 1;
        for (int i = leftEnd + 1; i < end; i++) {
            if (array[i] < middle) {
                return false;
            }
        }
        return isBinarySearchTree(array, start, leftEnd) && isBinarySearchTree(array, leftEnd + 1, end - 1);
    }
}
