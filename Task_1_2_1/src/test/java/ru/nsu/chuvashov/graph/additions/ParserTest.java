package ru.nsu.chuvashov.graph.additions;

import org.junit.jupiter.api.Test;
import ru.nsu.chuvashov.graph.Graph;
import ru.nsu.chuvashov.graph.graphs.AdjacencyList;
import ru.nsu.chuvashov.graph.graphs.AdjacencyMatrix;
import ru.nsu.chuvashov.graph.graphs.IncidenceMatrix;
import ru.nsu.chuvashov.graph.structure.Vertex;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for problems in parsing.
 */
class ParserTest {

    /**
     * Test is for correctly written graph.
     */
    @Test
    void parseIntCommon() throws IOException {
        Graph<Integer> graph = new AdjacencyList<>();
        graph = graph.readFromFile("graphfile.txt", Integer::parseInt);
        assertTrue(graph.getVertexes().contains(new Vertex<>(2)));
    }

    @Test
    void parseDoubleCommon() throws IOException {
        Graph<Double> graph = new AdjacencyList<>();
        graph = graph.readFromFile("graphfile2.txt", Double::parseDouble);
        assertTrue(graph.getVertexes().contains(new Vertex<>(1.25)));
    }

    /**
     * Test is trying to read from empty file.
     */
    @Test
    void readEmpty() {
        Graph<Integer> graph = new IncidenceMatrix<>();
        assertThrows(IllegalArgumentException.class, () -> graph.readFromFile("graphtest1.txt", Integer::parseInt));
    }

    /**
     * Test is trying to read from nonexistent file.
     */
    @Test
    void noFileTest() {
        Graph<Integer> graph = new AdjacencyList<>();
        assertThrows(FileNotFoundException.class, () -> graph.readFromFile("emptyFile.txt", Integer::parseInt));
    }

    /**
     * Test is trying to read from file with wrong number of first values.
     */
    @Test
    void wrongFirstValues() {
        Graph<Integer> graph = new AdjacencyMatrix<>();
        assertThrows(IllegalArgumentException.class, () -> graph.readFromFile("graphtest3.txt", Integer::parseInt));
    }

    /**
     * Test is lesser number of vertexes than was said earlier.
     */
    @Test
    void notEnoughVertexes() {
        Graph<Integer> graph = new AdjacencyMatrix<>();
        assertThrows(IllegalArgumentException.class, () -> graph.readFromFile("graphtest4.txt", Integer::parseInt));
    }

    /**
     * Test is lesser number of edges than was said earlier.
     */
    @Test
    void notEnoughEdges() {
        Graph<Integer> graph = new AdjacencyMatrix<>();
        assertThrows(IllegalArgumentException.class, () -> graph.readFromFile("graphtest5.txt", Integer::parseInt));
    }

    /**
     * Test is not having 3 attributes in edge string.
     */
    @Test
    void wrongEdgeAttr() {
        Graph<Integer> graph = new AdjacencyMatrix<>();
        assertThrows(IllegalArgumentException.class, () -> graph.readFromFile("graphtest6.txt", Integer::parseInt));
    }

    /**
     * Test is having wrong type of vertex than presented.
     */
    @Test
    void wrongType() {
        Graph<Integer> graph = new AdjacencyMatrix<>();
        assertThrows(IllegalArgumentException.class, () -> graph.readFromFile("graphtest7.txt", Integer::parseInt));
    }
}