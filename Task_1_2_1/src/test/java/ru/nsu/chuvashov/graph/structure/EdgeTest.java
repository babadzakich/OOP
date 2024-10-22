package ru.nsu.chuvashov.graph.structure;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class EdgeTest {
    @Test
    void toStringEdge() {
        Edge<Integer> e = new Edge<>(new Vertex<>(1), new Vertex<>(2), 1);
        assertEquals("Edge{from=Vertex[name=1], to=Vertex[name=2], weight=1}", e.toString());
    }
}