package ru.nsu.chuvashov.substring;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
            System.out.println(res.size());
            assertEquals(1867, res.size());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}