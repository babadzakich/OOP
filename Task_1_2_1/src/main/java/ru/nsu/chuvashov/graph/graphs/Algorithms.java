package ru.nsu.chuvashov.graph.graphs;

import ru.nsu.chuvashov.graph.Graph;
import ru.nsu.chuvashov.graph.structure.Vertex;

import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

public class Algorithms {
    public static <T> List<Vertex<T>> toposort(Graph<T> graph) {
        final Set<Vertex<T>> visited = new HashSet<>();
        final ArrayList<Vertex<T>> result = new ArrayList<>();

        for (Vertex<T> vertex : graph.getVertexes()) {
            if (!visited.contains(vertex)) {
                dfs(graph, vertex, visited, result);
            }
        }
        return result;
    }

    public static <T> void dfs(Graph<T> graph, Vertex<T> vertex, Set<Vertex<T>> visited, List<Vertex<T>> result) {
        visited.add(vertex);
        for (Vertex<T> v : graph.getNeighbors(vertex)) {
            if (!visited.contains(v)) {
                dfs(graph, v, visited, result);
            }
        }
        result.add(vertex);
    }
}
