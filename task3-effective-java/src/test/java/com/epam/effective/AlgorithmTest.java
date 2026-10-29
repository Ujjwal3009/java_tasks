package com.epam.effective;

import com.epam.effective.algo.BinaryTree;
import com.epam.effective.algo.InsertionSort;
import com.epam.effective.algo.IterativeBinarySearch;
import com.epam.effective.algo.MergeSort;
import com.epam.effective.algo.RecursiveBinarySearch;
import com.epam.effective.algo.SearchService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Comparator;
import java.util.List;

class AlgorithmTest {

    @Test
    void testMergeSort() {
        Integer[] array = {5, 2, 8, 1, 9, 3};
        new MergeSort<Integer>().sort(array, Comparator.naturalOrder());
        Assertions.assertArrayEquals(new Integer[]{1, 2, 3, 5, 8, 9}, array);
    }

    @Test
    void testInsertionSort() {
        Integer[] array = {12, 4, 5, 2, 9};
        new InsertionSort<Integer>().sort(array, Comparator.naturalOrder());
        Assertions.assertArrayEquals(new Integer[]{2, 4, 5, 9, 12}, array);
    }

    @Test
    void testIterativeBinarySearch() {
        Integer[] sorted = {10, 20, 30, 40, 50};
        var searcher = new IterativeBinarySearch<Integer>();
        Assertions.assertEquals(2, searcher.search(sorted, 30, Comparator.naturalOrder()));
        Assertions.assertEquals(-1, searcher.search(sorted, 99, Comparator.naturalOrder()));
    }

    @Test
    void testRecursiveBinarySearch() {
        Integer[] sorted = {10, 20, 30, 40, 50};
        var searcher = new RecursiveBinarySearch<Integer>();
        Assertions.assertEquals(0, searcher.search(sorted, 10, Comparator.naturalOrder()));
        Assertions.assertEquals(4, searcher.search(sorted, 50, Comparator.naturalOrder()));
        Assertions.assertEquals(-1, searcher.search(sorted, 5, Comparator.naturalOrder()));
    }

    @Test
    void testSearchServiceIntegration() {
        Integer[] array = {60, 10, 40, 20, 50, 30};
        var service = new SearchService<>(new MergeSort<Integer>(), new IterativeBinarySearch<Integer>());
        int idx = service.sortAndSearch(array, 40, Comparator.naturalOrder());
        Assertions.assertEquals(3, idx);
        Assertions.assertEquals(40, array[idx]);
    }

    @Test
    void testBinaryTreeTraversals() {
        var tree = new BinaryTree<Integer>();
        // Tree structure:
        //        5
        //       / \
        //      3   7
        //     / \   \
        //    2   4   8
        tree.insert(5);
        tree.insert(3);
        tree.insert(7);
        tree.insert(2);
        tree.insert(4);
        tree.insert(8);

        Assertions.assertEquals(List.of(2, 3, 4, 5, 7, 8), tree.inOrder());
        Assertions.assertEquals(List.of(5, 3, 2, 4, 7, 8), tree.preOrder());
        Assertions.assertEquals(List.of(2, 4, 3, 8, 7, 5), tree.postOrder());
        Assertions.assertEquals(List.of(5, 3, 7, 2, 4, 8), tree.levelOrder());
    }
}
