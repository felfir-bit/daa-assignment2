package metrics;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Трекер производительности для сбора метрик алгоритма
 */
public class PerformanceTracker {
    private long comparisons;
    private long swaps;
    private long arrayAccesses;
    private long memoryAllocations;
    private long startTime;
    private long endTime;

    public PerformanceTracker() {
        reset();
    }

    public void reset() {
        comparisons = 0;
        swaps = 0;
        arrayAccesses = 0;
        memoryAllocations = 0;
        startTime = 0;
        endTime = 0;
    }

    public void startTimer() {
        startTime = System.nanoTime();
    }

    public void stopTimer() {
        endTime = System.nanoTime();
    }

    public long getElapsedTime() {
        return endTime - startTime;
    }

    public void incrementComparisons() {
        comparisons++;
    }

    public void incrementComparisons(long count) {
        comparisons += count;
    }

    public void incrementSwaps() {
        swaps++;
    }

    public void incrementSwaps(long count) {
        swaps += count;
    }

    public void incrementArrayAccesses() {
        arrayAccesses++;
    }

    public void incrementArrayAccesses(long count) {
        arrayAccesses += count;
    }

    public void recordMemoryAllocation(long bytes) {
        memoryAllocations += bytes;
    }

    // Геттеры
    public long getComparisons() { return comparisons; }
    public long getSwaps() { return swaps; }
    public long getArrayAccesses() { return arrayAccesses; }
    public long getMemoryAllocations() { return memoryAllocations; }

    /**
     * Сохраняет метрики в CSV файл
     */
    public void saveToCSV(String testName, int arraySize, String arrayType) {
        saveToCSV("performance_results.csv", testName, arraySize, arrayType);
    }

    /**
     * Сохраняет метрики в указанный CSV файл
     */
    public void saveToCSV(String filename, String testName, int arraySize, String arrayType) {
        try (FileWriter writer = new FileWriter(filename, true)) {
            // Если файл пустой, добавляем заголовок
            if (getFileLength(filename) == 0) {
                writer.append("Timestamp,TestName,ArraySize,ArrayType,Time(ns),Comparisons,Swaps,ArrayAccesses,MemoryAllocations\n");
            }

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            long elapsedTime = getElapsedTime();

            // Для очень маленьких времен используем микросекунды
            if (elapsedTime < 1000) {
                elapsedTime = 1000; // Минимум 1 микросекунда для избежания нулей
            }

            writer.append(String.format("%s,%s,%d,%s,%d,%d,%d,%d,%d\n",
                    timestamp,
                    testName,
                    arraySize,
                    arrayType,
                    elapsedTime,
                    comparisons,
                    swaps,
                    arrayAccesses,
                    memoryAllocations
            ));

            writer.flush();
            System.out.println("✅ Results saved to " + filename);

        } catch (IOException e) {
            System.out.println("❌ Error saving to CSV: " + e.getMessage());
        }
    }

    private long getFileLength(String filename) {
        try {
            java.io.File file = new java.io.File(filename);
            return file.length();
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public String toString() {
        return String.format(
                "Performance Metrics:\nComparisons: %d\nSwaps: %d\nArray Accesses: %d\nMemory Allocated: %d bytes\nTime Elapsed: %d ns",
                comparisons, swaps, arrayAccesses, memoryAllocations, getElapsedTime()
        );
    }
}