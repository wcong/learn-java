package org.wcong.test.algorithm.jzoffer;

/**
 * test for bit operation and array
 * give a array two of the numbers appears only once others all appears twice,find the tow number
 * Created by wcong on 2017/3/28.
 */
public class TwoDifferentNumInArray {

    public static void main(String[] args) {
        twoNumbers(new int[]{1, 2, 3, 4, 5, 6, 4, 3, 2, 5});
    }


    public static int[] twoNumbers(int[] array) {
        if (array == null || array.length < 2) {
            return null;
        }
        int result = 0;
        for (int num : array) {
            result ^= num;
        }
        int bitIndex = 0;
        while (result != 0) {
            if ((result & 1) != 0) {
                break;
            }
            result = result >> 1;
            bitIndex += 1;
        }
        int num1 = 0;
        int num2 = 0;
        for (int num : array) {
            if (((num >> bitIndex) & 1) == 0) {
                num1 ^= num;
            } else {
                num2 ^= num;
            }
        }
        return new int[]{num1, num2};
    }

}
