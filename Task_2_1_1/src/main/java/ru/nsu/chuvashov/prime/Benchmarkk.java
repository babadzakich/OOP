package ru.nsu.chuvashov.prime;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

@State(Scope.Benchmark) // Состояние, общее для всех тестов
@BenchmarkMode(Mode.AverageTime) // Измеряем среднее время выполнения
@OutputTimeUnit(TimeUnit.MILLISECONDS) // Результаты в миллисекундах
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS) // 3 итерации разогрева
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS) // 5 итераций измерения
@Fork(1)
public class Benchmarkk {
    private PrimeChecker checker;
    boolean[] array;
    ArrayList<Integer> primes;
    Integer[] primesArray;

    @Setup
    public void setup() {
        checker = new PrimeChecker();
        array = new boolean[1000000];
        primes = new ArrayList<>();
        array[0] = array[1] = true;
        for (int i = 2; i < array.length; i++) {
            if (!array[i]) {
                primes.add(i);
                for (int j = i * 2; j < array.length; j+=i) {
                    array[j] = true;
                }
            }
        }
        primesArray = primes.toArray(new Integer[0]);
    }

    @Benchmark
    public void testSequential(Blackhole blackhole) {
        boolean result = checker.hasNonPrimeSequential(primesArray);
        blackhole.consume(result);
    }

    @Benchmark
    public void testThreadsOne(Blackhole blackhole) {
        boolean result = true;
        try {
            result = checker.hasNonPrimeThreads(primesArray, 1);
        } catch (Exception e) {
            blackhole.consume(e);
        }
        blackhole.consume(result);
    }

    @Benchmark
    public void testThreadsTwo(Blackhole blackhole) {
        boolean result = true;
        try {
            result = checker.hasNonPrimeThreads(primesArray, 2);
        } catch (Exception e) {
            blackhole.consume(e);
        }
        blackhole.consume(result);
    }

    @Benchmark
    public void testThreadsFour(Blackhole blackhole) {
        boolean result = true;
        try {
            result = checker.hasNonPrimeThreads(primesArray, 4);
        } catch (Exception e) {
            blackhole.consume(e);
        }
        blackhole.consume(result);
    }

    @Benchmark
    public void testThreadsEight(Blackhole blackhole) {
        boolean result = true;
        try {
            result = checker.hasNonPrimeThreads(primesArray, 8);
        } catch (Exception e) {
            blackhole.consume(e);
        }
        blackhole.consume(result);
    }

    @Benchmark
    public void testThreads128(Blackhole blackhole) {
        boolean result = true;
        try {
            result = checker.hasNonPrimeThreads(primesArray, 128);
        } catch (Exception e) {
            blackhole.consume(e);
        }
        blackhole.consume(result);
    }

    @Benchmark
    public void testParallelstream(Blackhole blackhole) {
        boolean result = checker.hasNonPrimeSequential(primesArray);
        blackhole.consume(result);
    }
}
