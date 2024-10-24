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
 * Graph implementation using Incidence matrix.
 * Graph is simple, oriented and weighted.
 *
 * @param <T> - type of vertexes in graph.
 */
public class IncidenceMatrix<T> implements Graph<T> {
    private final List<Vertex<T>> vertexes;
    private final List<Edge<T>> edges;
    private final List<List<Integer>> matrix;

    /**
     * I don`t know why, but reviewdog needs javadoc for constructor.
     */
    public IncidenceMatrix() {
        vertexes = new ArrayList<>();
        edges = new ArrayList<>();
        matrix = new ArrayList<>();
    }

    /**
     * Addition of vertex.
     *
     * @param vertex - vertex that we add.
     */
    @Override
    public void addVertex(Vertex<T> vertex) {
        if (vertexes.contains(vertex)) {
            throw new IllegalArgumentException("Vertex already exists");
        }
        vertexes.add(vertex);
        matrix.add(new ArrayList<>());
        for (int i = 0; i < edges.size(); i++) {
            matrix.getLast().add(0);
        }
    }

    /**
     * We add edge if the vertexes of edge are presented.
     *
     * @param edge - edge that we add.
     */
    @Override
    public void addEdge(Edge<T> edge) {
        if (!vertexes.contains(edge.to()) || !vertexes.contains(edge.from())) {
            throw new NoSuchElementException("Graph doesn`t contain vertexes from edge");
        }
        edges.add(edge);
        for (int i = 0; i < vertexes.size(); i++) {
            if (vertexes.get(i).equals(edge.from())) {
                matrix.get(i).add(-(edge.weight()));
            } else if (vertexes.get(i).equals(edge.to())) {
                matrix.get(i).add(edge.weight());
            } else {
                matrix.get(i).add(0);
            }
        }
    }

    /**
     * We search for neighbours of vertex.
     *
     * @param vertex - vertex, which neighbours we search.
     * @return list of neighbours.
     */
    @Override
    public List<Vertex<T>> getNeighbors(Vertex<T> vertex) {
        List<Vertex<T>> neighbors = new ArrayList<>();
        int index = vertexes.indexOf(vertex);
        if (index == -1) {
            throw new NoSuchElementException("Vertex not found");
        }
        for (int i = 0; i < matrix.get(index).size(); i++) {
            if (matrix.get(index).get(i) < 0) {
                neighbors.add(edges.get(i).to());
            }
        }
        return neighbors;
    }

    /**
     * We read graph from formatted input,
     * first we have two numbers n,m:
     * n - number of vertexes, m - number of edges.
     * We look through n integers and add n vertexes.
     * After that we search through m edges where each edge must be on different line,
     * first number is from, second is to.
     *
     * @param fileName - file, where we search.
     * @param parser - function to parse our vertex type.
     */
    @Override
    public Graph<T> readFromFile(String fileName, Function<String, T> parser) throws IOException {
        Graph<T> graph = new IncidenceMatrix<>();
        Parser.parse(graph, fileName, parser);
        return graph;
    }

    /**
     * We search for list of all vertices of graph.
     *
     * @return list of vertices.
     */
    @Override
    public List<Vertex<T>> getVertexes() {
        return vertexes;
    }

}
