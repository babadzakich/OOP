package ru.nsu.chuvashov.graph.structure;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    void testToString() {
        Vertex<Integer> v = new Vertex<>(1);
        assertEquals("Vertex[name=1]", v.toString());
    }
}