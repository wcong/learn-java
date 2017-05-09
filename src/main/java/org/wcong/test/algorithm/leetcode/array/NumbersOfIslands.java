package org.wcong.test.algorithm.leetcode.array;

/**
 * Given a 2d grid map of '1's (land) and '0's (water), count the number of islands.
 * An island is surrounded by water and is formed by connecting adjacent lands horizontally or vertically.
 * You may assume all four edges of the grid are all surrounded by water.
 *
 * @author wcong<wc19920415@gmail.com>
 * @since 09/05/2017
 */
public class NumbersOfIslands {
	public int numIslandsBruteForce(char[][] grid) {
		int nums = 0;
		for (int row = 0; row < grid.length; row++) {
			for (int column = 0; column < grid[row].length; column++) {
				if (grid[row][column] == '1') {
					nums += 1;
					elimateLand(grid, row, column);
				}
			}
		}
		return nums;
	}

	private void elimateLand(char[][] grid, int row, int column) {
		if (grid[row][column] == '0') {
			return;
		}
		grid[row][column] = '0';
		if (row > 0) {
			elimateLand(grid, row - 1, column);
		}
		if (column > 0) {
			elimateLand(grid, row, column - 1);
		}
		if (row < grid.length - 1) {
			elimateLand(grid, row + 1, column);
		}
		if (column < grid[row].length - 1) {
			elimateLand(grid, row, column + 1);
		}
	}

}
