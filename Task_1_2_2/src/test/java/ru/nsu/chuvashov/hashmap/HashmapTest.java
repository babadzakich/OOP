package ru.nsu.chuvashov.hashmap;

import static org.junit.jupiter.api.Assertions.*;

import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;

class HashmapTest {

    @Test
    void put() {
        Hashmap<Integer, Integer> hashmap = new Hashmap<>();
        hashmap.put(1, 1);
        hashmap.put(2, 2);
        assertEquals(hashmap.get(1), 1);
    }

    @Test
    void delete() {
        Hashmap<String, Integer> hashmap = new Hashmap<>();
        hashmap.put("1", 1);
        hashmap.delete("1", 1);
        assertThrows(NoSuchElementException.class, () -> hashmap.get("1"));
    }

    @Test
    void update() {
    }

    @Test
    void get() {
    }

    @Test
    void contains() {
    }

    @Test
    void iterator() {
    }

    @Test
    void print() {
    }
}