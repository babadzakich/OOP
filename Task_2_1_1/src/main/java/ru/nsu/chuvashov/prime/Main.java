package ru.nsu.chuvashov.prime;

import java.util.ArrayList;
import java.util.List;

/**
 * Main class for time calculation.
 */
public class Main {
    /**
     * Main method to call to calculate time.
     *
     * @param args - no args.
     * @throws InterruptedException because of join.
     */
    public static void main(String[] args) throws InterruptedException {
        List<Integer> primes = constructPrimes();
        Integer[] array = primes.toArray(new Integer[0]);
        PrimeChecker checker = new PrimeChecker();

        long start = System.nanoTime();
        checker.hasNonPrimeSequential(array);
        System.out.println("Sequential algo completed in - "
                + ((System.nanoTime() - start)) + "ms");

        start = System.nanoTime();
        checker.hasNonPrimeThreads(array, 2);
        System.out.println("Thread algo with 2 threads completed in - "
                + ((System.nanoTime() - start)) + "ms");

        start = System.nanoTime();
        checker.hasNonPrimeThreads(array, 4);
        System.out.println("Thread algo with 4 threads completed in - "
                + ((System.nanoTime() - start)) + "ms");

        start = System.nanoTime();
        checker.hasNonPrimeThreads(array, 8);
        System.out.println("Thread algo with 8 threads completed in - "
                + ((System.nanoTime() - start)) + "ms");

        start = System.nanoTime();
        checker.hasNonPrimeThreads(array, 128);
        System.out.println("Thread algo with 128 threads completed in - "
                + ((System.nanoTime() - start)) + "ms");

        start = System.nanoTime();
        checker.hasNonPrimeStreams(array);
        System.out.println("Parallelstream algo completed in - "
                + ((System.nanoTime() - start)) + "ms");
    }

    private static ArrayList<Integer> constructPrimes() {
        boolean[] array = new boolean[1000000];
        ArrayList<Integer> primes = new ArrayList<>();
        array[0] = array[1] = true;
        for (int i = 2; i < array.length; i++) {
            if (!array[i]) {
                primes.add(i);
                for (int j = i * 2; j < array.length; j+=i) {
                    array[j] = true;
                }
            }
        }
        return primes;
    }
}
