package org.example;



public class Main {

    public static void main(String[] args) {
        int[] arr = {1, 12, 9, 5, 6, 10};

        Heapsort hs = new Heapsort();
        arr = hs.heapsort(arr);

        for (int i = 0; i < arr.length; ++i)
            System.out.print(arr[i] + " ");

    }
}

/**
 * В классе 2 функции, heapsort(int[] arr), и heapify(int[] arr, int n, int i)
 * heapify - смотрит, есть ли у вершины потомки больше неё и если есть, то заменяет на самый большой из них, пока вершина
 * не встанет на своё место(не будет самой большой среди потомков)
 * heapsort - создаёт двоичную кучу, просматривая, есть ли у вершины потомки большего размера с помощью heapify
 * затем она начинает брать вершину с 0 индекса(мы знаем что там точно - максимум) и обменивать ёё с последней вершной,
 * после чего мы опять запускаем функцию heapify, чтобы найти новый максимум в оставшейся куче размера меньше на 1
 * в конце получаем отсортированный массив
 */
class Heapsort{

    public static int[] heapsort(int[] arr) {
        int n = arr.length;

        // Build heap (rearrange array)
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(arr, n, i);
        }

        // One by one extract an element from heap
        for (int i = n - 1; i > 0; i--) {
            // Move current root to end
            int temp = arr[0];
            arr[0] = arr[i];
            arr[i] = temp;

            // Call max heapify on the reduced heap
            heapify(arr, i, 0);
        }
        return arr;
    }

    // To heapify a subtree rooted with node i which is an index in arr[]
    static void heapify(int[] arr, int n, int i) {
        int largest = i; // Initialize largest as root
        int left = 2 * i + 1; // left = 2*i + 1
        int right = 2 * i + 2; // right = 2*i + 2

        // If left child is larger than root
        if (left < n && arr[left] > arr[largest]) {
            largest = left;
        }

        // If right child is larger than largest so far
        if (right < n && arr[right] > arr[largest]) {
            largest = right;
        }

        // If largest is not root
        if (largest != i) {
            int swap = arr[i];
            arr[i] = arr[largest];
            arr[largest] = swap;

            // Recursively heapify the affected sub-tree
            heapify(arr, n, largest);
        }

    }
}
