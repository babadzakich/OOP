package ru.nsu.chuvashov.hashmap;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;

class HashTableTest {

    @Test
    void put() {
        HashTable<Integer, Integer> hashTable = new HashTable<>();
        hashTable.put(1, 1);
        hashTable.put(2, 2);
        hashTable.put(1000001, 3);
        assertEquals(hashTable.get(1), 1);
    }

    @Test
    void putExisting() {
        HashTable<Integer, Integer> hashTable = new HashTable<>();
        hashTable.put(1, 1);
        assertThrows(IllegalArgumentException.class, () -> hashTable.put(1, 2));
    }

    @Test
    void delete() {
        HashTable<String, Integer> hashTable = new HashTable<>();
        hashTable.put("1", 1);
        hashTable.delete("1", 1);
        assertThrows(NoSuchElementException.class, () -> hashTable.get("1"));
    }

    @Test
    void deleteNonexistent() {
        HashTable<String, Integer> hashTable = new HashTable<>();
        hashTable.put("1", 1);
        assertThrows(NoSuchElementException.class, () -> hashTable.delete("2", 1));
    }

    @Test
    void deleteDiffValues() {
        HashTable<String, Integer> hashTable = new HashTable<>();
        hashTable.put("1", 1);
        hashTable.put("2", 2);
        assertThrows(IllegalArgumentException.class, () -> hashTable.delete("1", 2));
    }

    @Test
    void update() {
        HashTable<String, Integer> hashTable = new HashTable<>();
        hashTable.put("1", 1);
        int first = hashTable.get("1");
        hashTable.update("1", 2);
        assertNotEquals(hashTable.get("1"), first);
    }

    @Test
    void updateNonexistent() {
        HashTable<String, Integer> hashTable = new HashTable<>();
        hashTable.put("1", 1);
        assertThrows(NoSuchElementException.class, () -> hashTable.update("2", 1));
    }

    @Test
    void getNonexistent() {
        HashTable<String, Integer> hashTable = new HashTable<>();
        hashTable.put("1", 1);
        assertThrows(NoSuchElementException.class, () -> hashTable.get("2"));
    }

    @Test
    void contains() {
        HashTable<String, Integer> hashTable = new HashTable<>();
        hashTable.put("1", 1);
        assertTrue(hashTable.contains("1"));
    }

    @Test
    void containsNonexistent() {
        HashTable<String, Integer> hashTable = new HashTable<>();
        hashTable.put("1", 1);
        assertFalse(hashTable.contains("2"));
    }

    @Test
    void iterator() {
        HashTable<String, Integer> hashTable = new HashTable<>();
        hashTable.put("1", 1);
        hashTable.put("2", 2);
        hashTable.put("3", 3);
        hashTable.put("4", 4);
        PrintStream outputStream = System.out;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        for (HashTable.Entry<String, Integer> entry : hashTable) {
            System.out.println(entry);
        }
        System.setOut(outputStream);
        assertEquals(out.toString(), "1 1\n2 2\n3 3\n4 4\n");
    }

    @Test
    void print() {
        HashTable<Integer, Integer> hashTable = new HashTable<>();
        hashTable.put(1, 1);
        hashTable.put(2, 2);
        hashTable.put(3, 3);
        hashTable.put(4, 4);
        PrintStream outputStream = System.out;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        hashTable.print();
        System.setOut(outputStream);
        assertEquals(out.toString(), "1 1\n2 2\n3 3\n4 4\n");
    }

    @Test
    void resizeTest() {
        HashTable<String, String> hashTable = new HashTable<>(2);
        hashTable.put("1", "1");
        hashTable.put("2", "3");
        hashTable.put("3", "4");
        assertEquals("4", hashTable.get("3"));
    }

    @Test
    void concurrentTest() {
        HashTable<Integer, Integer> hashTable = new HashTable<>();
        hashTable.put(1, 1);
        hashTable.put(2, 2);
        hashTable.put(3, 3);
        try {
            for (HashTable.Entry<Integer, Integer> entry : hashTable) {
                hashTable.delete(entry.key, entry.value);
            }
        } catch (ConcurrentModificationException e) {
            assertTrue(e.getMessage().contains("concurrent modification"));
        }
    }

    @Test
    void equalsTest() {
        HashTable<Integer, Integer> hashTable = new HashTable<>();
        HashTable<Integer, Integer> hashTable2 = new HashTable<>();
        hashTable.put(1, 1);
        hashTable.put(2, 2);
        hashTable.put(3, 3);
        hashTable2.put(1, 1);
        hashTable2.put(2, 2);
        hashTable2.put(3, 3);
        assertEquals(hashTable, hashTable2);
    }

    @Test
    void notEqualsTest() {
        HashTable<Integer, Integer> hashTable = new HashTable<>();
        hashTable.put(1, 1);
        hashTable.put(2, 2);
        hashTable.put(3, 3);
        HashTable<String, Integer> hashTable2 = new HashTable<>();
        hashTable2.put("1", 1);
        hashTable2.put("2", 2);
        hashTable2.put("3", 3);
        assertNotEquals(hashTable, hashTable2);
    }
}