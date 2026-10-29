package com.epam.effective.algo;

import java.util.Comparator;

public interface Sorter<T> {
    void sort(T[] array, Comparator<? super T> comparator);
}
