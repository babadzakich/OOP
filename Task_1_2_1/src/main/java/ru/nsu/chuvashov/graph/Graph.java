package ru.nsu.chuvashov.graph;

import ru.nsu.chuvashov.graph.baseStructure.Edge;
import ru.nsu.chuvashov.graph.baseStructure.Vertex;

import java.util.List;

public interface Graph {
    void addVertex(Vertex vertex);

    void addEdge(Edge edge);

    List<Vertex> getNeighbors(Vertex vertex);

    void readFromFile(String filename);

    List<Vertex> toposort();
}
