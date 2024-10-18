package ru.nsu.chuvashov.graph.graphs;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.function.Function;

import ru.nsu.chuvashov.graph.Graph;
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
            throw new IllegalArgumentException("Edge from "
                    + edge.getFrom() + " to "
                    + edge.getTo() + " is not included");
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
     * @return list of our vertexes.
     */
    @Override
    public List<Vertex<T>> getVertexes() {
        return List.copyOf(incidenceList.keySet());
    }
}
