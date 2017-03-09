package org.wcong.test.algorithm;

/**
 * Created by hzwangcong on 2017/2/7.
 */
public class BinaryTree {

    private Node root;

    public static void main(String[] args) {

    }

    public void add(int value){
        if( root == null ){
            root = new Node();
            root.value = value;
            return;
        }
        Node last = root;
        while (true){
            Node next;
            if( value < last.value ){
                next = last.left;
                if( next == null ){
                    Node newNode = new Node();
                    newNode.value = value;
                    last.left = newNode;
                }else{
                    last = next;
                }
            }else{
                next = last.right;
                if( next == null ){
                    Node newNode = new Node();
                    newNode.value = value;
                    last.right = newNode;
                }else{
                    last = next;
                }
            }
        }
    }

    DubboLinkedInt binaryTreeToLink() {
        if( root == null ){
            return null;
        }
        DubboLinkedInt middle = new DubboLinkedInt();
        middle.value = root.value;
        recurrentLink(root,middle);
        DubboLinkedInt first = middle;
        while (first.left!=null){
            first = first.left;
        }
        return first;
    }

    private void recurrentLink( Node parentNode,DubboLinkedInt parentInt ){
        if( parentNode.left != null ){
            DubboLinkedInt leftInt = new DubboLinkedInt();
            leftInt.value = parentNode.left.value;
            leftInt.right = parentInt;
            if( parentInt.left != null ) {
                DubboLinkedInt tempInt = parentInt.left;
                parentInt.left = leftInt;
                leftInt.left = tempInt;
            }
            recurrentLink(parentNode.left , leftInt);
        }
        if( parentNode.right != null ){
            DubboLinkedInt rightInt = new DubboLinkedInt();
            rightInt.value = parentNode.right.value;
            rightInt.left = parentInt;
            if(parentInt.right != null) {
                DubboLinkedInt tempInt = parentInt.right;
                parentInt.right = rightInt;
                rightInt.right = tempInt;
            }
            recurrentLink(parentNode.right, rightInt);
        }
    }

    static class Node {
        int value;

        Node left;

        Node right;
    }
}
