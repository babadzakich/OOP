package ru.nsu.chuvashov.prime;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Integer[] array = new Integer[] {20319251, 6997901, 6997927, 6997937, 17858849, 6997967,
                6998009, 6998029, 6998039, 20165149, 6998051, 6998053};
        PrimeChecker checker = new PrimeChecker();

        long start = System.currentTimeMillis();
        checker.hasNonPrimeSequential(array);
        System.out.println("Sequential algo completed in - "
                + ((System.currentTimeMillis() - start)) + "ms");

        start = System.currentTimeMillis();
        checker.hasNonPrimeThreads(array, 2);
        System.out.println("Thread algo with 2 threads completed in - "
                + ((System.currentTimeMillis() - start)) + "ms");

        start = System.currentTimeMillis();
        checker.hasNonPrimeThreads(array, 4);
        System.out.println("Thread algo with 4 threads completed in - "
                + ((System.currentTimeMillis() - start)) + "ms");

        start = System.currentTimeMillis();
        checker.hasNonPrimeThreads(array, 8);
        System.out.println("Thread algo with 8 threads completed in - "
                + ((System.currentTimeMillis() - start)) + "ms");

        start = System.currentTimeMillis();
        checker.hasNonPrimeThreads(array, 128);
        System.out.println("Thread algo with 128 threads completed in - "
                + ((System.currentTimeMillis() - start)) + "ms");

        start = System.currentTimeMillis();
        checker.hasNonPrimeStreams(array);
        System.out.println("Parallelstream algo completed in - "
                + ((System.currentTimeMillis() - start)) + "ms");
    }
}
