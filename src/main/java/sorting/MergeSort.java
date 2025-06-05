package sorting;

import java.util.Arrays;

public class MergeSort {
    private int[] tempArray;


    /**
     * Sorts an array using the merge sort algorithm.
     *
     * @param array the array to be sorted (will be modified)
     */
    public void mergeSortImproved(int[] array) {
        // Protect against null
        if (array == null) {
            throw new IllegalArgumentException("Array cannot be null");
        }

        if (array.length <= 1) return;

        // use one temporary array
        tempArray = new int[array.length];
        mergeSortRecursive(array, 0, array.length - 1);
    }

    private void mergeSortRecursive(int[] array, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2; // prevent overflow
            mergeSortRecursive(array, left, mid);
            mergeSortRecursive(array, mid + 1, right);
            merge(array, left, mid, right);
        }
    }

    private void merge(int[] array, int left, int mid, int right) {
        // Copy to temp array
        System.arraycopy(array, left, tempArray, left, right - left + 1);

        int i = left;
        int j = mid + 1;
        int k = left;

        while (i <= mid && j <= right) {
            if (tempArray[i] <= tempArray[j]) {
                array[k++] = tempArray[i++];
            } else {
                array[k++] = tempArray[j++];
            }
        }

        while (i <= mid) {
            array[k++] = tempArray[i++];
        }
    }


    /**
     * Sorts an array using the merge sort algorithm.
     * Recursive approach.
     * Add optimization for small arrays:
     * use insertion sort for small arrays.
     *
     * @param array
     */
    public void mergeSort(int[] array) {
        if (array.length <= 1) return;

        // Add optimization for small arrays
        if (array.length <= 10) {  // threshold can be tuned
            insertionSort(array);
            return;
        }

        // divide array into two halves
        int mid = array.length / 2;
        // create two temporary arrays
        int[] left = new int[mid];
        int[] right = new int[array.length - mid];

        // copy data to temporary arrays
        System.arraycopy(array, 0, left, 0, mid);
        System.arraycopy(array, mid, right, 0, array.length - mid);

        // recursively sort the two halves
        mergeSort(left);
        mergeSort(right);

        //merge the sorted halves
        merge(left, right, array);
    }

    /**
     * Sorts an array using the insertion sort algorithm.
     * Insertion Sort performs better on small arrays due to less overhead.
     *
     * @param arr the array to be sorted (will be modified)
     */
    private void insertionSort(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            int key = arr[i];
            int j = i - 1;
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }

    }

    /**
     * Merges two sorted arrays into one sorted array.
     * Added check for already sorted arrays, no need to process them
     *
     * @param left
     * @param right
     * @param array
     */
    private void merge(int[] left, int[] right, int[] array) {

        int i = 0, j = 0;
        int k = 0;

        // Add check for already sorted arrays
        if (left[left.length - 1] <= right[0]) {
            System.arraycopy(left, 0, array, 0, left.length);
            System.arraycopy(right, 0, array, left.length, right.length);
            return;
        }

        while (i < left.length && j < right.length) {
            if (left[i] <= right[j]) {
                array[k++] = left[i++];
            } else {
                array[k++] = right[j++];
            }
        }

        // copy elements that left behind
        while (i < left.length) {
            array[k++] = left[i++];
        }

        while (j < right.length) {
            array[k++] = right[j++];
        }

    }

    public static void main(String[] args) {
//        int[] arr = new int[]{3, 5, 2, 1, 4, 9, 6};
        int[] arr1 = new int[]{1, 2, 3, 4, 5, 6, 7};
        int[] arr2 = new int[]{5, 3, 1, 4};
        MergeSort mergeSort = new MergeSort();
        mergeSort.mergeSort(arr1);

        System.out.println("Sorted array: " + Arrays.toString(arr1));

        mergeSort.mergeSortImproved(arr2);
        System.out.println("Sorted array: " + Arrays.toString(arr2));


    }

}
