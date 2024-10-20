package ru.nsu.chuvashov.graph.additions;

import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import ru.nsu.chuvashov.graph.Graph;
import ru.nsu.chuvashov.graph.structure.Vertex;

/**
 * Class for all algorithms with graphs.
 */
public class Algorithms {
    /**
     * Topological sort algorithm using DFS.
     *
     * @param graph - graph which we sort.
     * @param <T> - type of graph.
     * @return sorted list of vertexes in graph.
     */
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

    /**
     * Recursive algorithm for depth-first search.
     *
     * @param graph - where we use dfs.
     * @param vertex - current vertex.
     * @param visited - shows whether we visited vertex or not.
     * @param result - list of vertexes after search.
     * @param <T> - type of graph.
     */
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
