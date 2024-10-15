package ru.nsu.chuvashov.graph.structure;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VertexTest {

    @Test
    void getName() {
        Vertex v = new Vertex(2);
        assertEquals(2, v.getName());
    }

    @Test
    void testEquals() {
        Vertex v = new Vertex(1);
        Vertex v2 = new Vertex(1);
        assertEquals(v, v2);
        assertEquals(v, v);
        assertNotEquals(v, null);
        assertNotEquals(v, new Object());

        Vertex v3 = new Vertex(2);
        assertNotEquals(v, v3);
    }

    @Test
    void testToString() {
        Vertex v = new Vertex(1);
        assertEquals("1", v.toString());
    }
}