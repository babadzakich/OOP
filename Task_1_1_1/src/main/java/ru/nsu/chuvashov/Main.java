package ru.nsu.chuvashov;


import java.io.Console;
import java.util.Arrays;

public class Main {

    /**
     * Мы смотрим на предпоследний уровень дерева(у всех элементов i > n/2-1 - i * 2 + 1 > n (не может быть потомкав)
     * Потом начинаем сортировать с помощью heapify
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
     * мы смотрим на вершину и на её потомков, если максимум из потомков юольше вершины, то свапаем из, и делаеи так,
     * пока вершина не станет листом или пока максимум её детей не станет меньше вершины
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

    public static void main(String[] args) {
        Console consl = System.console();
        String[] conslInp;

        if (consl == null){
            conslInp = new String[]{"0"};
        }
        else{
            conslInp = System.console().readLine().split(" ");
        }

        int[] inArr = new int[conslInp.length];

        for (int i = 0; i < conslInp.length; i++) {
            inArr[i] = Integer.parseInt(conslInp[i]);
        }

        int[] res = Main.heapsort(inArr);

        System.out.println(Arrays.toString(res));
    }
}
