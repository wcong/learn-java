package org.wcong.test.algorithm.jzoffer.linked_list;

import org.springframework.util.Assert;
import org.wcong.test.algorithm.jzoffer.util.BuildLinkedList;
import org.wcong.test.algorithm.jzoffer.util.LinkedListNode;

/**
 * test for linked list
 * give to linked list find the first common node
 * Created by wcong on 2017/3/28.
 */
public class CommonNodeInTwoLinkedList {

    public static void main(String[] args) {
        Assert.isTrue(commmoNode(BuildLinkedList.makeOne(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9}).root, BuildLinkedList.makeOne(new int[]{11, 12, 12, 14, 5, 6, 7, 8, 9}).root) == 5);
    }

    public static int commmoNode(LinkedListNode list1, LinkedListNode list2) {
        if (list1 == null || list2 == null) {
            throw new RuntimeException("wrong param");
        }
        int list1Length = 1, list2Length = 1;
        LinkedListNode node = list1;
        while (node.next != null) {
            list1Length += 1;
            node = node.next;
        }
        node = list2;
        while (node.next != null) {
            list2Length += 1;
            node = node.next;
        }
        LinkedListNode startNode1 = list1;
        LinkedListNode startNode2 = list2;
        if (list1Length > list2Length) {
            int length = list1Length - list2Length;
            for (int i = 0; i < length; i++) {
                startNode1 = startNode1.next;
            }
        } else if (list1Length < list2Length) {
            int length = list2Length - list1Length;
            for (int i = 0; i < length; i++) {
                startNode2 = startNode2.next;
            }
        }
        while (true) {
            if (startNode1.value == startNode2.value) {
                return startNode1.value;
            }
            startNode1 = startNode1.next;
            startNode2 = startNode2.next;
        }
    }

}
