package org.wcong.test.algorithm;

import org.eclipse.core.runtime.Assert;

/**
 * Given n non-negative integers a1, a2, ..., an,
 * where each represents a point at coordinate (i, ai).
 * n vertical lines are drawn such that the two endpoints of line i is at (i, ai) and (i, 0).
 * Find two lines, which together with x-axis forms a container, such that the container contains the most water.
 * Note: You may not slant the container and n is at least 2.
 * Created by hzwangcong on 2017/3/1.
 */
public class ContainerWithMostWater {

    public static void main(String[] args) {
        Assert.isTrue(maxArea(new int[]{1, 2}) == 1);
        Assert.isTrue(maxArea(new int[]{1, 2, 1}) == 2);
        Assert.isTrue(maxArea(new int[]{3, 2, 1, 3}) == 9);
    }

    /**
     * go to  next element
     * second height
     */
    public static int maxArea(int[] height) {
        int left = 0, right = height.length - 1;
        int maxArea = 0;

        while (left < right) {
            maxArea = Math.max(maxArea, Math.min(height[left], height[right])
                    * (right - left));
            if (height[left] < height[right])
                left++;
            else
                right--;
        }

        return maxArea;
    }

}
