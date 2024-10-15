package ru.nsu.chuvashov.graph;

import ru.nsu.chuvashov.graph.structure.Edge;
import ru.nsu.chuvashov.graph.structure.Vertex;

import java.util.ArrayList;

public interface Graph {
    void addVertex(Vertex vertex);

    void addEdge(Edge edge);

    ArrayList<Vertex> getNeighbors(Vertex vertex);

    void readFromFile(String filename);

    ArrayList<Vertex> toposort();
}
