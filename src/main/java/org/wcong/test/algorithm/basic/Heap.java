package org.wcong.test.algorithm.basic;

import java.util.Arrays;

/**
 * a heap data structure
 * 1 property max-heap there is max-heap property and min-heap there is min-heap property
 * 2 operation
 * 1. max-heapify : give a index,assume it's children is all satisfied ,make sure index is satisfied
 * 2. build max-heap : make self a max-heap
 * 3. heap-sort
 * 4. max-heap-insert
 * 5. heap-extract-max
 * 6. heap-increase-key
 * 7. heap-maximum
 * Created by wcong on 2017/3/29.
 */
public class Heap {

    private int[] array;

    public Heap(int[] array) {
        assert array != null;
        this.array = Arrays.copyOf(array, array.length);
    }

    /**
     * initialization:priory to the first iteration of loop,i=[n/2],each node[n/2]+1,[n/2]+2,n is a leaf and thus the root
     */
    public void buildMaxHeap() {
        if (array == null || array.length <= 1) {
            return;
        }
        int end = array.length / 2;
        for (int i = end - 1; i >= 0; i++) {
            maxHeapify(i, array.length - 1);
        }
    }

    public int maximun() {
        return array[0];
    }

    public int heapExtractMax() {
        return 0;
    }

    public void maxHeapInsert(int add) {
        int[] newArray = new int[array.length + 1];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = array[i];
        }
        heapIncreaseKey(newArray.length - 1, add);
    }


    public void heapIncreaseKey(int index, int k) {
        array[index] = k;
        while (index > 0) {
            int parent = (index + 1) / 2 - 1;
            if (array[parent] < array[index]) {
                int temp = array[parent];
                array[parent] = array[index];
                array[index] = temp;
                index = parent;
            } else {
                break;
            }
        }
    }

    public void maxHeapify(int index, int length) {
        int left = index * 2 + 1;
        int right = left + 1;
        int maxIndex = index;
        if (left <= length && array[left] > array[maxIndex]) {
            maxIndex = left;
        }
        if (right <= length && array[right] > array[maxIndex]) {
            maxIndex = right;
        }
        if (maxIndex == index) {
            return;
        }
        int temp = array[index];
        array[index] = array[maxIndex];
        array[maxIndex] = temp;
        maxHeapify(maxIndex, length);
    }

    public static void heapSort(int[] array) {
        Heap heap = new Heap(array);
        heap.buildMaxHeap();
        int end = array.length - 1;
        while (end > 0) {
            int temp = array[0];
            array[0] = array[end];
            array[end] = temp;
            end -= 1;
            heap.maxHeapify(0, end);
        }
    }


}
