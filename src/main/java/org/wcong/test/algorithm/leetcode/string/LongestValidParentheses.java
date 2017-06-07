package org.wcong.test.algorithm.leetcode.string;

import java.util.Stack;

/**
 * Given a string containing just the characters '(' and ')',
 * find the length of the longest valid (well-formed) parentheses substring.
 * For "(()", the longest valid parentheses substring is "()", which has length = 2.
 * Another example is ")()())", where the longest valid parentheses substring is "()()", which has length = 4.
 *
 * @author wcong<wc19920415@gmail.com>
 * @since 03/06/2017
 */
public class LongestValidParentheses {
    public int longestValidParenthesesBruteForce(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        int maxLength = 0;
        for (int start = 0; start < s.length(); start++) {
            for (int end = start + 1; end < s.length(); end++) {
                if (isValidParentheses(s, start, end)) {
                    int length = end - start + 1;
                    if (length > maxLength) {
                        maxLength = length;
                    }
                }
            }
        }
        return maxLength;
    }

    private boolean isValidParentheses(String s, int start, int end) {
        Stack<Character> characterStack = new Stack<>();
        for (int i = start; i <= end; i++) {
            if (s.charAt(i) == '(') {
                characterStack.push('(');
            } else if (s.charAt(i) == ')' && !characterStack.isEmpty() && characterStack.peek() == '(') {
                characterStack.pop();
            } else {
                return false;
            }
        }
        return characterStack.isEmpty();
    }

    public int longestValidParenthesesGreed(String s) {
        int maxLength = 0;
        int leftNum = 0;
        int rightNum = 0;
        int startIndex = 0;
        Stack<Integer> characterStack = new Stack<>();
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(') {
                characterStack.push(i);
                leftNum += 1;
            } else {
                if (characterStack.isEmpty()) {
                    leftNum = 0;
                    rightNum = 0;
                    startIndex = i + 1;
                } else {
                    characterStack.peek();
                    rightNum += 1;
                    characterStack.pop();
                    if (characterStack.isEmpty()) {
                        int length = i - startIndex + 1;
                        if (length > maxLength) {
                            maxLength = length;
                        }
                        leftNum = 0;
                        rightNum = 0;
                    }
                }
            }
        }
        if (leftNum >= rightNum && rightNum * 2 > maxLength) {
            maxLength = rightNum * 2;
        }
        return maxLength;
    }
}
