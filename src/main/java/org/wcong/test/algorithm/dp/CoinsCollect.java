package org.wcong.test.algorithm.dp;

/**
 * give a m*n matrix board each cell is 0 cell or 1 cell,
 * a robot collect coin from left up
 * calculate the max coin number when he arrive the right bottom
 *
 * @author wcong<wc19920415@gmail.com>
 * @since 2017/4/9
 */
public class CoinsCollect {

	public static void main(String[] args) {

	}

	public static int maxCoins(int[][] board) {
		int[][] result = new int[board.length + 1][board[0].length + 1];
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				result[i + 1][j + 1] = Math.max(result[i][j + 1], result[i + 1][j]) + board[i][j];
			}
		}
		return result[board.length][board[0].length];
	}

}
