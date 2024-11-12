package ru.nsu.chuvashov.substring;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class FinderTest {

    @Test
    void find() {
        Finder finder = new Finder();
        try {
            List<Integer> res = finder.find("test1.txt", "бра");
            List<Integer> expected = List.of(1, 8);
            assertEquals(expected, res);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGamlet() {
        Finder finder = new Finder();
        try {
            List<Integer> res = finder.find("test2.txt", "in");
            assertEquals(1867, res.size());
            System.out.println(res);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testStrugatskie() {
        Finder finder = new Finder();
        try {
            List<Integer> res = finder.find("test3.txt", "было");
            assertEquals(182, res.size());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testBigAssData() {
        String line = "All work and no play makes Jack a dull boy";
        StringBuilder sb = new StringBuilder();
        sb.append(line.repeat(10000));

        List<Integer> res = new ArrayList<>();
        KnuthMorrisPratt.getPattern(sb.toString(), "work", res, 0);
        System.out.println(res.size());
    }
}