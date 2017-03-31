package org.wcong.test.algorithm.dp;

/**
 * we are given a sequence of matrix to to multiplied
 * and we wish to computer the product
 * TODO
 * Created by wcong on 2017/3/30.
 */
public class MatrixChainMultiplication {

    static class MatrixSymbol {
        int row = 1;
        int column = 1;

        public MatrixSymbol mulitply(MatrixSymbol other) {
            MatrixSymbol matrixSymbol = new MatrixSymbol();
            matrixSymbol.row = row;
            matrixSymbol.column = other.column;
            return matrixSymbol;
        }
    }

    public static void main(String[] args) {

    }

    public static int multipiedNum(MatrixSymbol[] matrixArray) {
        int[] multipliedNum = new int[matrixArray.length + 1];
        MatrixSymbol[] multipliedMetrix = new MatrixSymbol[matrixArray.length + 1];
        for (int i = 1; i < matrixArray.length; i++) {
            int max = -1;
            int lastMultiplied = 0;
            MatrixSymbol lastMatrixSybmol = new MatrixSymbol();
            for (int j = i; j > 0; j--) {
                lastMultiplied += matrixArray[j].row;
            }
        }
        return 0;
    }
}
