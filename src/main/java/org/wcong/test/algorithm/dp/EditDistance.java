package org.wcong.test.algorithm.dp;

/**
 * give two strings a,b calculate the distance for b transform to b
 * use
 * add insert one char
 * delete delete one char
 * replace replace one char
 * return the minimum distance
 * Created by wcong on 2017/3/31.
 */
public class EditDistance {

    public static void main() {
    }

    public static int mininumEditDistance(String a, String b) {
        int[][] result = new int[a.length()][b.length()];
        for (int i = 0; i < a.length(); i++) {
            for (int j = 0; j < b.length(); j++) {
                if (a.charAt(i) == b.charAt(j)) {
                    result[i + 1][j + 1] = result[i][j] + 1;
                } else {
                    result[i + 1][j + 1] = 1 + Math.min(Math.min(result[i + 1][j], result[i][j + 1]), result[i][j]);
                }
            }
        }
        return result[a.length()][b.length()];
    }

}
