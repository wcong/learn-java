package org.wcong.test.algorithm.jzoffer;

import org.springframework.util.Assert;

import java.util.Stack;

/**
 * test for stack and queue
 * implement a queue bu two stack
 * Created by wcong on 2017/3/24.
 */
public class TwoStackForQueue {


    public static void main(String[] args) {
        StackQueue stackQueue = new StackQueue();
        stackQueue.push(1);
        stackQueue.push(2);
        stackQueue.push(3);
        stackQueue.push(4);
        Assert.isTrue(stackQueue.pop() == 1);
        Assert.isTrue(stackQueue.pop() == 2);
        Assert.isTrue(stackQueue.pop() == 3);
        Assert.isTrue(stackQueue.pop() == 4);
    }


    public static class StackQueue {

        private Stack<Integer> in = new Stack<>();

        private Stack<Integer> out = new Stack<>();

        public void push(int num) {
            in.push(num);
        }

        public int pop() {
            if (out.isEmpty()) {
                while (!in.isEmpty()) {
                    out.push(in.pop());
                }
            }
            return out.pop();
        }
    }

}
