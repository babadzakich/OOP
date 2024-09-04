package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class heapsortTest {

    @Test
    void heapsort() {
        assertArrayEquals(new int[]{1,5,6,9,10,12}, Heapsort.heapsort(new int[]{1,12,9,5,6,10}));
        assertArrayEquals(new int[]{1,1,6,9,10,12}, Heapsort.heapsort(new int[]{1,12,9,1,6,10}));
        assertArrayEquals(new int[]{1,5,6,9,10,12}, Heapsort.heapsort(new int[]{1,5,6,9,10,12}));
        assertArrayEquals(new int[]{1,1,1,1,1,1}, Heapsort.heapsort(new int[]{1,1,1,1,1,1}));
    }

    @Test
    void heapify() {
    }
}