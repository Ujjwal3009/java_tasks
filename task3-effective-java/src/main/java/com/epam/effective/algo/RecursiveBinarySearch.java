package com.epam.effective.algo;

import java.util.Comparator;

public class RecursiveBinarySearch<T> implements BinarySearcher<T> {

    @Override
    public int search(T[] array, T key, Comparator<? super T> comparator) {
        if (array == null || key == null) {
            return -1;
        }
        return search(array, key, 0, array.length - 1, comparator);
    }

    private int search(T[] array, T key, int low, int high, Comparator<? super T> comparator) {
        if (low > high) {
            return -1;
        }
        var mid = low + (high - low) / 2;
        var cmp = comparator.compare(array[mid], key);

        if (cmp == 0) {
            return mid;
        } else if (cmp < 0) {
            return search(array, key, mid + 1, high, comparator);
        } else {
            return search(array, key, low, mid - 1, comparator);
        }
    }
}
