package org.wcong.test.algorithm.dp;

/**
 * Consider the problem of neatly printing a paragraph with a monospaced font (all
 * characters having the same width) on a printer.
 * The input text is a sequence of n words of lengths l1; l2; : : : ; ln, measured in characters.
 * We want to print this paragraph neatly on a number of lines that hold a maximum of M characters each.
 * Our criterion of “neatness” is as follows. If a given line contains words i through j ,
 * where i <= j , and we leave exactly one space between words, the number of extra
 * We wish to minimize the left space to right.
 * Created by wcong on 2017/3/31.
 */
public class PrintNearly {

    public static void main(String[] args) {

    }

    public static int paintNum(int[] array, int m) {
        int[][] result = new int[array.length + 1][array.length + 1];
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j <= i; j++) {
                if (array[i] + result[i][j] <= m) {
                    result[i + 1][j + 1] = result[i][j + 1] + array[i];
                } else {
                    result[i + 1][j + 1] = result[i - 1][j] > result[i][j - 1] ? result[i - 1][j] : result[i][j - 1];
                }
            }
        }
        return result[array.length][array.length];
    }

}
