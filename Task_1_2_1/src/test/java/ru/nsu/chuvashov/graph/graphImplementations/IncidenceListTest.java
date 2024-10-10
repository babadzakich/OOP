package ru.nsu.chuvashov.graph.graphImplementations;

import org.junit.jupiter.api.Test;
import ru.nsu.chuvashov.graph.baseStructure.Edge;
import ru.nsu.chuvashov.graph.baseStructure.Vertex;

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
        Edge e1 = Edge.createUnweightedEdge(new Vertex(1), new Vertex(2));
        list.addEdge(e1);
        Edge e2 = Edge.createUnweightedEdge(new Vertex(2), new Vertex(3));
        list.addEdge(e2);
        Edge e3 = Edge.createUnweightedEdge(new Vertex(3), new Vertex(1));
        list.addEdge(e3);
        assertTrue(list.incidenceList.get(new Vertex(1)).contains(e1)
                && list.incidenceList.get(new Vertex(2)).contains(e2));
    }

    @Test
    void getNeighbors() {
        IncidenceList list = new IncidenceList();
        list.addEdge(Edge.createUnweightedEdge(new Vertex(1), new Vertex(2)));
        list.addEdge(Edge.createUnweightedEdge(new Vertex(1), new Vertex(3)));
        list.addEdge(Edge.createUnweightedEdge(new Vertex(1), new Vertex(4)));

        ArrayList<Vertex> check = new ArrayList<>();
        check.add(new Vertex(2));
        check.add(new Vertex(3));
        check.add(new Vertex(4));
        ArrayList<Vertex> neighbors = list.getNeighbors(new Vertex(1));
        assertEquals(neighbors, check);

        assertEquals(list.getNeighbors(new Vertex(2)), new ArrayList<>());

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
        list.addEdge(Edge.createUnweightedEdge(new Vertex(5), new Vertex(2)));
        list.addEdge(Edge.createUnweightedEdge(new Vertex(5), new Vertex(0)));
        list.addEdge(Edge.createUnweightedEdge(new Vertex(2), new Vertex(3)));
        list.addEdge(Edge.createUnweightedEdge(new Vertex(3), new Vertex(1)));
        list.addEdge(Edge.createUnweightedEdge(new Vertex(4), new Vertex(1)));
        list.addEdge(Edge.createUnweightedEdge(new Vertex(4), new Vertex(0)));

        ArrayList<Vertex> check = new ArrayList<>();
        check.add(new Vertex(0));
        check.add(new Vertex(1));
        check.add(new Vertex(3));
        check.add(new Vertex(2));
        check.add(new Vertex(5));
        check.add(new Vertex(4));
        ArrayList<Vertex> tSort = list.toposort();
        assertEquals(check, tSort);
    }
}