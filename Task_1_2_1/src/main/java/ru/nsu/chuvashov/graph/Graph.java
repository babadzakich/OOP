package ru.nsu.chuvashov.graph;

import java.util.List;
import java.util.function.Function;

import ru.nsu.chuvashov.graph.structure.Edge;
import ru.nsu.chuvashov.graph.structure.Vertex;

/**
 * Interface for simple weighted graph.
 */
public interface Graph <T> {
    /**
     * Addition of vertex.
     *
     * @param vertex - vertex that we add.
     */
    void addVertex(Vertex<T> vertex);

    /**
     * We add edge if the vertexes of edge are presented.
     *
     * @param edge - edge that we add.
     */
    void addEdge(Edge<T> edge);

    /**
     * We search for neighbours of vertex.
     *
     * @param vertex - vertex, which neighbours we search.
     * @return list of neighbours.
     */
    List<Vertex<T>> getNeighbors(Vertex<T> vertex);

    /**
     * We read graph from formatted input,
     * first we have two numbers n,m:
     * n - number of vertexes, m - number of edges.
     * We look through n integers and add n vertexes.
     * After that we search through m edges where each edge must be on different line,
     * first number is from, second is to.
     *
     * @param from file, where we search.
     * @param parser - function to parse our vertex type.
     */
    Graph<T> readFromFile(String from, Function<String, T> parser);

    /**
     * We search for list of all vertices of graph.
     *
     * @return list of vertices.
     */
    List<Vertex<T>> getVertexes();
}
