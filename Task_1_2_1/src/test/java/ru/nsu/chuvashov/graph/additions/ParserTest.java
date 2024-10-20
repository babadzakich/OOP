package ru.nsu.chuvashov.graph.additions;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import ru.nsu.chuvashov.graph.Graph;
import ru.nsu.chuvashov.graph.graphs.AdjacencyList;
import ru.nsu.chuvashov.graph.graphs.AdjacencyMatrix;
import ru.nsu.chuvashov.graph.graphs.IncidenceMatrix;
import ru.nsu.chuvashov.graph.structure.Vertex;

class ParserTest {

    /**
     * Tests for problems in parsing.
     * First test is for correctly written graph.
     * Second test is trying to read from empty file.
     * Third test is trying to read from nonexistent file.
     * Fourth test is trying to read from file with wrong number of first values.
     * Fifth test is lesser number of vertexes than was said earlier.
     * Sixth test is lesser number of edges than was said earlier.
     * Seventh test is not having 3 attributes in edge string.
     * Eight test is having wrong type of vertex than presented.
     */
    @Test
    void parse() {
        Graph<Integer> graph = new AdjacencyList<>();
        graph = graph.readFromFile("graphfile.txt", Integer::parseInt);
        assertTrue(graph.getVertexes().contains(new Vertex<>(2)));

        Graph<Integer> graph2 = new IncidenceMatrix<>();
        try {
            graph2 = graph2.readFromFile("graphtest1.txt", Integer::parseInt);
        } catch (IllegalArgumentException e) {
            assertInstanceOf(IllegalArgumentException.class, e);
        }

        try {
            graph2 = graph2.readFromFile("emptyFile.txt", Integer::parseInt);
        } catch (IllegalArgumentException e) {
            assertInstanceOf(IllegalArgumentException.class, e);
        }

        try {
            graph2.readFromFile("graphtest3.txt", Integer::parseInt);
        } catch (IllegalArgumentException e) {
            assertInstanceOf(IllegalArgumentException.class, e);
        }

        Graph<Integer> graph3 = new AdjacencyMatrix<>();

        try {
            graph3 = graph3.readFromFile("graphtest4.txt", Integer::parseInt);
        } catch (IllegalArgumentException e) {
            assertInstanceOf(IllegalArgumentException.class, e);
        }

        try {
            graph3 = graph3.readFromFile("graphtest5.txt", Integer::parseInt);
        } catch (IllegalArgumentException e) {
            assertInstanceOf(IllegalArgumentException.class, e);
        }

        try {
            graph3 = graph3.readFromFile("graphtest6.txt", Integer::parseInt);
        } catch (IllegalArgumentException e) {
            assertInstanceOf(IllegalArgumentException.class, e);
        }

        try {
            graph3.readFromFile("graphtest7.txt", Integer::parseInt);
        } catch (IllegalArgumentException e) {
            assertInstanceOf(IllegalArgumentException.class, e);
        }
    }
}