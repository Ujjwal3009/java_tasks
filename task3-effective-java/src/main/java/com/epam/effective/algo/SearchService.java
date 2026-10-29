package com.epam.effective.algo;

import java.util.Comparator;
import java.util.Objects;

public class SearchService<T> {

    private final Sorter<T> sorter;
    private final BinarySearcher<T> binarySearcher;

    public SearchService(Sorter<T> sorter, BinarySearcher<T> binarySearcher) {
        this.sorter = Objects.requireNonNull(sorter, "sorter cannot be null");
        this.binarySearcher = Objects.requireNonNull(binarySearcher, "binarySearcher cannot be null");
    }

    public int sortAndSearch(T[] array, T key, Comparator<? super T> comparator) {
        if (array == null || key == null) {
            return -1;
        }
        sorter.sort(array, comparator);
        return binarySearcher.search(array, key, comparator);
    }
}
