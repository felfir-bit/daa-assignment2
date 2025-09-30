package cli;

import algorithms.InsertionSort;
import algorithms.SelectionSort;
import metrics.PerformanceTracker;

import java.util.Random;
import java.util.Scanner;

public class BenchmarkRunner {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Sorting Benchmark ===");
        System.out.println("📊 Results will be saved to performance_results.csv");

        Object sorter = selectSorter(scanner); // выбираем сортировщик

        while (true) {
            printMenu();
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    testWithFixedSizes(sorter);
                    break;
                case 2:
                    testCustomArray(scanner, sorter);
                    break;
                case 3:
                    testEdgeCases(sorter);
                    break;
                case 4:
                    generateFullReport(sorter);
                    break;
                case 5:
                    System.out.println("Выход...");
                    return;
                default:
                    System.out.println("Неверный выбор!");
            }
        }
    }

    private static Object selectSorter(Scanner scanner) {
        System.out.println("Выберите сортировщик:");
        System.out.println("1. Insertion Sort");
        System.out.println("2. Selection Sort");
        System.out.print("Ваш выбор: ");
        int choice = scanner.nextInt();
        if (choice == 2) {
            return new SelectionSort();
        }
        return new InsertionSort();
    }

    private static void printMenu() {
        System.out.println("\nВыберите опцию:");
        System.out.println("1. Тест на стандартных размерах (100, 1000, 10000)");
        System.out.println("2. Тест с пользовательским массивом");
        System.out.println("3. Тест крайних случаев");
        System.out.println("4. Сгенерировать полный CSV отчет");
        System.out.println("5. Выход");
        System.out.print("Ваш выбор: ");
    }

    private static void testWithFixedSizes(Object sorter) {
        int[] sizes = {100, 500, 1000, 5000};
        for (int size : sizes) {
            System.out.println("\n--- Тест размера: " + size + " ---");

            testArray(sorter, generateRandomArray(size), "Random_Array", size, "Random");
            testArray(sorter, generateSortedArray(size), "Sorted_Array", size, "Sorted");
            testArray(sorter, generateReverseSortedArray(size), "Reverse_Array", size, "ReverseSorted");
            testArray(sorter, generateAlmostSortedArray(size), "AlmostSorted_Array", size, "AlmostSorted");
        }
    }

    private static void testCustomArray(Scanner scanner, Object sorter) {
        System.out.print("Введите размер массива: ");
        int size = scanner.nextInt();

        System.out.println("Выберите тип массива:");
        System.out.println("1. Случайный");
        System.out.println("2. Уже отсортированный");
        System.out.println("3. Обратно отсортированный");
        System.out.println("4. Почти отсортированный");
        int type = scanner.nextInt();

        int[] array;
        String arrayType;
        switch (type) {
            case 2: array = generateSortedArray(size); arrayType = "Sorted"; break;
            case 3: array = generateReverseSortedArray(size); arrayType = "ReverseSorted"; break;
            case 4: array = generateAlmostSortedArray(size); arrayType = "AlmostSorted"; break;
            default: array = generateRandomArray(size); arrayType = "Random";
        }

        testArray(sorter, array, "Custom_Array", size, arrayType);
    }

    private static void testEdgeCases(Object sorter) {
        int[][] edgeCases = {
                new int[0],
                new int[]{42},
                new int[]{5, 2, 5, 3, 2, 1},
                new int[]{1, 1, 1, 1, 1}
        };
        String[] names = {"Empty_Array", "Single_Element", "Array_With_Duplicates", "Array_All_Same"};

        for (int i = 0; i < edgeCases.length; i++) {
            testArray(sorter, edgeCases[i], names[i], edgeCases[i].length, names[i]);
        }
    }

    private static void generateFullReport(Object sorter) {
        int[] sizes = {100, 500, 1000, 2000, 5000};
        String[] types = {"Random", "Sorted", "ReverseSorted", "AlmostSorted"};

        for (int size : sizes) {
            for (String type : types) {
                int[] array;
                switch (type) {
                    case "Sorted": array = generateSortedArray(size); break;
                    case "ReverseSorted": array = generateReverseSortedArray(size); break;
                    case "AlmostSorted": array = generateAlmostSortedArray(size); break;
                    default: array = generateRandomArray(size);
                }
                testArray(sorter, array, "Full_Report_" + type, size, type);
            }
        }
        System.out.println("✅ Полный отчет сохранен в performance_results.csv");
    }

    private static void testArray(Object sorterObj, int[] array, String testName, int size, String arrayType) {
        PerformanceTracker tracker;

        if (sorterObj instanceof InsertionSort sorter) {
            sorter.resetPerformanceTracker();
            tracker = sorter.getPerformanceTracker();

            if (array.length > 0) {
                sorter.sort(array.clone());
                sorter.resetPerformanceTracker();
            }

            tracker.startTimer();
            int[] sorted = sorter.sort(array);
            tracker.stopTimer();

            boolean isSorted = isSorted(sorted);

            System.out.println(testName + ":");
            System.out.println("  Корректно отсортирован: " + isSorted);
            System.out.println("  Время: " + tracker.getElapsedTime() + " ns");
            System.out.println("  Сравнения: " + tracker.getComparisons());
            System.out.println("  Обмены: " + tracker.getSwaps());
            System.out.println("  Обращения к массиву: " + tracker.getArrayAccesses());

            tracker.saveToCSV(testName, size, arrayType);

        } else if (sorterObj instanceof SelectionSort sorter) {
            sorter.resetPerformanceTracker();
            tracker = sorter.getPerformanceTracker();

            if (array.length > 0) {
                sorter.sort(array.clone());
                sorter.resetPerformanceTracker();
            }

            tracker.startTimer();
            int[] sorted = sorter.sort(array);
            tracker.stopTimer();

            boolean isSorted = isSorted(sorted);

            System.out.println(testName + ":");
            System.out.println("  Корректно отсортирован: " + isSorted);
            System.out.println("  Время: " + tracker.getElapsedTime() + " ns");
            System.out.println("  Сравнения: " + tracker.getComparisons());
            System.out.println("  Обмены: " + tracker.getSwaps());
            System.out.println("  Обращения к массиву: " + tracker.getArrayAccesses());

            tracker.saveToCSV(testName, size, arrayType);
        }
    }

    private static int[] generateRandomArray(int size) {
        Random random = new Random();
        int[] array = new int[size];
        for (int i = 0; i < size; i++) array[i] = random.nextInt(size * 10);
        return array;
    }

    private static int[] generateSortedArray(int size) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) array[i] = i;
        return array;
    }

    private static int[] generateReverseSortedArray(int size) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) array[i] = size - i;
        return array;
    }

    private static int[] generateAlmostSortedArray(int size) {
        int[] array = generateSortedArray(size);
        Random random = new Random();
        for (int i = 0; i < size * 0.1; i++) {
            int idx1 = random.nextInt(size);
            int idx2 = random.nextInt(size);
            int temp = array[idx1];
            array[idx1] = array[idx2];
            array[idx2] = temp;
        }
        return array;
    }

    private static boolean isSorted(int[] array) {
        for (int i = 0; i < array.length - 1; i++) if (array[i] > array[i + 1]) return false;
        return true;
    }
}
