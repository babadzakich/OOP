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
public class Hashmap<K, V> implements Iterable<Hashmap.Entry<K, V>> {
    private List<List<Entry<K, V>>> map;
    private final int size;
    private int currentMod;
    private int capacity;

    /**
     * Constructor for hashmap with default size.
     */
    public Hashmap() {
        currentMod = 0;
        capacity = 0;
        size = 16;
        map = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            map.add(new LinkedList<>());
        }
    }

    /**
     * Constructor for hashmap with given capacity.
     *
     * @param capacity - our hashmap capacity.
     */
    public Hashmap(int capacity) {
        currentMod = 0;
        this.size = capacity;
        map = new ArrayList<>(capacity);
        for (int i = 0; i < capacity; i++) {
            map.add(new LinkedList<>());
        }
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
        for (Entry<K, V> entry : map.get(hash)) {
            if (entry.key == key) {
                throw new IllegalArgumentException("key already exists");
            }
        }
        map.get(hash).add(new Entry<>(key, value));
        currentMod++;
        capacity++;
        double loadFactor = 0.75;
        if (capacity * loadFactor >= size) {
            resize();
        }
    }

    /**
     * If our map capacity divided by size exceeds load factor,
     * we resize our hashmap to avoid huge collisions.
     */
    private void resize() {
        capacity *= 2;
        List<List<Entry<K, V>>> newMap = new ArrayList<>(capacity);
        for (int i = 0; i < capacity; i++) {
            newMap.add(new LinkedList<>());
        }
        for (List<Entry<K, V>> list : map) {
            for (Entry<K, V> entry : list) {
                newMap.get(hash(entry.key)).add(new Entry<>(entry.key, entry.value));
            }
        }
        map.clear();
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
        for (Entry<K, V> entry : map.get(index)) {
            if (entry.key == key && entry.value == value) {
                map.get(index).remove(entry);
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
        for (Entry<K, V> entry : map.get(index)) {
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
        for (Entry<K, V> entry : map.get(index)) {
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
        for (Entry<K, V> entry : map.get(index)) {
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
    public Iterator<Entry<K, V>> iterator() {
        return new HashmapIterator();
    }

    /**
     * Print function.
     * We print our hashmap with format (K V).
     */
    public void print() {
        for (int i = 0; i < size; i++) {
            for (Entry<K, V> entry : map.get(i)) {
                System.out.println(entry);
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
        public Entry<K, V> next() {
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
