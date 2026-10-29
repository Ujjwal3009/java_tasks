package com.epam.effective.algo;

import java.util.Comparator;

public class IterativeBinarySearch<T> implements BinarySearcher<T> {

    @Override
    public int search(T[] array, T key, Comparator<? super T> comparator) {
        if (array == null || key == null) {
            return -1;
        }
        var low = 0;
        var high = array.length - 1;

        while (low <= high) {
            var mid = low + (high - low) / 2;
            var cmp = comparator.compare(array[mid], key);

            if (cmp == 0) {
                return mid;
            } else if (cmp < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return -1;
    }
}
