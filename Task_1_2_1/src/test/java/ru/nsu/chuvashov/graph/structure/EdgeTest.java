package ru.nsu.chuvashov.graph.structure;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

class EdgeTest {
    @Test
    void equalsEdge() {
        Edge<Integer> e = new Edge<>(new Vertex<>(1), new Vertex<>(2), 1);
        Edge<Integer> e2 = new Edge<>(new Vertex<>(1), new Vertex<>(2), 1);
        assertEquals(e, e2);
        assertEquals(e, e);
        assertNotEquals(e, null);
        assertNotEquals(e, new Object());

        Edge<Integer> e3 = new Edge<>(new Vertex<>(2), new Vertex<>(2), 1);
        assertNotEquals(e, e3);
        Edge<Integer> e4 = new Edge<>(new Vertex<>(1), new Vertex<>(3), 1);
        assertNotEquals(e, e4);
        Edge<Integer> e5 = new Edge<>(new Vertex<>(1), new Vertex<>(2), 2);
        assertNotEquals(e, e5);
        Edge<String> e6 = new Edge<>(new Vertex<>("1"), new Vertex<>("2"), 1);
        assertNotEquals(e, e6);
    }

    @Test
    void hashCodeEdge() {
        Edge<Integer> e = new Edge<>(new Vertex<>(1), new Vertex<>(2), 1);
        assertEquals(1024, e.hashCode());
    }

    @Test
    void toStringEdge() {
        Edge<Integer> e = new Edge<>(new Vertex<>(1), new Vertex<>(2), 1);
        assertEquals("Edge{from=1, to=2, weight=1}", e.toString());
    }
}