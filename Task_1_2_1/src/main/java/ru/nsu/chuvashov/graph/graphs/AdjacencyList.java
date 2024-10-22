package ru.nsu.chuvashov.graph.graphs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;
import ru.nsu.chuvashov.graph.Graph;
import ru.nsu.chuvashov.graph.additions.Parser;
import ru.nsu.chuvashov.graph.structure.Edge;
import ru.nsu.chuvashov.graph.structure.Vertex;

/**
 * Graph implementation using vector.
 *
 * @param <T> - our type of vertexes.
 */
public class AdjacencyList<T> implements Graph<T> {
    private final Map<Vertex<T>, List<Edge<T>>> adjacencyList;

    public AdjacencyList() {
        adjacencyList = new HashMap<>();
    }

    /**
     * We put our vertex in hashmap,
     * and create for her array list of neighbours.
     *
     * @param vertex - vertex that we add.
     */
    @Override
    public void addVertex(Vertex<T> vertex) {
        if (!adjacencyList.containsKey(vertex)) {
            adjacencyList.put(vertex, new ArrayList<>());
        } else {
            throw new IllegalArgumentException("Vertex already exists");
        }
    }

    /**
     * We just add edge to vertex from which edge goes.
     *
     * @param edge - edge that we add.
     */
    @Override
    public void addEdge(Edge<T> edge) {
        if (!adjacencyList.containsKey(edge.from())
                || !adjacencyList.containsKey(edge.to())) {
            throw new IllegalArgumentException("Graph doesn`t contain vertexes from edge");
        }
        adjacencyList.get(edge.from()).add(edge);
    }

    /**
     * We jst take arraylist of needed vertex(if exists),
     * and get every vertex To from all edges in list.
     *
     * @param vertex - vertex, which neighbours we search.
     * @return - list of neighbours.
     */
    @Override
    public List<Vertex<T>> getNeighbors(Vertex<T> vertex) {
        if (!adjacencyList.containsKey(vertex)) {
            throw new NoSuchElementException("Vertex not found");
        }
        ArrayList<Vertex<T>> result = new ArrayList<>(adjacencyList.get(vertex).size());
        for (Edge<T> edge : adjacencyList.get(vertex)) {
            result.add(edge.to());
        }
        return result;
    }

    /**
     * We read graph from formatted input,
     * first we have two numbers n,m:
     * n - number of vertexes, m - number of edges.
     * We look through n integers and add n vertexes.
     * After that we search through m edges where each edge must be on different line,
     * first number is from, second is to.
     *
     * @param fileName file, where we search.
     * @param parser  - function to parse our vertex type.
     * @return graph.
     */
    @Override
    public Graph<T> readFromFile(String fileName, Function<String, T> parser) throws IOException {
        Graph<T> graph = new AdjacencyList<>();
        Parser.parse(graph, fileName, parser);
        return graph;
    }

    /**
     * All vertexes from graph.
     *
     * @return list of our vertexes.
     */
    @Override
    public List<Vertex<T>> getVertexes() {
        return List.copyOf(adjacencyList.keySet());
    }
}
