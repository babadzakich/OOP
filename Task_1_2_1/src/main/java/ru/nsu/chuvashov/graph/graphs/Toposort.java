package ru.nsu.chuvashov.graph.graphs;

import ru.nsu.chuvashov.graph.Graph;
import ru.nsu.chuvashov.graph.structure.Vertex;

import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

public class Toposort {
    public static List<Vertex> toposort(Graph graph) {
        final Set<Vertex> visited = new HashSet<>();
        final ArrayList<Vertex> result = new ArrayList<>();

        for (Vertex vertex : graph.getVertices()) {
            if (!visited.contains(vertex)) {
                dfs(graph, vertex, visited, result);
            }
        }
        return result;
    }

    private static void dfs(Graph graph, Vertex vertex, Set<Vertex> visited, List<Vertex> result) {
        visited.add(vertex);
        for (Vertex v : graph.getNeighbors(vertex)) {
            if (!visited.contains(v)) {
                dfs(graph, v, visited, result);
            }
        }
        result.add(vertex);
    }
}
