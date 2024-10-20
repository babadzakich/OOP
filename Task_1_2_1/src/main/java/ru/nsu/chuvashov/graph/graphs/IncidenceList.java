package ru.nsu.chuvashov.graph.graphs;

import java.util.*;
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
public class IncidenceList <T> implements Graph<T> {
    public final Map<Vertex<T>, ArrayList<Edge<T>>> incidenceList;

    public IncidenceList() {
        incidenceList = new HashMap<>();
    }

    @Override
    public void addVertex(Vertex<T> vertex) {
        if (!incidenceList.containsKey(vertex)) {
            incidenceList.put(vertex, new ArrayList<>());
        }
    }

    @Override
    public void addEdge(Edge<T> edge) {
        if (!incidenceList.containsKey(edge.getFrom())
                || !incidenceList.containsKey(edge.getTo())) {
            throw new IllegalArgumentException("Graph doesn`t contain vertexes from edge");
        }
        incidenceList.get(edge.getFrom()).add(edge);
        edge.getFrom().updateDegree(edge);
        edge.getTo().updateDegree(edge);
    }

    @Override
    public List<Vertex<T>> getNeighbors(Vertex<T> vertex) {
        if (!incidenceList.containsKey(vertex)) {
            throw new NoSuchElementException("Vertex not found");
        }
        ArrayList<Vertex<T>> result = new ArrayList<>(incidenceList.get(vertex).size());
        for (Edge<T> edge : incidenceList.get(vertex)) {
            result.add(edge.getTo());
        }
        return result;
    }

    @Override
    public Graph<T> readFromFile(String fileName, Function<String, T> parser) {
        IncidenceList<T> graph = new IncidenceList<>();

        return Parser.parse(graph, fileName, parser);
    }

    /**
     * @return list of our vertexes.
     */
    @Override
    public List<Vertex<T>> getVertexes() {
        return List.copyOf(incidenceList.keySet());
    }
}
