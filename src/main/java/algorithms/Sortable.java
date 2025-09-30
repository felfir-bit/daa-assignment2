package algorithms;

import metrics.PerformanceTracker;

public interface Sortable {
    int[] sort(int[] array);
    PerformanceTracker getPerformanceTracker();
    void resetPerformanceTracker();
}
