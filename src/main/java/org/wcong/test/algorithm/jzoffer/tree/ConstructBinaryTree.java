package org.wcong.test.algorithm.jzoffer.tree;

import org.springframework.util.Assert;

import java.util.Stack;

/**
 * test for tree
 * give a binary tree there inorder preorder postorder tree walk,
 * build a binary tree by preorder 12473568  and inorder 47215386 out put
 * Created by wcong on 2017/3/23.
 */
public class ConstructBinaryTree {


    public static void main(String[] args) {
        Assert.isTrue(constructBinaryTree("12473568", "47215386").preOrderString().equals("12473568"));
    }

    static class BinaryTreeNode {
        char value;
        BinaryTreeNode left;
        BinaryTreeNode right;
    }

    static class BinaryTree {
        BinaryTreeNode root;

        @Override
        public boolean equals(Object o) {
            if (o == null) {
                return false;
            }
            if (!(o instanceof BinaryTree)) {
                return false;
            }
            return isEqual(root, ((BinaryTree) o).root);
        }

        private boolean isEqual(BinaryTreeNode node1, BinaryTreeNode node2) {
            if (node1 == null && node2 == null) {
                return true;
            }
            if (node1 != null && node2 != null) {
                return node1.value == node2.value && isEqual(node1.left, node2.left) && isEqual(node1.right, node2.right);
            }
            return false;
        }

        public String preOrderString() {
            StringBuilder sb = new StringBuilder(12);
            Stack<BinaryTreeNode> tempNodeStack = new Stack<>();
            tempNodeStack.add(root);
            while (!tempNodeStack.isEmpty()) {
                BinaryTreeNode node = tempNodeStack.pop();
                sb.append(node.value);
                if (node.right != null) {
                    tempNodeStack.push(node.right);
                }
                if (node.left != null) {
                    tempNodeStack.push(node.left);
                }
            }
            return sb.toString();
        }
    }

    //by preorder 12473568  and inorder 47215386
    static BinaryTree constructBinaryTree(String preOrder, String inOrder) {
        if (preOrder == null || inOrder == null || preOrder.length() == 0 || inOrder.length() == 0 || preOrder.length() != inOrder.length()) {
            return null;
        }
        char[] preOrderArray = preOrder.toCharArray();
        char[] inOrderArray = inOrder.toCharArray();
        BinaryTree binaryTree = new BinaryTree();
        binaryTree.root = buildTree(preOrderArray, 0, preOrderArray.length - 1, inOrderArray, 0, inOrderArray.length - 1);
        return binaryTree;
    }

    private static BinaryTreeNode buildTree(char[] preOrderArray, int preOrderArrayLeft, int preOrderArrayRight,
                                            char[] inOrderArray, int inOrderArrayLeft, int inOrderArrayRight) {
        if (preOrderArrayLeft > preOrderArrayRight) {
            return null;
        }
        char inNode = preOrderArray[preOrderArrayLeft];
        BinaryTreeNode node = new BinaryTreeNode();
        node.value = inNode;
        int leftIndex = inOrderArrayLeft;
        while (leftIndex <= inOrderArrayRight) {
            if (inOrderArray[leftIndex] == inNode) {
                break;
            }
            leftIndex += 1;
        }
        node.left = buildTree(preOrderArray, preOrderArrayLeft + 1, preOrderArrayLeft+leftIndex-inOrderArrayLeft, inOrderArray, inOrderArrayLeft, leftIndex - 1);
        node.right = buildTree(preOrderArray, preOrderArrayLeft+leftIndex-inOrderArrayLeft + 1, preOrderArrayRight, inOrderArray, leftIndex + 1, inOrderArrayRight);
        return node;
    }

}
