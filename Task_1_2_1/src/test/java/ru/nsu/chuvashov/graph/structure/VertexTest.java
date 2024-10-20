package ru.nsu.chuvashov.graph.structure;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

class VertexTest {

    @Test
    void getName() {
        Vertex<Integer> v = new Vertex<>(2);
        assertEquals(2, v.name());
        Vertex<Boolean> v2 = new Vertex<>(true);
        assertTrue(v2.name());
    }

    @Test
    void testEquals() {
        Vertex<Integer> v = new Vertex<>(1);
        Vertex<Integer> v2 = new Vertex<>(1);
        assertEquals(v, v2);
        assertEquals(v, v);
        assertNotEquals(v, null);
        assertNotEquals(v, new Object());

        Vertex<Integer> v3 = new Vertex<>(2);
        assertNotEquals(v, v3);
    }

    @Test
    void testToString() {
        Vertex<Integer> v = new Vertex<>(1);
        assertEquals("1", v.toString());
    }
}