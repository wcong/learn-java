package org.wcong.test.datastructure;

import java.util.Stack;

/**
 * search,add,delete,traversal for binary search tree
 * minimum, maximize, floor, ceiling,selection, rank,
 * Created by wcong on 2017/4/5.
 */
public class BinarySearchTree<T extends Comparable> {

    private static class Node<T> {
        T value;
        Node<T> left;
        Node<T> right;
    }

    private Node<T> root;

    public Node<T> findIterative(T value) {
        Node<T> node = root;
        while (node != null) {
            if (node.value.equals(value)) {
                return node;
            } else if (node.value.compareTo(value) > 0) {
                node = node.left;
            } else {
                node = node.right;
            }
        }
        return null;
    }

    public Node<T> findRecursive(T value) {
        return findNode(root, value);
    }

    private Node<T> findNode(Node<T> node, T value) {
        if (node == null) {
            return null;
        }
        if (node.value == value) {
            return node;
        } else if (node.value.compareTo(value) > 0) {
            return findNode(node.left, value);
        } else {
            return findNode(node.right, value);
        }
    }

    public void insertIterative(T value) {
        if (root == null) {
            root = new Node<>();
            root.value = value;
            return;
        }
        Node<T> parent = root;
        Node<T> node = root;
        while (node != null) {
            if (node.value.equals(value)) {
                return;
            } else if (node.value.compareTo(value) > 0) {
                parent = node;
                node = node.left;
            } else {
                parent = node;
                node = node.right;
            }
        }
        Node<T> insertNode = new Node<>();
        insertNode.value = value;
        if (parent.value.compareTo(value) > 0) {
            parent.left = insertNode;
        } else {
            parent.right = insertNode;
        }
    }

    public void insertRecursive(T value) {
        if (root == null) {
            root = new Node<>();
            root.value = value;
            return;
        }
        insertNode(root, value);
    }

    private void insertNode(Node<T> node, T value) {
        if (node == null || node.value.equals(value)) {
            return;
        }
        if (node.value.compareTo(value) > 0) {
            if (node.left != null) {
                insertNode(node.left, value);
            } else {
                Node<T> insertNode = new Node<>();
                node.value = value;
                node.left = insertNode;
            }
        } else {
            if (node.right != null) {
                insertNode(node.right, value);
            } else {
                Node<T> insertNode = new Node<>();
                node.value = value;
                node.right = insertNode;
            }
        }
    }

    public Node<T> deleteMin(Node<T> parent, Node<T> node) {
        if (node.left != null) {
            return deleteMin(node, node.left);
        }
        if (parent.left == node) {
            parent.left = node.right;
        } else {
            parent.right = node.right;
        }
        return node;
    }

    private void deleteIterative(T value) {
        Node<T> parent = null;
        Node<T> node = root;
        while (node != null) {
            if (node.value.equals(value)) {
                break;
            } else if (node.value.compareTo(value) > 0) {
                parent = node;
                node = node.left;
            } else {
                parent = node;
                node = node.right;
            }
        }
        if (node == null) {
            return;
        }
        if (parent == null) {
            if (node.right == null) {
                root = node.left;
            } else {
                root = deleteMin(root, root.left);
            }
        } else {
            if (node.right == null) {
                if (parent.left == node) {
                    parent.left = node.left;
                } else {
                    parent.right = node.left;
                }
            } else {
                Node<T> rightMin = deleteMin(node, node.right);
                rightMin.left = node.left;
                rightMin.right = node.right;
                if (parent.left == node) {
                    parent.left = rightMin;
                } else {
                    parent.right = rightMin;
                }
            }
        }
    }

    public void inOrderTraversal() {
        inOrderTraversal(root);
    }

    private void inOrderTraversal(Node<T> node) {
        if (node == null) {
            return;
        }
        inOrderTraversal(node.left);
        System.out.println(node.value);
        inOrderTraversal(node.right);
    }

    private void inOrderTravelsalIterative() {

    }

    private void preOrderTraversalIterative() {
        Stack<Node<T>> stack = new Stack<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            Node<T> node = stack.pop();
            System.out.println(node.value);
            if (node.right != null) {
                stack.push(node.right);
            }
            if (node.left != null) {
                stack.push(node.left);
            }
        }
    }

    private void postOrderTraversalIterative() {
        Stack<Node<T>> stack = new Stack<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            Node<T> node = stack.pop();
        }
    }

}
