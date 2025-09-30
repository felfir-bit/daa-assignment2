package algorithms;

import metrics.PerformanceTracker;

/**
 * Оптимизированная сортировка вставками с бинарным поиском
 */
public class InsertionSort {
    private PerformanceTracker tracker;

    public InsertionSort() {
        this.tracker = new PerformanceTracker();
    }

    public InsertionSort(PerformanceTracker tracker) {
        this.tracker = tracker;
    }

    public int[] sort(int[] array) {
        if (array == null) {
            tracker.incrementComparisons();
            throw new IllegalArgumentException("Input array cannot be null");
        }

        if (array.length <= 1) {
            tracker.incrementComparisons();
            return array.clone();
        }

        int[] result = array.clone();
        tracker.recordMemoryAllocation(result.length * 4);

        if (isAlmostSorted(result)) {
            return optimizedSortForAlmostSorted(result);
        } else {
            return standardInsertionSort(result);
        }
    }

    private boolean isAlmostSorted(int[] array) {
        int disorderCount = 0;
        for (int i = 0; i < array.length - 1; i++) {
            tracker.incrementComparisons();
            tracker.incrementArrayAccesses(2);
            if (array[i] > array[i + 1]) {
                disorderCount++;
                if (disorderCount > array.length * 0.1) {
                    return false;
                }
            }
        }
        return disorderCount <= array.length * 0.1;
    }

    private int[] standardInsertionSort(int[] array) {
        for (int i = 1; i < array.length; i++) {
            int key = array[i];
            tracker.incrementArrayAccesses(1);
            int j = i - 1;

            tracker.incrementComparisons();
            tracker.incrementArrayAccesses(2);
            while (j >= 0 && array[j] > key) {
                array[j + 1] = array[j];
                tracker.incrementSwaps(1);
                tracker.incrementArrayAccesses(2);
                j--;

                if (j >= 0) {
                    tracker.incrementComparisons();
                    tracker.incrementArrayAccesses(1);
                }
            }
            array[j + 1] = key;
            tracker.incrementArrayAccesses(1);
        }
        return array;
    }

    private int[] optimizedSortForAlmostSorted(int[] array) {
        for (int i = 1; i < array.length; i++) {
            tracker.incrementArrayAccesses(2);
            if (array[i] < array[i - 1]) {
                int key = array[i];
                tracker.incrementArrayAccesses(1);

                int pos = binarySearch(array, 0, i - 1, key);

                for (int j = i; j > pos; j--) {
                    array[j] = array[j - 1];
                    tracker.incrementSwaps(1);
                    tracker.incrementArrayAccesses(2);
                }
                array[pos] = key;
                tracker.incrementArrayAccesses(1);
            }
            tracker.incrementComparisons();
        }
        return array;
    }

    private int binarySearch(int[] array, int left, int right, int key) {
        while (left <= right) {
            int mid = left + (right - left) / 2;
            tracker.incrementArrayAccesses(1);
            tracker.incrementComparisons();

            if (array[mid] == key) {
                return mid + 1;
            } else if (array[mid] < key) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return left;
    }

    public PerformanceTracker getPerformanceTracker() {
        return tracker;
    }

    public void resetPerformanceTracker() {
        tracker.reset();
    }
}