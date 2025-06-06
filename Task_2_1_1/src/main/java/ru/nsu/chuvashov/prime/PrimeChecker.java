package ru.nsu.chuvashov.prime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;

import javax.management.monitor.Monitor;

/**
 * Class for all algorithms to check for nonprime number.
 */
public class PrimeChecker {

    /**
     * Checking for nonprime number without parallelism.
     *
     * @param numbers - initial array
     * @return true if we have nonprime, false else.
     */
    public boolean hasNonPrimeSequential(Integer[] numbers) {
        for (int number : numbers) {
            if (IsNotPrime.isNotPrime(number)) {
                return true;
            }
        }
        return false;
    }

    private boolean flag;
    private boolean starter = false;
    private final Object lock = new Object();

    /**
     * Method that uses threads to search for nonprime.
     *
     * @param numbers - initial array.
     * @param amount - amount of threads.
     * @return true if we have nonprime, false else.
     * @throws InterruptedException - from join.
     */
    public boolean hasNonPrimeThreads(Integer[] numbers, int amount) throws InterruptedException {
        flag = false;
        List<Thread> threads = new ArrayList<>(amount);
        int step = Math.floorDiv(numbers.length, amount);
        int j = 0;
        for (int i = 0; i < amount; i++) {
            Thread thread = new Thread(new ThreadBody(numbers, j,
                    i == (amount - 1) ? numbers.length : j + step));
            threads.add(thread);
            thread.start();
            j += step;
        }

        synchronized (lock) {
            starter = true;
            lock.notifyAll();
        }

        for (Thread thread : threads) {
            thread.join();
        }
        return flag;
    }

    @AllArgsConstructor
    private class ThreadBody implements Runnable {
        private final Integer[] array;
        private final int start;
        private final int end;

        @Override
        public void run() {
            synchronized (lock) {
                while (!starter) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            }
            for (int i = start; i < end && !flag; i++) {
                if (IsNotPrime.isNotPrime(array[i])) {
                    flag = true;
                    break;
                }
            }
        }


    }

    public boolean hasNonPrimeStreams(Integer[] nums) {
        List<Integer> numbers = new ArrayList<>(Arrays.asList(nums));
        return numbers.parallelStream().filter(IsNotPrime::isNotPrime).count() > 0;
    }

    /**
     * Class that stands for checking for primeness of a number.
     */
    public static class IsNotPrime {
        /**
         * Checker for primeness.
         *
         * @param number - to check.
         * @return true if number is not prime.
         */
        public static boolean isNotPrime(int number) {
            if ((number % 2 == 0 && number != 2) || Math.abs(number) < 2) {
                return false;
            }
            for (int i = 3; i * i <= number; i += 2) {
                if (number % i == 0) {
                    return true;
                }
            }
            return false;
        }
    }
}
