package ru.nsu.chuvashov.graph.graphs;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;
import ru.nsu.chuvashov.graph.Graph;
import ru.nsu.chuvashov.graph.additions.Parser;
import ru.nsu.chuvashov.graph.structure.Edge;
import ru.nsu.chuvashov.graph.structure.Vertex;

public class IncidenceMatrix<T> implements Graph<T> {
    private final ArrayList<Vertex<T>> vertexes;
    private final ArrayList<Edge<T>> edges;
    private final ArrayList<ArrayList<Integer>> matrix;

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
        if (!vertexes.contains(edge.getTo()) || !vertexes.contains(edge.getFrom())) {
            throw new IllegalArgumentException("Graph doesn`t contain vertexes from edge");
        }
        edges.add(edge);
        for (int i = 0; i < vertexes.size(); i++) {
            if (vertexes.get(i) == edge.getFrom()) {
                matrix.get(i).add(-(edge.getWeight()));
            } else if (vertexes.get(i) == edge.getTo()) {
                matrix.get(i).add(edge.getWeight());
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
            if (matrix.get(index).get(i) > 0) {
                neighbors.add(edges.get(i).getTo());
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
    public Graph<T> readFromFile(String fileName, Function<String, T> parser) {
        IncidenceMatrix<T> graph = new IncidenceMatrix<>();

        return Parser.parse(graph, fileName, parser);
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
