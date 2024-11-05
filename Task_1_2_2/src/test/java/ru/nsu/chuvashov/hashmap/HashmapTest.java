package ru.nsu.chuvashov.hashmap;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;

class HashmapTest {

    @Test
    void put() {
        Hashmap<Integer, Integer> hashmap = new Hashmap<>();
        hashmap.put(1, 1);
        hashmap.put(2, 2);
        hashmap.put(1000001, 3);
        assertEquals(hashmap.get(1), 1);
    }

    @Test
    void putExisting() {
        Hashmap<Integer, Integer> hashmap = new Hashmap<>();
        hashmap.put(1, 1);
        assertThrows(IllegalArgumentException.class, () -> hashmap.put(1, 2));
    }

    @Test
    void delete() {
        Hashmap<String, Integer> hashmap = new Hashmap<>();
        hashmap.put("1", 1);
        hashmap.delete("1", 1);
        assertThrows(NoSuchElementException.class, () -> hashmap.get("1"));
    }

    @Test
    void deleteNonexistent() {
        Hashmap<String, Integer> hashmap = new Hashmap<>();
        hashmap.put("1", 1);
        assertThrows(NoSuchElementException.class, () -> hashmap.delete("2", 1));
    }

    @Test
    void deleteDiffValues() {
        Hashmap<String, Integer> hashmap = new Hashmap<>();
        hashmap.put("1", 1);
        hashmap.put("2", 2);
        assertThrows(IllegalArgumentException.class, () -> hashmap.delete("1", 2));
    }

    @Test
    void update() {
        Hashmap<String, Integer> hashmap = new Hashmap<>();
        hashmap.put("1", 1);
        int first = hashmap.get("1");
        hashmap.update("1", 2);
        assertNotEquals(hashmap.get("1"), first);
    }

    @Test
    void updateNonexistent() {
        Hashmap<String, Integer> hashmap = new Hashmap<>();
        hashmap.put("1", 1);
        assertThrows(NoSuchElementException.class, () -> hashmap.update("2", 1));
    }

    @Test
    void getNonexistent() {
        Hashmap<String, Integer> hashmap = new Hashmap<>();
        hashmap.put("1", 1);
        assertThrows(NoSuchElementException.class, () -> hashmap.get("2"));
    }

    @Test
    void contains() {
        Hashmap<String, Integer> hashmap = new Hashmap<>();
        hashmap.put("1", 1);
        assertTrue(hashmap.contains("1"));
    }

    @Test
    void containsNonexistent() {
        Hashmap<String, Integer> hashmap = new Hashmap<>();
        hashmap.put("1", 1);
        assertFalse(hashmap.contains("2"));
    }

    @Test
    void iterator() {
        Hashmap<String, Integer> hashmap = new Hashmap<>();
        hashmap.put("1", 1);
        hashmap.put("2", 2);
        hashmap.put("3", 3);
        hashmap.put("4", 4);
        PrintStream outputStream = System.out;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        for (Hashmap.Entry<String, Integer> entry : hashmap) {
            System.out.println(entry);
        }
        System.setOut(outputStream);
        assertEquals(out.toString(), "1 1\n2 2\n3 3\n4 4\n");
    }

    @Test
    void print() {
        Hashmap<Integer, Integer> hashmap = new Hashmap<>();
        hashmap.put(1, 1);
        hashmap.put(2, 2);
        hashmap.put(3, 3);
        hashmap.put(4, 4);
        PrintStream outputStream = System.out;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        hashmap.print();
        System.setOut(outputStream);
        assertEquals(out.toString(), "1 1\n2 2\n3 3\n4 4\n");
    }

    @Test
    void resizeTest() {
        Hashmap<String, String> hashmap = new Hashmap<>(2);
        hashmap.put("1", "1");
        hashmap.put("2", "3");
        hashmap.put("3", "4");
        assertEquals("4", hashmap.get("3"));
    }
}