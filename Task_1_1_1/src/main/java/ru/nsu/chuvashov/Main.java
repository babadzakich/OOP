package ru.nsu.chuvashov;

import java.io.Console;
import java.util.Arrays;

/**
 * Реализация пирамидальной сортировки.
 * Класс Main с методами heapsort, heapify.
 *
 * @author Чувашов Артём
 */
public class Main {

    /**
     * Мы смотрим на предпоследний уровень дерева.
     * Потом начинаем сортировать с помощью heapify.
     *
     * @param arr сортирующийся массив
     * @return отсортированный массив
     */
    public static int[] heapsort(int[] arr) {
        int n = arr.length;

        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(arr, n, i);
        }

        for (int i = n - 1; i > 0; i--) {

            int temp = arr[0];
            arr[0] = arr[i];
            arr[i] = temp;

            heapify(arr, i, 0);
        }
        return arr;
    }

    /**
     * Мы смотрим на вершину и на её потомков.
     * Если максимум из потомков больше вершины, то свапаем их.
     * Делаем так, пока вершина не станет листом.
     * Или пока максимум её детей не станет меньше вершины.
     *
     * @param arr - сортирующийся массив
     * @param n - длина массива
     * @param i - вершина которую сейчас проверяем
     */
    static void heapify(int[] arr, int n, int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < n && arr[left] > arr[largest]) {
            largest = left;
        }

        if (right < n && arr[right] > arr[largest]) {
            largest = right;
        }

        if (largest != i) {
            int swap = arr[i];
            arr[i] = arr[largest];
            arr[largest] = swap;

            heapify(arr, n, largest);
        }

    }

    /**
     * Функция для запуска скомпилированного приложения.
     *
     * @param args - базовый параметр
     */
    public static void main(String[] args) {
        Console console = System.console();
        String[] consulIn;

        if (console == null) {
            consulIn = new String[] {"0"};
        }
        else{
            consulIn = System.console().readLine().split(" ");
        }

        int[] inArr = new int[consulIn.length];

        for (int i = 0; i < consulIn.length; i++) {
            inArr[i] = Integer.parseInt(consulIn[i]);
        }

        int[] res = Main.heapsort(inArr);

        System.out.println(Arrays.toString(res));
    }
}
