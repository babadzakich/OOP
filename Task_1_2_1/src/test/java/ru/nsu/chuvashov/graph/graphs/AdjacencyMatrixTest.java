package ru.nsu.chuvashov.graph.graphs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
import ru.nsu.chuvashov.graph.structure.Edge;
import ru.nsu.chuvashov.graph.structure.Vertex;

class AdjacencyMatrixTest {

    @Test
    void addVertex() {
        AdjacencyMatrix<Integer> matrix = new AdjacencyMatrix<>();
        matrix.addVertex(new Vertex<>(1));
        matrix.addVertex(new Vertex<>(2));
        assertEquals(matrix.getNeighbors(new Vertex<>(1)).size(), 0);
    }

    @Test
    void addEdge() {
        AdjacencyMatrix<Integer> matrix = new AdjacencyMatrix<>();
        matrix.addVertex(new Vertex<>(1));
        matrix.addVertex(new Vertex<>(2));
        matrix.addEdge(new Edge<>(new Vertex<>(1), new Vertex<>(2), 1));
        assertEquals(matrix.getNeighbors(new Vertex<>(1)).getFirst(), new Vertex<>(2));
        try {
            matrix.addEdge(new Edge<>(new Vertex<>(1), new Vertex<>(3), 1));
        } catch (IllegalArgumentException e) {
            assertInstanceOf(IllegalArgumentException.class, e);
        }
    }

    @Test
    void getNeighbors() {
        AdjacencyMatrix<Integer> matrix = new AdjacencyMatrix<>();

        Vertex<Integer> v1 = new Vertex<>(1);
        matrix.addVertex(v1);
        Vertex<Integer> v2 = new Vertex<>(2);
        matrix.addVertex(v2);
        Vertex<Integer> v3 = new Vertex<>(3);
        matrix.addVertex(v3);

        Edge<Integer> e1 = new Edge<>(v1, v2, 1);
        Edge<Integer> e2 = new Edge<>(v1, v3, 1);
        matrix.addEdge(e1);
        matrix.addEdge(e2);

        List<Vertex<Integer>> neighbors = matrix.getNeighbors(v1);
        ArrayList<Vertex<Integer>> check = new ArrayList<>();
        check.add(v2);
        check.add(v3);
        assertEquals(check, neighbors);

        try {
            matrix.getNeighbors(new Vertex<>(4));
        } catch (NoSuchElementException e) {
            assertInstanceOf(NoSuchElementException.class, e);
        }

        ArrayList<Vertex<Integer>> check2 = new ArrayList<>();
        check2.add(v1);
        check2.add(v2);
        check2.add(v3);
        assertEquals(check2, matrix.getVertexes());
    }

    @Test
    void readFromFile() {
    }
}