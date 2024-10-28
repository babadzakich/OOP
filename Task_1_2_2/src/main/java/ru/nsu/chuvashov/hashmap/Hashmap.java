package ru.nsu.chuvashov.hashmap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Hashmap <K,V> implements Iterable<K>{
    private final List<K> keys;
    private final List<LinkedList<V>> values;
    private final int size = 1000000;

    public Hashmap() {
        keys = new ArrayList<K>(size);
        values = new ArrayList<LinkedList<V>>(size);
    }

    public void put(K key, V value) {
        int index = hash(key) % size;
        keys.add(index, key);
        if (values.get(index).contains(value)) {
            return;
        } else {
            values.get(index).add(value);
        }
    }

    public void delete(K key, V value) {
        int index = hash(key) % size;
        keys.remove(index);
        values.get(index).remove(value);
    }

    public void update(K key, V value) {
        int index = hash(key) % size;
        values.get(index).clear();
        values.get(index).add(value);
    }

    public boolean contains(K key) {
        return keys.contains(key);
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
        for (K key : keys) {
            System.out.println(key + ": " + values.get(hash(key) % size));
        }
    }
}
