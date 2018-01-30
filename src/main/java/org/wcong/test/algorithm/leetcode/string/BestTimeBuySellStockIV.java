package org.wcong.test.algorithm.leetcode.string;

/**
 * Say you have an array for which the ith element is the price of a given stock on day i.
 * Design an algorithm to find the maximum profit.
 * You may complete at most k transactions.
 * Note:
 * You may not engage in multiple transactions at the same time
 * (ie, you must sell the stock before you buy again).
 *
 * @author wcong<wc19920415@gmail.com>
 * @since 08/06/2017
 */
public class BestTimeBuySellStockIV {

    private Integer maxProfit;

    public int maxProfitBruteForce(int k, int[] prices) {
        maxProfit = 0;
        iterateOptions(k, 0, prices, false, 0, 0);
        return maxProfit;
    }

    private void iterateOptions(int transactions, int index, int[] prices, boolean isBuy, int currentPrice, int currentProfit) {
        if (index >= prices.length || transactions <= 0) {
            if (currentProfit > maxProfit) {
                maxProfit = currentProfit;
            }
            return;
        }
        if (!isBuy) {
            iterateOptions(transactions, index + 1, prices, true, prices[index], currentProfit);
            iterateOptions(transactions, index + 1, prices, false, currentPrice, currentProfit);
        } else {
            iterateOptions(transactions - 1, index + 1, prices, false, prices[index], currentProfit + prices[index] - currentPrice);
            iterateOptions(transactions, index + 1, prices, true, currentPrice, currentProfit);
        }
    }


    static class Result {
        int profit = 0;
        int transactions = 0;
    }

    public int maxProfitDp(int k, int[] prices) {
        if (k <= 0 || prices.length == 0) {
            return 0;
        }
        Result[] profits = new Result[prices.length];
        profits[0] = new Result();
        for (int i = 1; i < prices.length; i++) {
            Result result = new Result();
            result.profit = prices[i] - prices[0];
            result.transactions = 1;
            if (result.profit < 0) {
                result.profit = 0;
                result.transactions = 0;
            }
            for (int j = 1; j < i; j++) {
                if (profits[j - 1].transactions + 1 > k) {
                    continue;
                }
                int profit = profits[j - 1].profit + prices[i] - prices[j];
                if (profit > result.profit) {
                    result.profit = profit;
                    result.transactions = profits[j - 1].transactions + 1;
                }
            }
            if (profits[i - 1].profit > result.profit) {
                result.profit = profits[i - 1].profit;
                result.transactions = profits[i - 1].transactions;
            }
            profits[i] = result;
        }
        return profits[prices.length - 1].profit;
    }
}
