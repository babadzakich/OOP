package ru.nsu.chuvashov.prime;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class PrimeCheckerTest {
    Integer[] test1 = new Integer[]{6, 8, 7, 13, 5, 9, 4};
    Integer[] test2 = new Integer[]{20319251, 6997901, 6997927, 6997937, 17858849, 6997967,
        6998009, 6998029, 6998039, 20165149, 6998051, 6998053};
    @Test
    void hasNonPrimeSequential() {
        PrimeChecker checker = new PrimeChecker();
        assertTrue(checker.hasNonPrimeSequential(test1));
        assertFalse(checker.hasNonPrimeSequential(test2));
    }

    @Test
    void hasNonPrimeThreads() throws InterruptedException {
        PrimeChecker checker = new PrimeChecker();
        assertTrue(checker.hasNonPrimeThreads(test1, 8));
        assertFalse(checker.hasNonPrimeThreads(test2, 8));
    }

    @Test
    void hasNonPrimeParallel() {
        PrimeChecker checker = new PrimeChecker();
        assertTrue(checker.hasNonPrimeStreams(test1));
        assertFalse(checker.hasNonPrimeStreams(test2));
    }
}