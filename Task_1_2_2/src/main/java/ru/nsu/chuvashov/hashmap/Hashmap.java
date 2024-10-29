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
public class Hashmap<K, V> implements Iterable<K> {
    private final List<LinkedList<K>> keys;
    private final List<LinkedList<V>> values;
    private final int size = 1000000;

    /**
     * Constructor for hashmap.
     */
    public Hashmap() {
        keys = new ArrayList<>(size);
        values = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            keys.add(new LinkedList<>());
            values.add(new LinkedList<>());
        }
    }

    /**
     * Put element in hashmap by the key.
     *
     * @param key - our key.
     * @param value - our value.
     */
    public void put(K key, V value) {
        int index = hash(key);
        if (keys.get(index).contains(key)) {
            throw new IllegalArgumentException("Key already exists");
        }
        keys.add(index, new LinkedList<>());
        values.add(index, new LinkedList<>());

        keys.get(index).add(key);
        values.get(index).add(value);
    }

    public void delete(K key, V value) {
        int index = hash(key);
        if (!keys.get(index).contains(key)) {
            throw new NoSuchElementException("Key does not exist");
        }
        if (values.get(index).get(keys.get(index).indexOf(key)) != value) {
            throw new IllegalArgumentException("Presented value does not match");
        }
        values.get(index).remove(keys.get(index).indexOf(key));
        keys.get(index).remove(key);
    }

    public void update(K key, V value) {
        int index = hash(key);
        if (!keys.get(index).contains(key)) {
            throw new NoSuchElementException("Key does not exist");
        }
        values.get(index).set(keys.get(index).indexOf(key), value);
    }

    public V get(K key) {
        int index = hash(key);
        if (!keys.get(index).contains(key)) {
            throw new NoSuchElementException("Key does not exist");
        }
        return values.get(index).get(keys.get(index).indexOf(key));
    }

    public boolean contains(K key) {
        int index = hash(key);
        return keys.get(index).contains(key);
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
    public Iterator<K> iterator() {
        return null;
    }


    public void print() {
        for (int j = 0; j < keys.size(); j++) {
            for (int i = 0; i < keys.get(j).size(); i++) {
                System.out.println(keys.get(j).get(i) + " " + values.get(j).get(i));
            }
        }
    }
}
