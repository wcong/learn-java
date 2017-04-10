package org.wcong.test.datastructure;

/**
 * search,insert,delete for a linked list
 * Created by wcong on 2017/4/5.
 */
public class LinkedList<T> {

    private static class Node<T> {
        T value;
        Node<T> next;
    }

    private Node<T> root;

    public Node<T> find(T value) {
        Node<T> node = root;
        while (root != null) {
            if (root.value.equals(value)) {
                return node;
            }
            node = node.next;
        }
        return null;
    }

    public void insert(T value) {
        if (root == null) {
            Node<T> insert = new Node<>();
            insert.value = value;
            root = insert;
            return;
        }
        Node<T> node = root;
        while (true) {
            if (node.value == value) {
                return;
            }
            if (node.next == null) {
                break;
            }
            node = node.next;
        }
        Node<T> insert = new Node<>();
        insert.value = value;
        node.next = insert;
    }

    public void delete(T value) {
        if (root == null) {
            return;
        }
        if (root.value.equals(value)) {
            root = root.next;
        }
        Node<T> parent = root;
        Node<T> node = root.next;
        while (node != null && node.value != value) {
            parent = node;
            node = node.next;
        }
        if (node != null) {
            parent.next = node.next;
        }
    }

}
