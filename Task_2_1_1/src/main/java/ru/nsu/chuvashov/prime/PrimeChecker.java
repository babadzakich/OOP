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
        ThreadBody[] threads = new ThreadBody[threadsAmount];
        for (int number : numbers) {
            for (int i = 0; i < threadsAmount; i++) {
                threads[i] = new ThreadBody(number, i);
                threads[i].start();
            }
            for (int i = 0; i < threadsAmount; i++) {
                threads[i].join();
                if (nigger) return true;
            }

        }
        return false;
    }

    @AllArgsConstructor
    private class ThreadBody extends Thread {
        private final int number;
        private final int index;

        @Override
        public void run() {
            for (int i = 2 + index; i * i <= number; i += threadsAmount) {
                if (nigger) break;
                if (number % i == 0) {
                    nigger = true;
                    break;
                }
            }
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
