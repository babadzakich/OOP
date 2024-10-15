package ru.nsu.chuvashov.graph.graphs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import org.junit.jupiter.api.Test;
import ru.nsu.chuvashov.graph.structure.Edge;
import ru.nsu.chuvashov.graph.structure.Vertex;

class AdjacencyMatrixTest {

    @Test
    void addVertex() {
        AdjacencyMatrix matrix = new AdjacencyMatrix();
        matrix.addVertex(new Vertex(1));
        matrix.addVertex(new Vertex(2));
        assertEquals(matrix.getNeighbors(new Vertex(1)).size(), 0);
    }

    @Test
    void addEdge() {
        AdjacencyMatrix matrix = new AdjacencyMatrix();
        matrix.addVertex(new Vertex(1));
        matrix.addVertex(new Vertex(2));
        matrix.addEdge(new Edge(new Vertex(1), new Vertex(2)));
        assertEquals(matrix.getNeighbors(new Vertex(1)).getFirst(), new Vertex(2));
        try {
            matrix.addEdge(new Edge(new Vertex(1), new Vertex(3)));
        } catch (IllegalArgumentException e) {
            assertInstanceOf(IllegalArgumentException.class, e);
        }
    }

    @Test
    void getNeighbors() {

    }
}