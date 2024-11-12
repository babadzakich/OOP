package ru.nsu.chuvashov.substring;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
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
}