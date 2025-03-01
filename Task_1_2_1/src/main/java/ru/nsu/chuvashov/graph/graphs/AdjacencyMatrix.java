package ru.nsu.chuvashov.graph.graphs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;
import ru.nsu.chuvashov.graph.Graph;
import ru.nsu.chuvashov.graph.additions.Parser;
import ru.nsu.chuvashov.graph.structure.Edge;
import ru.nsu.chuvashov.graph.structure.Vertex;

/**
 * Class that implements graph using adjacency matrix.
 * Graph is simple oriented and with weighted edges.
 *
 * @param <T> - type of vertexes in graph.
 */
public class AdjacencyMatrix<T> implements Graph<T> {
    private final List<List<Integer>> matrix;
    private final List<Vertex<T>> vertices;

    public AdjacencyMatrix() {
        vertices = new ArrayList<>();
        matrix = new ArrayList<>();
    }

    @Override
    public void addVertex(Vertex<T> vertex) {
        if (vertices.contains(vertex)) {
            throw new IllegalArgumentException("Vertex already exists");
        }
        for (List<Integer> integers : matrix) {
            integers.add(0);
        }
        vertices.add(vertex);
        matrix.add(new ArrayList<>(vertices.size()));
        for (int i = 0; i < vertices.size(); i++) {
            matrix.getLast().add(0);
        }
    }

    @Override
    public void addEdge(Edge<T> edge) {
        if (!vertices.contains(edge.from()) || !vertices.contains(edge.to())) {
            throw new IllegalArgumentException("Graph doesn`t contain vertexes from edge");
        }
        int x = matrix.get(vertices.indexOf(edge.from())).get(vertices.indexOf(edge.to()));
        x += edge.weight();
        matrix.get(vertices.indexOf(edge.from())).set(vertices.indexOf(edge.to()), x);
    }

    @Override
    public List<Vertex<T>> getNeighbors(Vertex<T> vertex) {
        List<Vertex<T>> neighbors = new ArrayList<>();
        if (!vertices.contains(vertex)) {
            throw new NoSuchElementException("Vertex not found");
        }
        for (int i = 0; i < matrix.get(vertices.indexOf(vertex)).size(); i++) {
            if (matrix.get(vertices.indexOf(vertex)).get(i) > 0) {
                neighbors.add(vertices.get(i));
            }
        }
        return neighbors;
    }

    @Override
    public Graph<T> readFromFile(String fileName, Function<String, T> parser) throws IOException {
        Graph<T> graph = new AdjacencyMatrix<>();
        Parser.parse(graph, fileName, parser);
        return graph;
    }

    /**
     * A list of vertexes in graph.
     *
     * @return list of vertices in graph.
     */
    @Override
    public List<Vertex<T>> getVertexes() {
        return vertices;
    }
}
