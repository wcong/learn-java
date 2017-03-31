package org.wcong.test.algorithm.dp;

/**
 * given a string S with n characters and an array containing the break points, compute
 * the lowest cost for a sequence of breaks, along with a sequence of breaks that
 * achieves this cost.
 * length of 20 points 2,8,10
 * Created by wcong on 2017/3/31.
 */
public class BreakingAString {
    public static void main(String[] args) {

    }

    public static int minimumCost(String a, int[] point) {
        return minimumCost(a, 0, a.length() - 1, point, 0, point.length - 1);
    }

    public static int minimumCost(String a, int start, int end, int[] point, int pointStart, int pointEnd) {
        if (start >= end || pointStart >= pointEnd) {
            return 0;
        }
        int length = end - start + 1;
        int left = minimumCost(a, point[pointStart], end, point, pointStart + 1, pointEnd);
        int right = minimumCost(a, start, point[pointEnd], point, pointStart, pointEnd - 1);
        return left < right ? left + length : right + length;
    }


}
