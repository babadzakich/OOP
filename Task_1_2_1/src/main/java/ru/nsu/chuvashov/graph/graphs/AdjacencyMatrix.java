package ru.nsu.chuvashov.graph.graphs;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

import ru.nsu.chuvashov.graph.Graph;
import ru.nsu.chuvashov.graph.structure.Edge;
import ru.nsu.chuvashov.graph.structure.Vertex;

public class AdjacencyMatrix<T> implements Graph<T> {
    private final ArrayList<ArrayList<Integer>> matrix;
    private final ArrayList<Vertex<T>> vertices;

    public AdjacencyMatrix() {
        vertices = new ArrayList<>();
        matrix = new ArrayList<>();
    }

    @Override
    public void addVertex(Vertex<T> vertex) {
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
    public void addEdge(Edge<T> edge) {
        if (!vertices.contains(edge.getFrom()) || !vertices.contains(edge.getTo())) {
            throw new IllegalArgumentException("Edge from " + edge.getFrom()
                    + " to " + edge.getTo() + " can`t be in matrix");
        }
        int x = matrix.get(vertices.indexOf(edge.getFrom())).get(vertices.indexOf(edge.getTo()));
        x += edge.getWeight();
        matrix.get(vertices.indexOf(edge.getFrom())).set(vertices.indexOf(edge.getTo()), x);
        edge.getFrom().updateDegree(edge);
        edge.getTo().updateDegree(edge);
    }

    @Override
    public List<Vertex<T>> getNeighbors(Vertex<T> vertex) {
        ArrayList<Vertex<T>> neighbors = new ArrayList<>();
        for (int i = 0; i < matrix.get(vertices.indexOf(vertex)).size(); i++) {
            if (matrix.get(vertices.indexOf(vertex)).get(i) > 0) {
                neighbors.add(vertices.get(i));
            }
        }
        return neighbors;
    }

    @Override
    public Graph<T> readFromFile(String fileName, Function<String, T> parser) {
        AdjacencyMatrix<T> graph = new AdjacencyMatrix<>();
        try {
            File newStream = new File(fileName);
            Scanner scanner = new Scanner(newStream);
            int vertexCount;
            int edgeCount;
            String[] sizes = scanner.nextLine().trim().split(" ");
            if (sizes.length != 2) {
                throw new IllegalArgumentException("Incorrect file format");
            } else {
                vertexCount = Integer.parseInt(sizes[0]);
                edgeCount = Integer.parseInt(sizes[1]);
            }
            for (int i = 0; i < vertexCount; i++) {
                graph.addVertex(new Vertex<>(parser.apply(scanner.nextLine().trim())));
            }
            for (int i = 0; i < edgeCount; i++) {
                String[] line = scanner.nextLine().trim().split(" ");
                Edge<T> e;
                try {
                    if (line.length == 3) {
                        e = new Edge<>(new Vertex<>(parser.apply(line[0])),
                                new Vertex<>(parser.apply(line[1])), Integer.parseInt(line[2]));
                    } else {
                        throw new IllegalArgumentException("Wrong number of arguments");
                    }
                } catch (NumberFormatException ex) {
                    throw new IllegalArgumentException("Wrong type of arguments");
                }
                graph.addEdge(e);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return graph;
    }

    /**
     * @return list of vertices in graph.
     */
    @Override
    public List<Vertex<T>> getVertexes() {
       return vertices;
    }


}
