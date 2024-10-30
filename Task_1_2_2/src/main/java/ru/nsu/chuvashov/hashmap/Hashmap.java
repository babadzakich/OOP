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
public class Hashmap<K, V> implements Iterable<Hashmap.Entry<K,V>> {
    private final List<List<Entry<K,V>>> map;
    private final int size = 1000000;
    private int currentMod;

    /**
     * Constructor for hashmap.
     */
    public Hashmap() {
        currentMod = 0;
        map = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            map.add(new LinkedList<>());
        }
    }

    /**
     * Put element in hashmap by the key.
     *
     * @param key - our key.
     * @param value - our value.
     */
    public void put(K key, V value) {
        int hash = hash(key);
        for (Entry<K,V> entry : map.get(hash)) {
            if (entry.key == key) {
                throw new IllegalArgumentException("key already exists");
            }
        }
        map.get(hash).add(new Entry<>(key, value));
        currentMod++;
    }

    public void delete(K key, V value) {
        int index = hash(key);
        for (Entry<K,V> entry : map.get(index)) {
            if (entry.key == key && entry.value == value) {
                map.get(index).remove(entry);
                currentMod++;
                return;
            } else if (entry.key == key) {
                throw new IllegalArgumentException("Value differs from hashed one");
            }
        }
        throw new NoSuchElementException("key not found");
    }

    public void update(K key, V value) {
        int index = hash(key);
        for (Entry<K,V> entry : map.get(index)) {
            if (entry.key == key) {
                entry.value = value;
                currentMod++;
                return;
            }
        }
        throw new NoSuchElementException("key not found");
    }

    public V get(K key) {
        int index = hash(key);
        for (Entry<K,V> entry : map.get(index)) {
            if (entry.key == key) {
                return entry.value;
            }
        }
        throw new NoSuchElementException("key not found");
    }

    public boolean contains(K key) {
        int index = hash(key);
        for (Entry<K,V> entry : map.get(index)) {
            if (entry.key == key) {
                return true;
            }
        }
        return false;
    }

    private int hash(K key) {
        return key.hashCode() % size;
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<Entry<K,V>> iterator() {
        return new HashmapIterator();
    }

    public void print() {
        for (int i = 0; i < size; i++) {
            for (Entry<K,V> entry : map.get(i)) {
                System.out.println(entry);
            }
        }
    }

    public static class Entry<K,V> {
        private final K key;
        private V value;
        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return key.toString() + " " + value;
        }
    }

    private class HashmapIterator implements Iterator<Entry<K,V>> {
        private int bucket;
        private int entry;
        private Entry<K,V> current;
        private final int expectedMod;

        HashmapIterator() {
            bucket = 0;
            entry = 0;
            current = null;
            expectedMod = currentMod;
        }

        /**
         * Returns {@code true} if the iteration has more elements.
         * (In other words, returns {@code true} if {@link #next} would
         * return an element rather than throwing an exception.)
         *
         * @return {@code true} if the iteration has more elements
         */
        @Override
        public boolean hasNext() {
            while (bucket < size) {
                if (entry < map.get(bucket).size()) {
                    return true;
                }
                bucket++;
                entry = 0;
            }
            return false;
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration
         * @throws NoSuchElementException if the iteration has no more elements
         */
        @Override
        public Entry<K,V> next() {
            if (expectedMod != currentMod) {
                throw new ConcurrentModificationException();
            }
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            current = map.get(bucket).get(entry++);
            return current;
        }
    }
}
