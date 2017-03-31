package org.wcong.test.algorithm.dp;

/**
 * given a rod of length n inches and a table of n prices p[i] for i=1,2,3....
 * determine the maximum revenue r(n) by cutting up rod and selling prices
 * Created by wcong on 2017/3/30.
 */
public class RodCutting {

    public static void main(String[] args) {

    }

    public static int rodCutting(int[] array, int n) {
        int[] revenueArray = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            int max = array[i];
            for (int j = 1; j < i; j++) {
                int revenue = revenueArray[j] + revenueArray[i - j];
                if (revenue > max) {
                    max = revenue;
                }
            }
            revenueArray[i] = max;
        }
        return revenueArray[n];
    }

}
