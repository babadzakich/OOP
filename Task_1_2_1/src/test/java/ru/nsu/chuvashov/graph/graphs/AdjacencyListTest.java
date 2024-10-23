package ru.nsu.chuvashov.graph.graphs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
import ru.nsu.chuvashov.graph.additions.Algorithms;
import ru.nsu.chuvashov.graph.structure.Edge;
import ru.nsu.chuvashov.graph.structure.Vertex;

class AdjacencyListTest {

    @Test
    void addVertex() {
        AdjacencyList<Integer> list = new AdjacencyList<>();
        Vertex<Integer> v1 = new Vertex<>(1);
        list.addVertex(v1);
        assertTrue(list.getVertexes().contains(v1));

        try {
            list.addVertex(v1);
        } catch (IllegalArgumentException e) {
            assertInstanceOf(IllegalArgumentException.class, e);
        }
    }

    @Test
    void addEdge() {
        AdjacencyList<Integer> list = new AdjacencyList<>();
        Vertex<Integer> v1 = new Vertex<>(1);
        list.addVertex(v1);
        Vertex<Integer> v2 = new Vertex<>(2);
        list.addVertex(v2);
        Edge<Integer> e1 = new Edge<>(v1, v2, 1);
        list.addEdge(e1);
        Vertex<Integer> v3 = new Vertex<>(3);
        list.addVertex(v3);
        Edge<Integer> e2 = new Edge<>(v2, v3, 1);
        list.addEdge(e2);
        Edge<Integer> e3 = new Edge<>(v3, v1, 1);
        list.addEdge(e3);
        assertTrue(list.getNeighbors(v1).contains(v2)
                && list.getNeighbors(v2).contains(v3));

        try {
            list.addEdge(new Edge<>(v1, new Vertex<>(4), 1));
        } catch (IllegalArgumentException e) {
            assertInstanceOf(IllegalArgumentException.class, e);
        }
    }

    @Test
    void getNeighbors() {
        AdjacencyList<Integer> list = new AdjacencyList<>();
        Vertex<Integer> v1 = new Vertex<>(1);
        list.addVertex(v1);
        Vertex<Integer> v2 = new Vertex<>(2);
        list.addVertex(v2);
        list.addEdge(new Edge<>(v1, v2, 1));
        Vertex<Integer> v3 = new Vertex<>(3);
        list.addVertex(v3);
        list.addEdge(new Edge<>(v1, v3, 1));
        Vertex<Integer> v4 = new Vertex<>(4);
        list.addVertex(v4);
        list.addEdge(new Edge<>(v1, v4, 1));

        ArrayList<Vertex<Integer>> check = new ArrayList<>();
        check.add(v2);
        check.add(v3);
        check.add(v4);
        List<Vertex<Integer>> neighbors = list.getNeighbors(v1);
        assertEquals(neighbors, check);

        assertEquals(list.getNeighbors(v2), new ArrayList<>());

        try {
            list.getNeighbors(new Vertex<>(5));
        } catch (NoSuchElementException e) {
            assertInstanceOf(NoSuchElementException.class, e);
        }
    }


    @Test
    void readFromFile() {
    }

    @Test
    void toposort() {
        AdjacencyList<Integer> list = new AdjacencyList<>();
        Vertex<Integer> v0 = new Vertex<>(0);
        list.addVertex(v0);
        Vertex<Integer> v1 = new Vertex<>(1);
        list.addVertex(v1);
        Vertex<Integer> v2 = new Vertex<>(2);
        list.addVertex(v2);
        Vertex<Integer> v3 = new Vertex<>(3);
        list.addVertex(v3);
        Vertex<Integer> v4 = new Vertex<>(4);
        list.addVertex(v4);
        Vertex<Integer> v5 = new Vertex<>(5);
        list.addVertex(v5);

        list.addEdge(new Edge<>(v3, v1, 1));
        list.addEdge(new Edge<>(v2, v3, 1));
        list.addEdge(new Edge<>(v4, v1, 1));
        list.addEdge(new Edge<>(v4, v0, 1));
        list.addEdge(new Edge<>(v5, v2, 1));
        list.addEdge(new Edge<>(v5, v0, 1));

        ArrayList<Vertex<Integer>> check = new ArrayList<>();
        check.add(v0);
        check.add(v1);
        check.add(v3);
        check.add(v2);
        check.add(v4);
        check.add(v5);
        List<Vertex<Integer>> tsort = Algorithms.toposort(list);
        assertEquals(check, tsort);
    }
}