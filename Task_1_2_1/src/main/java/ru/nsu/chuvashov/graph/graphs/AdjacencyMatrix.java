package ru.nsu.chuvashov.graph.graphs;

import java.util.ArrayList;
import ru.nsu.chuvashov.graph.Graph;
import ru.nsu.chuvashov.graph.structure.Edge;
import ru.nsu.chuvashov.graph.structure.Vertex;

public class AdjacencyMatrix implements Graph {
    private final ArrayList<ArrayList<Integer>> matrix;
    private final ArrayList<Vertex> vertices;

    public AdjacencyMatrix() {
        vertices = new ArrayList<>();
        matrix = new ArrayList<>();
    }

    @Override
    public void addVertex(Vertex vertex) {
        for (ArrayList<Integer> integers : matrix) {
            integers.add(0);
        }
        vertices.add(vertex);
        matrix.add(new ArrayList<>(vertices.size()));
        for (int i = 0; i < vertices.size(); i++) {
            matrix.getLast().add(0);
        }
    }

    @Override
    public void addEdge(Edge edge) {
        if (!vertices.contains(edge.getFrom()) || !vertices.contains(edge.getTo())) {
            throw new IllegalArgumentException("Edge from " + edge.getFrom()
                    + " to " + edge.getTo() + " can`t be in matrix");
        }
        int x = matrix.get(vertices.indexOf(edge.getFrom())).get(vertices.indexOf(edge.getTo()));
        x += 1;
        matrix.get(vertices.indexOf(edge.getFrom())).set(vertices.indexOf(edge.getTo()), x);
        edge.getFrom().updateDegree(edge);
        edge.getTo().updateDegree(edge);
    }

    @Override
    public ArrayList<Vertex> getNeighbors(Vertex vertex) {
        ArrayList<Vertex> neighbors = new ArrayList<>();
        for (int i = 0; i < matrix.get(vertices.indexOf(vertex)).size(); i++) {
            if (matrix.get(vertices.indexOf(vertex)).get(i) > 0) {
                neighbors.add(vertices.get(i));
            }
        }
        return neighbors;
    }

    @Override
    public void readFromFile(String filename) {

    }

    @Override
    public ArrayList<Vertex> toposort() {
        return null;
    }
}
