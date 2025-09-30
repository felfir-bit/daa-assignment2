package algorithms;

import metrics.PerformanceTracker;

/**
 * Оптимизированная сортировка выбором (Selection Sort) с корректной ранней остановкой
 */
public class SelectionSort implements Sortable {
    private PerformanceTracker tracker;

    public SelectionSort() {
        this.tracker = new PerformanceTracker();
    }

    public SelectionSort(PerformanceTracker tracker) {
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

        for (int i = 0; i < result.length - 1; i++) {
            int minIndex = i;

            // поиск минимума в неотсортированной части
            for (int j = i + 1; j < result.length; j++) {
                tracker.incrementComparisons();
                tracker.incrementArrayAccesses(2);
                if (result[j] < result[minIndex]) {
                    minIndex = j;
                }
            }

            // обмен, если нашли меньший элемент
            if (minIndex != i) {
                int temp = result[i];
                result[i] = result[minIndex];
                result[minIndex] = temp;
                tracker.incrementSwaps(1);
                tracker.incrementArrayAccesses(4);
            }

            // ✅ корректная ранняя остановка:
            // если оставшаяся часть массива уже отсортирована — прерываем цикл
            if (isSorted(result, i + 1)) {
                break;
            }
        }

        return result;
    }

    /**
     * Проверка, отсортирован ли хвост массива начиная с позиции start
     */
    private boolean isSorted(int[] arr, int start) {
        for (int i = start; i < arr.length - 1; i++) {
            tracker.incrementComparisons();
            tracker.incrementArrayAccesses(2);
            if (arr[i] > arr[i + 1]) {
                return false;
            }
        }
        return true;
    }

    public PerformanceTracker getPerformanceTracker() {
        return tracker;
    }

    public void resetPerformanceTracker() {
        tracker.reset();
    }
}
