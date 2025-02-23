package ru.nsu.chuvashov.prime;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * Benchmark Runner.
 */
public class BenchmarkRunner {
    /**
     * Main body for class.
     *
     * @param args - no args.
     * @throws RunnerException because of run.
     */
    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(Benchmarkk.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run(); // Запуск бенчмарков
    }
}
