package org.wcong.test.algorithm;

/**
 * a*b,b*c,c*d,d*e
 *
 * Created by hzwangcong on 2017/2/5.
 */
public class HeapSort {

    public static void main(String[] args) {

    }

    static void maxHeapify(int[] array, int top) {
        if (top >= array.length) {
            return;
        }
        int left = (top + 1) * 2 - 1;
        int right = (top + 1) * 2;
        int large = top;
        if (left < array.length && array[left] > array[large]) {
            large = left;
        }
        if (right < array.length && array[right] > array[large]) {
            large = right;
        }
        if (large != top) {
            int temp = array[top];
            array[top] = array[large];
            array[large] = temp;
            maxHeapify(array, large);
        }
    }

}
