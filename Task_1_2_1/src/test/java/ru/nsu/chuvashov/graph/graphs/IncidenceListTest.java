package ru.nsu.chuvashov.graph.graphs;

import org.junit.jupiter.api.Test;
import ru.nsu.chuvashov.graph.structure.Edge;
import ru.nsu.chuvashov.graph.structure.Vertex;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class IncidenceListTest {

    @Test
    void addVertex() {
        IncidenceList list = new IncidenceList();
        list.addVertex(new Vertex(1));
        assertTrue(list.incidenceList.containsKey(new Vertex(1)));
    }

    @Test
    void addEdge() {
        IncidenceList list = new IncidenceList();
        Vertex v1 = new Vertex(1);
        list.addVertex(v1);
        Vertex v2 = new Vertex(2);
        list.addVertex(v2);
        Edge e1 = new Edge(v1, v2);
        list.addEdge(e1);
        Vertex v3 = new Vertex(3);
        list.addVertex(v3);
        Edge e2 = new Edge(v2, v3);
        list.addEdge(e2);
        Edge e3 = new Edge(v3, v1);
        list.addEdge(e3);
        assertTrue(list.incidenceList.get(v1).contains(e1)
                && list.incidenceList.get(v2).contains(e2));
    }

    @Test
    void getNeighbors() {
        IncidenceList list = new IncidenceList();
        Vertex v1 = new Vertex(1);
        list.addVertex(v1);
        Vertex v2 = new Vertex(2);
        list.addVertex(v2);
        list.addEdge(new Edge(v1, v2));
        Vertex v3 = new Vertex(3);
        list.addVertex(v3);
        list.addEdge(new Edge(v1, v3));
        Vertex v4 = new Vertex(4);
        list.addVertex(v4);
        list.addEdge(new Edge(v1, v4));

        ArrayList<Vertex> check = new ArrayList<>();
        check.add(v2);
        check.add(v3);
        check.add(v4);
        ArrayList<Vertex> neighbors = list.getNeighbors(v1);
        assertEquals(neighbors, check);

        assertEquals(list.getNeighbors(v2), new ArrayList<>());

        try {
            list.getNeighbors(new Vertex(5));
        } catch (NoSuchElementException e) {
            assertInstanceOf(NoSuchElementException.class, e);
        }
    }


    @Test
    void readFromFile() {
    }

    @Test
    void toposort() {
        IncidenceList list = new IncidenceList();
        Vertex v0 = new Vertex(0);
        list.addVertex(v0);
        Vertex v1 = new Vertex(1);
        list.addVertex(v1);
        Vertex v2 = new Vertex(2);
        list.addVertex(v2);
        Vertex v3 = new Vertex(3);
        list.addVertex(v3);
        Vertex v4 = new Vertex(4);
        list.addVertex(v4);
        Vertex v5 = new Vertex(5);
        list.addVertex(v5);

        list.addEdge(new Edge(v3, v1));
        list.addEdge(new Edge(v2, v3));
        list.addEdge(new Edge(v4, v1));
        list.addEdge(new Edge(v4, v0));
        list.addEdge(new Edge(v5, v2));
        list.addEdge(new Edge(v5, v0));

        ArrayList<Vertex> check = new ArrayList<>();
        check.add(v0);
        check.add(v1);
        check.add(v3);
        check.add(v2);
        check.add(v5);
        check.add(v4);
        ArrayList<Vertex> tSort = list.toposort();
        assertEquals(check, tSort);
    }
}