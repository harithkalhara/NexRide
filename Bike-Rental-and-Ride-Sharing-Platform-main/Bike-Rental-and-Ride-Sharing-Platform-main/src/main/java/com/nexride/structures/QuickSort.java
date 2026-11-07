package com.nexride.structures;

import java.util.Comparator;
import java.util.List;

public final class QuickSort {
    private QuickSort() {}

    public static <T> List<T> sorted(List<T> input, Comparator<? super T> order) {
        List<T> copy = new java.util.ArrayList<>(input);
        sort(copy, order);
        return copy;
    }

    public static <T> void sort(List<T> list, Comparator<? super T> order) {
        if (list.size() < 2) return;
        quickSort(list, 0, list.size() - 1, order);
    }

    private static <T> void quickSort(List<T> a, int low, int high, Comparator<? super T> order) {
        while (low < high) {
            int p = partition(a, low, high, order);
            if (p - low < high - p) {
                quickSort(a, low, p - 1, order);
                low = p + 1;
            } else {
                quickSort(a, p + 1, high, order);
                high = p - 1;
            }
        }
    }

    private static <T> int partition(List<T> a, int low, int high, Comparator<? super T> order) {
        int mid = low + (high - low) / 2;

        if (order.compare(a.get(mid), a.get(low)) < 0) swap(a, mid, low);
        if (order.compare(a.get(high), a.get(low)) < 0) swap(a, high, low);
        if (order.compare(a.get(high), a.get(mid)) < 0) swap(a, high, mid);
        swap(a, mid, high);

        T pivot = a.get(high);
        int i = low;
        for (int j = low; j < high; j++) {
            if (order.compare(a.get(j), pivot) < 0) {
                swap(a, i, j);
                i++;
            }
        }
        swap(a, i, high);
        return i;
    }

    private static <T> void swap(List<T> a, int i, int j) {
        if (i == j) return;
        T tmp = a.get(i);
        a.set(i, a.get(j));
        a.set(j, tmp);
    }
}
