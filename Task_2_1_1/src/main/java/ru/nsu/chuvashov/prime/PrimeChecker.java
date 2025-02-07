package ru.nsu.chuvashov.prime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;

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
            for (int i = 2; i * i <= number; i++) {
                if (number % i == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean nigger;
    private int threadsAmount;

    /**
     * Method that uses threads to search for nonprime.
     *
     * @param numbers - initial array.
     * @param amount - amount of threads.
     * @return true if we have nonprime, false else.
     * @throws InterruptedException - from join.
     */
    public boolean hasNonPrimeThreads(Integer[] numbers, int amount) throws InterruptedException {
        nigger = false;
        threadsAmount = amount;
        List<ThreadBody> threads = new ArrayList<>(threadsAmount);

        for (int i = 0; i < numbers.length; i+=threadsAmount) {
            ThreadBody thread = new ThreadBody(numbers, i,
                    Math.min(i + threadsAmount, numbers.length));
            threads.add(thread);
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
        return nigger;
    }

    @AllArgsConstructor
    private class ThreadBody extends Thread {
        private final Integer[] array;
        private final int start;
        private final int end;

        @Override
        public void run() {
            for (int i = start; i <= end && !nigger; i++) {
                if (!isPrime(array[i])) {
                    nigger = true;
                }
            }
        }
        private boolean isPrime(int number) {
            if (number % 2 == 0) return false;
            for (int i = 3; i * i <= number; i += 2) {
                if (number % i == 0) {
                    return false;
                }
            }
            return true;
        }
    }

    public boolean hasNonPrimeStreams(Integer[] nums) {
        List<Integer> numbers = new ArrayList<>(Arrays.asList(nums));
        return numbers.parallelStream().anyMatch(this::isNotPrime);
    }

    private boolean isNotPrime(int number) {
        for (int i = 2; i * i <= number; i++) {
            if (number % i == 0) {
                return true;
            }
        }
        return false;
    }
}
