package algorithms;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class SelectionSortTest {
    private SelectionSort sorter;

    @BeforeEach
    void setUp() {
        sorter = new SelectionSort();
    }

    @Test
    void testEmptyArray() {
        int[] input = {};
        int[] result = sorter.sort(input);
        assertArrayEquals(new int[]{}, result);
    }

    @Test
    void testSingleElement() {
        int[] input = {42};
        int[] result = sorter.sort(input);
        assertArrayEquals(new int[]{42}, result);
    }

    @Test
    void testAlreadySorted() {
        int[] input = {1, 2, 3, 4, 5};
        int[] result = sorter.sort(input);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, result);
    }

    @Test
    void testReverseSorted() {
        int[] input = {5, 4, 3, 2, 1};
        int[] result = sorter.sort(input);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, result);
    }

    @Test
    void testRandomArray() {
        int[] input = {64, 34, 25, 12, 22, 11, 90};
        int[] result = sorter.sort(input);
        int[] expected = {11, 12, 22, 25, 34, 64, 90};
        assertArrayEquals(expected, result);
    }

    @Test
    void testDuplicates() {
        int[] input = {5, 2, 5, 3, 2, 1};
        int[] result = sorter.sort(input);
        int[] expected = {1, 2, 2, 3, 5, 5};
        assertArrayEquals(expected, result);
    }

    @Test
    void testNullInput() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> sorter.sort(null));
        assertTrue(exception.getMessage().contains("cannot be null"));
    }

    @Test
    void testCSVOutput() {
        int[] input = {5, 2, 4, 1, 3};
        sorter.sort(input);

        sorter.getPerformanceTracker().saveToCSV("SelectionSort_Test", 5, "TestData");

        File csvFile = new File("performance_results.csv");
        assertTrue(csvFile.exists() || csvFile.length() > 0);
    }
}
