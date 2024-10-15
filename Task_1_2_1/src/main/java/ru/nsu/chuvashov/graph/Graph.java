package ru.nsu.chuvashov.graph;

import java.util.ArrayList;
import java.util.List;

import ru.nsu.chuvashov.graph.structure.Edge;
import ru.nsu.chuvashov.graph.structure.Vertex;

public interface Graph {
    void addVertex(Vertex vertex);

    void addEdge(Edge edge);

    ArrayList<Vertex> getNeighbors(Vertex vertex);

    void readFromFile(String filename);

    List<Vertex> getVertices();
}
