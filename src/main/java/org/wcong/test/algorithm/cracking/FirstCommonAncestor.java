package org.wcong.test.algorithm.cracking;

import org.wcong.test.algorithm.tree.BinaryTree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by wcong on 2017/4/14.
 */
public class FirstCommonAncestor {

    public static void main(String[] args) {

    }

    public static BinaryTree.Node firstCommonAncestor(BinaryTree.Node root, BinaryTree.Node target1, BinaryTree.Node target2) {
        List<List<BinaryTree.Node>> resultList = new ArrayList<>(2);
        List<BinaryTree.Node> trace = new LinkedList<>();
        firstCommonAncestor(root, target1, target2, resultList, trace);
        List<BinaryTree.Node> pathOne = resultList.get(0);
        List<BinaryTree.Node> pathTwo = resultList.get(1);
        return null;
    }

    private static void firstCommonAncestor(BinaryTree.Node root,
                                            BinaryTree.Node target1,
                                            BinaryTree.Node target2,
                                            List<List<BinaryTree.Node>> resultList,
                                            List<BinaryTree.Node> trace) {
        if (resultList.size() == 2) {
            return;
        }
        trace.add(root);
        if (target1.key == root.key) {
            resultList.add(new LinkedList<>(trace));
        } else if (target2.key == root.key) {
            resultList.add(new LinkedList<>(trace));
        }
        if (root.left != null) {
            firstCommonAncestor(root.left, target1, target2, resultList, trace);
        }
        if (root.right != null) {
            firstCommonAncestor(root.right, target1, target2, resultList, trace);
        }
        trace.remove(trace.size() - 1);
    }

}
