package org.wcong.test.algorithm.leetcode.array;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Given n points on a 2D plane
 * find the maximum number of points that lie on the same straight line.
 *
 * @author wcong<wc19920415@gmail.com>
 * @since 10/05/2017
 */
public class MaxPointsOnALine {

	public int maxPointsBruteForce(Point[] points) {
		if (points == null) {
			return 0;
		}
		if (points.length < 2) {
			return points.length;
		}
		int maxPoints = 0;
		for (int firstIndex = 0; firstIndex < points.length; firstIndex++) {
			for (int secondIndex = firstIndex + 1; secondIndex < points.length; secondIndex++) {
				Point firstPoint = points[firstIndex];
				Point secondPoint = points[secondIndex];
				double angel = 0;
				if (firstPoint.y != secondPoint.y) {
					angel = firstPoint.x > secondPoint.x ?
							(firstPoint.x - secondPoint.x) / (firstPoint.y - secondPoint.y) :
							(secondPoint.x - firstPoint.x) / (secondPoint.y - firstPoint.y);
				}
				int pointsNum = 2;
				for (int leftIndex = secondIndex + 1; leftIndex < points.length; leftIndex++) {
					Point nextPoint = points[leftIndex];
					if (firstPoint.y == nextPoint.y) {
						if (firstPoint.y == secondPoint.y) {
							pointsNum += 1;
						}
						continue;
					}
					double nextAngel = firstPoint.x > nextPoint.x ?
							(firstPoint.x - nextPoint.x) / (firstPoint.y - nextPoint.y) :
							(nextPoint.x - firstPoint.x) / (nextPoint.y - firstPoint.y);
					if (angel == nextAngel) {
						pointsNum += 1;
					}
				}
				if (pointsNum > maxPoints) {
					maxPoints = pointsNum;
				}
			}
		}
		return maxPoints;
	}

	static class MyPoint {
		Point point;

		String key;

		int count;
	}

	public int maxPointsDp(Point[] points) {
		if (points == null) {
			return 0;
		}
		if (points.length < 2) {
			return points.length;
		}
		Map<String, MyPoint> pointCount = new HashMap<>();
		for (Point point : points) {
			String key = point.x + "_" + point.y;
			if (pointCount.containsKey(key)) {
				pointCount.get(key).count += 1;
			} else {
				MyPoint myPoint = new MyPoint();
				myPoint.point = point;
				myPoint.key = key;
				myPoint.count = 1;
				pointCount.put(key, myPoint);
			}
		}
		List<MyPoint> myPointList = new ArrayList<>(pointCount.values());
		if( myPointList.size()==1 ){
			return myPointList.get(0).count;
		}
		Set<String> usedPoints = new HashSet<>();
		int maxPoints = 0;
		for (int firstIndex = 0; firstIndex < myPointList.size(); firstIndex++) {
			for (int secondIndex = firstIndex + 1; secondIndex < myPointList.size(); secondIndex++) {
				MyPoint firstPoint = myPointList.get(firstIndex);
				MyPoint secondPoint = myPointList.get(secondIndex);
				if (usedPoints.contains(firstIndex + "_" + secondIndex)) {
					continue;
				}
				double angel = 0;
				if (firstPoint.point.y != secondPoint.point.y) {
					angel = firstPoint.point.x > secondPoint.point.x ?
							(firstPoint.point.x - secondPoint.point.x) / (double)(firstPoint.point.y - secondPoint.point.y) :
							(secondPoint.point.x - firstPoint.point.x) / (double)(secondPoint.point.y - firstPoint.point.y);
				}
				int pointsNum = firstPoint.count + secondPoint.count;
				List<Integer> sameIndex = new LinkedList<>();
				for (int leftIndex = secondIndex + 1; leftIndex < myPointList.size(); leftIndex++) {
					if (usedPoints.contains(secondIndex + "_" + leftIndex)) {
						continue;
					}
					MyPoint nextPoint = myPointList.get(leftIndex);
					if (firstPoint.point.y == nextPoint.point.y) {
						if (firstPoint.point.y == secondPoint.point.y) {
							for (Integer lastIndex : sameIndex) {
								usedPoints.add(lastIndex + "_" + leftIndex);
							}
							sameIndex.add(leftIndex);
							pointsNum += nextPoint.count;
						}
						continue;
					}
					double nextAngel = firstPoint.point.x > nextPoint.point.x ?
							(firstPoint.point.x - nextPoint.point.x) / (double)(firstPoint.point.y - nextPoint.point.y) :
							(nextPoint.point.x - firstPoint.point.x) / (double)(nextPoint.point.y - firstPoint.point.y);
					if (angel == nextAngel) {
						for (Integer lastIndex : sameIndex) {
							usedPoints.add(lastIndex + "_" + leftIndex);
						}
						sameIndex.add(leftIndex);
						pointsNum += nextPoint.count;
					}
				}

				if (pointsNum > maxPoints) {
					maxPoints = pointsNum;
				}
			}
		}
		return maxPoints;
	}

	public static class Point {
		int x;

		int y;

		Point() {
			x = 0;
			y = 0;
		}

		public Point(int a, int b) {
			x = a;
			y = b;
		}
	}

}
