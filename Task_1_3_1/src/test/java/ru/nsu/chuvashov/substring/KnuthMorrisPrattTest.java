package ru.nsu.chuvashov.substring;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class KnuthMorrisPrattTest {

    @Test
    void getPattern() {
        String text = "абраkadaбра";
        String pattern = "бра";
        List<Integer> result = new ArrayList<>();
        KnuthMorrisPratt.getPattern(text, pattern, result, 0);
        List<Integer> expected = new ArrayList<>();
        expected.add(1);
        expected.add(8);
        assertEquals(expected, result);
    }
}