package com.epam.effective.algo;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class BinaryTree<T extends Comparable<T>> {

    public static class Node<T> {
        public T value;
        public Node<T> left;
        public Node<T> right;

        public Node(T value) {
            this.value = value;
        }
    }

    private Node<T> root;

    public void insert(T value) {
        if (value == null) return;
        root = insertRecursive(root, value);
    }

    private Node<T> insertRecursive(Node<T> current, T value) {
        if (current == null) {
            return new Node<>(value);
        }
        if (value.compareTo(current.value) < 0) {
            current.left = insertRecursive(current.left, value);
        } else if (value.compareTo(current.value) > 0) {
            current.right = insertRecursive(current.right, value);
        }
        return current;
    }

    public List<T> inOrder() {
        List<T> result = new ArrayList<>();
        inOrderTraversal(root, result);
        return result;
    }

    private void inOrderTraversal(Node<T> node, List<T> result) {
        if (node != null) {
            inOrderTraversal(node.left, result);
            result.add(node.value);
            inOrderTraversal(node.right, result);
        }
    }

    public List<T> preOrder() {
        List<T> result = new ArrayList<>();
        preOrderTraversal(root, result);
        return result;
    }

    private void preOrderTraversal(Node<T> node, List<T> result) {
        if (node != null) {
            result.add(node.value);
            preOrderTraversal(node.left, result);
            preOrderTraversal(node.right, result);
        }
    }

    public List<T> postOrder() {
        List<T> result = new ArrayList<>();
        postOrderTraversal(root, result);
        return result;
    }

    private void postOrderTraversal(Node<T> node, List<T> result) {
        if (node != null) {
            postOrderTraversal(node.left, result);
            postOrderTraversal(node.right, result);
            result.add(node.value);
        }
    }

    public List<T> levelOrder() {
        List<T> result = new ArrayList<>();
        if (root == null) return result;

        Queue<Node<T>> queue = new ArrayDeque<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            var current = queue.poll();
            result.add(current.value);
            if (current.left != null) queue.add(current.left);
            if (current.right != null) queue.add(current.right);
        }
        return result;
    }
}
