package ru.nsu.chuvashov.prime;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class BenchmarkRunner {
    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(Benchmarkk.class.getSimpleName()) // Указываем класс с бенчмарками
                .forks(1) // Количество запусков
                .build();

        new Runner(opt).run(); // Запуск бенчмарков
    }
}
