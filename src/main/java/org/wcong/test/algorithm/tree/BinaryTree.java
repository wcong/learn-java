package org.wcong.test.algorithm.tree;

/**
 * Created by wcong on 2017/3/17.
 */
public class BinaryTree {

    Node root;


    public static class Node {

        public Node(int key) {
            this.key = key;
        }

        public int key;

        public Node left;

        public Node right;

    }

    public Node root() {
        return root;
    }

    public void addNode(int key) {
        if (root == null) {
            root = new Node(key);
            return;
        }
        addNodeInternal(key, root);
    }

    public void deleteNode(int key) {
        if (root == null) {
            return;
        }
        if (root.key == key) {
            if (root.right == null) {
                if (root.left == null) {
                    root = null;
                } else {
                    root = root.left;
                }
            } else {
                Node parent = root;
                Node replace = root.right;
                while (replace.left != null) {
                    parent = replace;
                    replace = replace.left;
                }
                if (parent.left == replace) {
                    parent.left = null;
                } else {
                    parent.right = replace.right;
                }
                root.key = replace.key;
            }
        } else {
            if (key < root.key) {
                deleteNodeInternal(root, root.left, key);
            } else {
                deleteNodeInternal(root, root.right, key);
            }
        }
    }


    private void deleteNodeInternal(Node parent, Node node, int key) {
        if (node == null) {
            return;
        }
        if (node.key == key) {
            if (node.right == null && node.left == null) {
                if (parent.left == node) {
                    parent.left = null;
                } else {
                    parent.right = null;
                }
            } else if (node.right == null) {

            }
        } else if (node.key < key) {
            deleteNodeInternal(node, node.right, key);
        } else {
            deleteNodeInternal(node, node.left, key);
        }
    }

    private void addNodeInternal(int key, Node node) {
        if (node.key == key) {
            return;
        }
        if (key < node.key) {
            if (node.left == null) {
                node.left = new Node(key);
            } else {
                addNodeInternal(key, node.left);
            }
        } else {
            if (node.right == null) {
                node.right = new Node(key);
            } else {
                addNodeInternal(key, node.right);
            }
        }
    }

}
