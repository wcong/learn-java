package org.wcong.test.algorithm.jzoffer;

import org.wcong.test.algorithm.jzoffer.util.ComplexListNode;

/**
 * test for linked list
 * give a complex linked list copy it
 *
 * @author wcong<wc19920415@gmail.com>
 * @since 2017/3/26
 */
public class ComplexListNodeCopy {

    public static ComplexListNode copyComplexList(ComplexListNode head) {
        if (head == null) {
            return null;
        }
        ComplexListNode node = head;
        while (node != null) {
            ComplexListNode copy = new ComplexListNode();
            copy.value = node.value;
            copy.next = node.next;
            node.next = copy;
            node = copy.next;
        }
        node = head;
        while (node != null) {
            ComplexListNode copy = node.next;
            if (node.other != null) {
                copy.other = node.other.next;
            }
            node = copy.next;
        }
        node = head;
        ComplexListNode root = head.next;
        while (node != null) {
            ComplexListNode copy = node.next;
            node.next = copy.next;
            node = copy.next;
            if( copy.next != null ) {
                copy.next = copy.next.next;
            }
        }
        return root;
    }

}
