package org.wcong.test.algorithm.cracking.dp_divide_conque;

import java.util.LinkedList;
import java.util.List;

/**
 * imagine a robot sitting on the upper left corner of grid with r rows,and c columns
 * the robot can move in two directions,right and down
 * but certain cells are off limits such that robot cannot step on them
 * design an algorithm to find the path for the robot from top left to bottom right
 *
 * @author wcong<wc19920415@gmail.com>
 * @since 19/04/2017
 */
public class RobotInGrid {

	static class Step {
		int x;

		int y;

		public Step(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

	public List<Step> findPath(int[][] grid) {

		Step[][] step = new Step[grid.length][grid[0].length];
		step[0][grid[0].length - 1] = new Step(0, 0);
		for (int y = grid[0].length - 2; y >= 0; y--) {
			if (step[0][y + 1] != null && grid[0][y] == 1) {
				step[0][y] = new Step(0, y + 1);
			}
		}
		for (int x = 1; x < grid.length; x++) {
			if (step[x - 1][grid[0].length - 1] != null && grid[x][grid[0].length - 1] == 1) {
				step[x][grid[0].length - 1] = new Step(x - 1, grid[0].length - 1);
			}
		}
		for (int x = 1; x < grid.length; x++) {
			for (int y = grid[0].length - 2; y >= 0; y--) {
				if (grid[x][y] == 0) {
					continue;
				}
				if (step[x - 1][y] != null) {
					step[x][y] = new Step(x - 1, y);
				} else if (step[x][y + 1] != null) {
					step[x][y] = new Step(x, y + 1);
				}
			}
		}
		if (step[grid.length - 1][grid[0].length - 1] == null) {
			return null;
		}
		List<Step> findStep = new LinkedList<>();
		Step soloStep = step[grid.length - 1][grid[0].length - 1];
		while (soloStep != null && soloStep.x != 0 && soloStep.y != 0) {
			findStep.add(0, soloStep);
			soloStep = step[soloStep.x][soloStep.y];
		}
		return findStep;
	}
}
