package org.wcong.test.algorithm.jzoffer;

import org.springframework.util.Assert;

/**
 * test for array
 * in a sorted tow dimension array,which is increasing from left to right,top to bottom.
 * is there a target in that array.
 * Created by wcong on 2017/3/23.
 */
public class FindInTwoDimension {

    public static void main(String[] args) {
        int[][] findMatrix = new int[][]{{1, 4, 7, 10, 13}, {2, 5, 8, 16, 17}, {3, 6, 9, 12, 15}};
        Assert.isTrue(findInTwoDimensionLogN(findMatrix, 5));
        Assert.isTrue(!findInTwoDimensionLogN(findMatrix, 11));
        Assert.isTrue(!findInTwoDimensionLogN(null, 11));
    }

    public static boolean findInDimensionBigON(int[][] matrix, int target) {
        if (matrix == null) {
            return false;
        }
        for (int i = 0; i < matrix.length; i++) {
            int start = 0;
            int end = matrix[i].length;
            while (start <= end) {
                int middle = start + (end - start) / 2;
                if (matrix[i][middle] == target) {
                    return true;
                } else if (matrix[i][middle] < target) {
                    start = middle + 1;
                } else {
                    end = middle - 1;
                }
            }
        }
        return false;
    }

    public static boolean findInTwoDimensionLogN(int[][] matrix, int target) {
        if (matrix == null || matrix.length <= 0) {
            return false;
        }
        return findInTwoDimensionSub(matrix, 0, matrix[0].length - 1, target);
    }

    public static boolean findInTwoDimensionSub(int[][] matrix, int row, int column, int target) {
        if (row >= matrix.length) {
            return false;
        }
        int startColumn = 0;
        int endColumn = column;
        while (startColumn <= endColumn) {
            int middle = startColumn + (endColumn - startColumn) / 2;
            if (matrix[row][middle] == target) {
                return true;
            } else if (matrix[row][middle] > target) {
                endColumn = middle - 1;
            } else {
                startColumn = middle + 1;
            }
        }
        int startRow = row;
        int endRow = matrix.length - 1;
        while (startRow <= endRow) {
            int middle = startRow + (endRow - startRow) / 2;
            if (matrix[middle][endColumn] == target) {
                return true;
            } else if (matrix[middle][endColumn] < target) {
                startRow = middle + 1;
            } else {
                endRow = middle - 1;
            }
        }
        return findInTwoDimensionSub(matrix, endRow + 1, endColumn - 1, target);
    }

}
