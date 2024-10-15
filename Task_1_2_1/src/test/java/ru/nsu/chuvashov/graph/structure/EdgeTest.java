package ru.nsu.chuvashov.graph.structure;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

class EdgeTest {
    @Test
    void equalsEdge() {
        Edge e = new Edge(new Vertex(1), new Vertex(2));
        Edge e2 = new Edge(new Vertex(1), new Vertex(2));
        assertEquals(e, e2);
        assertEquals(e, e);
        assertNotEquals(e, null);
        assertNotEquals(e, new Object());

        Edge e3 = new Edge(new Vertex(2), new Vertex(2));
        assertNotEquals(e, e3);
        Edge e4 = new Edge(new Vertex(1), new Vertex(3));
        assertNotEquals(e, e4);
    }

    @Test
    void hashCodeEdge() {
        Edge e = new Edge(new Vertex(1), new Vertex(2));
        assertEquals(1023, e.hashCode());
    }

    @Test
    void toStringEdge() {
        Edge e = new Edge(new Vertex(1), new Vertex(2));
        assertEquals("Edge{from=1, to=2}", e.toString());
    }
}