package ru.nsu.chuvashov.graph.graphs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
import ru.nsu.chuvashov.graph.structure.Edge;
import ru.nsu.chuvashov.graph.structure.Vertex;

class IncidenceListTest {

    @Test
    void addVertex() {
        IncidenceList<Integer> list = new IncidenceList<>();
        list.addVertex(new Vertex<>(1));
        assertTrue(list.incidenceList.containsKey(new Vertex<>(1)));
    }

    @Test
    void addEdge() {
        IncidenceList<Integer> list = new IncidenceList<>();
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
        assertTrue(list.incidenceList.get(v1).contains(e1)
                && list.incidenceList.get(v2).contains(e2));
    }

    @Test
    void getNeighbors() {
        IncidenceList<Integer> list = new IncidenceList<>();
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
        IncidenceList<Integer> list = new IncidenceList<>();
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