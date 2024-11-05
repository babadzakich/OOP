package ru.nsu.chuvashov.hashmap;

import java.util.*;

/**
 * Hashmap class that utilizes
 * arraylists of linked lists
 * to work with collisions.
 *
 * @param <K> - type of keys.
 * @param <V> - type of values.
 */
public class HashTable<K, V> implements Iterable<HashTable.Entry<K, V>> {
    private List<Entry<K, V>>[] map;
    private int size;
    private int currentMod;
    private int capacity;
    private final double loadFactor = 0.75;

    /**
     * Constructor for hashmap with default size.
     */
    public HashTable() {
        size = 16;
        Creator();
    }

    /**
     * Constructor for hashmap with given capacity.
     *
     * @param capacity - our hashmap capacity.
     */
    public HashTable(int capacity) {
        this.size = capacity;
        Creator();
    }

    /**
     * Real creator.
     */
    private void Creator() {
        currentMod = 0;
        this.capacity = 0;
        map = (LinkedList<Entry<K,V>>[]) new LinkedList<?>[size];
    }

    /**
     * Put element in hashmap by the key.
     * If needed, we resize our hashmap.
     *
     * @param key - our key.
     * @param value - our value.
     */
    public void put(K key, V value) {
        int hash = hash(key);
        if (map[hash] != null) {
            for (Entry<K, V> entry : map[hash]) {
                if (entry.key == key) {
                    throw new IllegalArgumentException("key already exists");
                }
            }

        } else {
            map[hash] = new LinkedList<>();
        }
        map[hash].add(new Entry<>(key, value));
        currentMod++;
        capacity++;
        if (capacity * loadFactor >= size) {
            resize();
        }
    }

    /**
     * If our map capacity divided by size exceeds load factor,
     * we resize our hashmap to avoid huge collisions.
     */
    private void resize() {
        size *= 2;
        List<Entry<K, V>>[] newMap = (LinkedList<Entry<K,V>>[]) new LinkedList<?>[size];
        for (List<Entry<K, V>> list : map) {
            for (Entry<K, V> entry : list) {
                if (newMap[hash(entry.key)] == null) {
                    newMap[hash(entry.key)] = new LinkedList<>();
                }
                newMap[hash(entry.key)].add(new Entry<>(entry.key, entry.value));
            }
        }
        map = newMap;
    }

    /**
     * Searches for key with needed value and deletes it.
     *
     * @param key - our key.
     * @param value - our value.
     */
    public void delete(K key, V value) {
        int index = hash(key);
        if (map[index] == null) {
            throw new NoSuchElementException("bucket not initialized");
        }
        for (Entry<K, V> entry : map[index]) {
            if (entry.key == key && entry.value == value) {
                map[index].remove(entry);
                currentMod++;
                capacity--;
                return;
            } else if (entry.key == key) {
                throw new IllegalArgumentException("Value differs from hashed one");
            }
        }
        throw new NoSuchElementException("key not found");
    }

    /**
     * Searches for key and gives it new value.
     *
     * @param key what we search.
     * @param value what we change.
     */
    public void update(K key, V value) {
        int index = hash(key);
        if (map[index] == null) {
            throw new NoSuchElementException("bucket not initialized");
        }
        for (Entry<K, V> entry : map[index]) {
            if (entry.key == key) {
                entry.value = value;
                currentMod++;
                return;
            }
        }
        throw new NoSuchElementException("key not found");
    }

    /**
     * Getter from hashmap.
     *
     * @param key what we search.
     * @return value.
     */
    public V get(K key) {
        int index = hash(key);
        if (map[index] == null) {
            throw new NoSuchElementException("bucket not initialized");
        }
        for (Entry<K, V> entry : map[index]) {
            if (entry.key == key) {
                return entry.value;
            }
        }
        throw new NoSuchElementException("key not found");
    }

    /**
     * Contains method.
     *
     * @param key - what we search.
     * @return true if key is in hashmap.
     */
    public boolean contains(K key) {
        int index = hash(key);
        if (map[index] == null) {
            return false;
        }
        for (Entry<K, V> entry : map[index]) {
            if (entry.key == key) {
                return true;
            }
        }
        return false;
    }

    private int hash(K key) {
        return (key.hashCode() + size) % size;
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new HashmapIterator();
    }

    /**
     * Print function.
     * We print our hashmap with format (K V).
     */
    public void print() {
        for (int i = 0; i < size; i++) {
            if (map[i] != null) {
                for (Entry<K, V> entry : map[i]) {
                    System.out.println(entry);
                }
            }
        }
    }

    /**
     * Class for making a pair of Key and Value.
     *
     * @param <K> - Key parameter.
     * @param <V> - Value parameter.
     */
    public static class Entry<K, V> {
        public final K key;
        public V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return key.toString() + " " + value;
        }
    }

    private class HashmapIterator implements Iterator<Entry<K, V>> {
        private int bucket;
        private int entry;
        private Entry<K, V> current;
        private final int expectedMod;

        HashmapIterator() {
            bucket = 0;
            entry = 0;
            current = null;
            expectedMod = currentMod;
        }

        @Override
        public boolean hasNext() {
            while (bucket < size) {
                if (map[bucket] != null) {
                    if (entry < map[bucket].size()) {
                        return true;
                    }
                }
                bucket++;
                entry = 0;
            }
            return false;
        }

        @Override
        public Entry<K, V> next() {
            if (expectedMod != currentMod) {
                throw new ConcurrentModificationException("concurrent modification");
            }
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            current = map[bucket].get(entry++);
            return current;
        }
    }
}
