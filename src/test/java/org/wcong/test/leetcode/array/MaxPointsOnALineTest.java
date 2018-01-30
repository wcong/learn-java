package org.wcong.test.leetcode.array;

import org.junit.Assert;
import org.junit.Test;
import org.wcong.test.algorithm.leetcode.array.MaxPointsOnALine;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 10/05/2017
 */
public class MaxPointsOnALineTest {

	@Test
	public void testMaxPoints() {
		MaxPointsOnALine maxPointsOnALine = new MaxPointsOnALine();
		MaxPointsOnALine.Point[] points = new MaxPointsOnALine.Point[] { new MaxPointsOnALine.Point(0, 0),
				new MaxPointsOnALine.Point(1, 1), new MaxPointsOnALine.Point(2, 2), new MaxPointsOnALine.Point(1, 0),
				new MaxPointsOnALine.Point(2, 0) };
		MaxPointsOnALine.Point[] points1 = new MaxPointsOnALine.Point[] { new MaxPointsOnALine.Point(0, 0),
				new MaxPointsOnALine.Point(1, 1), new MaxPointsOnALine.Point(0, 0) };
		MaxPointsOnALine.Point[] points2 = new MaxPointsOnALine.Point[] { new MaxPointsOnALine.Point(1, 1),new MaxPointsOnALine.Point(1, 1),
				new MaxPointsOnALine.Point(2, 2) , new MaxPointsOnALine.Point(2, 2) };
		//[-4,1],[-7,7],[-1,5],[9,-25]
		MaxPointsOnALine.Point[] points3 = new MaxPointsOnALine.Point[] { new MaxPointsOnALine.Point(-4, 1),new MaxPointsOnALine.Point(-7, 7),
				new MaxPointsOnALine.Point(-1, 5) , new MaxPointsOnALine.Point(9, -25) };
		Assert.assertTrue(maxPointsOnALine.maxPointsBruteForce(points) == 3);
		Assert.assertTrue(maxPointsOnALine.maxPointsDp(points) == 3);
		Assert.assertTrue(maxPointsOnALine.maxPointsDp(points1) == 3);
		Assert.assertTrue(maxPointsOnALine.maxPointsDp(points2) == 4);
		Assert.assertTrue(maxPointsOnALine.maxPointsDp(points3) == 3);
	}

}
