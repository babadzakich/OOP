package ru.nsu.chuvashov.graph.baseStructure;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

class EdgeTest {
    @Test
    void createWeightedEdge() {
        Edge e = Edge.createWeightedEdge(new Vertex(1), new Vertex(2), 2);
        assertEquals(2, e.getWeight());
        assertEquals(new Vertex(2), e.getTo());
    }

    @Test
    void equalsEdge() {
        Edge e = Edge.createWeightedEdge(new Vertex(1), new Vertex(2), 2);
        Edge e2 = Edge.createWeightedEdge(new Vertex(1), new Vertex(2), 2);
        assertEquals(e, e2);
        assertEquals(e, e);
        assertNotEquals(e, null);
        assertNotEquals(e, new Object());

        Edge e3 = Edge.createWeightedEdge(new Vertex(2), new Vertex(2), 2);
        assertNotEquals(e, e3);
        Edge e4 = Edge.createWeightedEdge(new Vertex(1), new Vertex(3), 2);
        assertNotEquals(e, e4);
        Edge e5 = Edge.createWeightedEdge(new Vertex(1), new Vertex(2), 3);
        assertNotEquals(e, e5);
    }

    @Test
    void hashCodeEdge() {
        Edge e = Edge.createWeightedEdge(new Vertex(1), new Vertex(2), 2);
        assertEquals(31715, e.hashCode());
    }

    @Test
    void toStringEdge() {
        Edge e = Edge.createWeightedEdge(new Vertex(1), new Vertex(2), 2);
        assertEquals("Edge{from=1, to=2, weight=2}", e.toString());
    }
}