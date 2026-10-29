package com.epam.effective.algo;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;

public class MergeSort<T> implements Sorter<T> {

    @Override
    public void sort(T[] array, Comparator<? super T> comparator) {
        if (array == null || array.length <= 1) {
            return;
        }
        @SuppressWarnings("unchecked")
        T[] auxiliary = (T[]) Array.newInstance(array.getClass().getComponentType(), array.length);
        mergeSort(array, auxiliary, 0, array.length - 1, comparator);
    }

    private void mergeSort(T[] array, T[] aux, int low, int high, Comparator<? super T> comparator) {
        if (low >= high) {
            return;
        }
        var mid = low + (high - low) / 2;
        mergeSort(array, aux, low, mid, comparator);
        mergeSort(array, aux, mid + 1, high, comparator);
        merge(array, aux, low, mid, high, comparator);
    }

    private void merge(T[] array, T[] aux, int low, int mid, int high, Comparator<? super T> comparator) {
        System.arraycopy(array, low, aux, low, high - low + 1);

        var i = low;
        var j = mid + 1;
        var k = low;

        while (i <= mid && j <= high) {
            if (comparator.compare(aux[i], aux[j]) <= 0) {
                array[k++] = aux[i++];
            } else {
                array[k++] = aux[j++];
            }
        }
        while (i <= mid) {
            array[k++] = aux[i++];
        }
        while (j <= high) {
            array[k++] = aux[j++];
        }
    }
}
