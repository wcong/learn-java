package org.wcong.test.algorithm.leetcode.array;

/**
 * There are N children standing in a line. Each child is assigned a rating value.
 * You are giving candies to these children subjected to the following requirements:
 * 1. Each child must have at least one candy.
 * 2. Children with a higher rating get more candies than their neighbors.
 * What is the minimum candies you must give?
 *
 * @author wcong<wc19920415@gmail.com>
 * @since 19/06/2017
 */
public class Candy {
    public int candy(int[] ratings) {
        int candies = 1;
        int[] candy = new int[ratings.length];
        candy[0] = 1;
        for (int i = 1; i < ratings.length; i++) {
            if (ratings[i] > ratings[i - 1]) {
                candy[i] = candy[i - 1] + 1;
                candies += candy[i];
            } else if (ratings[i] < ratings[i - 1]) {
                int lastIndex = i;
                int lastRating = ratings[i];
                while (lastIndex + 1 < ratings.length && lastRating > ratings[lastIndex + 1]) {
                    lastIndex += 1;
                    lastRating = ratings[lastIndex];
                }
                int num = 1;
                for (int j = lastIndex; j >= i; j--) {
                    candy[j] = num;
                    candies += num;
                    num += 1;
                }
                if (candy[i - 1] < num) {
                    candies += num - candy[i - 1];
                    candy[i - 1] = num;
                }
                i = lastIndex;
            } else {
                candy[i] = 1;
                candies += 1;
            }
        }
        return candies;
    }
}
