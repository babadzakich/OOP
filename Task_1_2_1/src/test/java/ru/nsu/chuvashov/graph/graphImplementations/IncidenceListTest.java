package ru.nsu.chuvashov.graph.graphImplementations;

import org.junit.jupiter.api.Test;
import ru.nsu.chuvashov.graph.baseStructure.Edge;
import ru.nsu.chuvashov.graph.baseStructure.Vertex;

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
    }

    @Test
    void readFromFile() {
    }

    @Test
    void toposort() {
    }
}