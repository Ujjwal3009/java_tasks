package com.epam.effective.algo;

import java.util.Comparator;

public interface BinarySearcher<T> {
    int search(T[] array, T key, Comparator<? super T> comparator);
}
