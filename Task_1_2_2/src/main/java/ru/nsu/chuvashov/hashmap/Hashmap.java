package ru.nsu.chuvashov.hashmap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Hashmap <K,V> implements Iterable<K>{
    private final List<LinkedList<K>> keys;
    private final List<LinkedList<V>> values;
    private final int size = 1000000;

    public Hashmap() {
        keys = new ArrayList<>(size);
        values = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            keys.add(new LinkedList<>());
            values.add(new LinkedList<>());
        }
    }

    public void put(K key, V value) {
        int index = hash(key) % size;
        if (keys.get(index).contains(key)) {
            throw new IllegalArgumentException("Key already exists");
        }
        keys.add(index, new LinkedList<>());
        values.add(index, new LinkedList<>());

        keys.get(index).add(key);
        values.get(index).add(value);
    }

    public void delete(K key, V value) {
        int index = hash(key) % size;
        if (!keys.get(index).contains(key)) {
            throw new IllegalArgumentException("Key does not exist");
        }
        if (values.get(index).get(keys.get(index).indexOf(key)) != value) {
            throw new IllegalArgumentException("Presented value does not match");
        }
        values.get(index).remove(keys.get(index).indexOf(key));
        keys.get(index).remove(key);
    }

    public void update(K key, V value) {
        int index = hash(key) % size;
        if (!keys.get(index).contains(key)) {
            throw new IllegalArgumentException("Key does not exist");
        }
        values.get(index).set(keys.get(index).indexOf(key), value);
    }

    public V get(K key) {
        int index = hash(key) % size;
        if (!keys.get(index).contains(key)) {
            throw new IllegalArgumentException("Key does not exist");
        }
        return values.get(index).get(keys.get(index).indexOf(key));
    }

    public boolean contains(K key) {
        int index = hash(key) % size;
        return keys.get(index).contains(key);
    }



    private int hash(K key) {
        return key.hashCode();
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
        for (LinkedList<K> key : keys) {
            for (int i = 0; i < key.size(); i++) {
                System.out.println(key.get(i) + " " + values.get(i).get(i));
            }
        }
    }
}
