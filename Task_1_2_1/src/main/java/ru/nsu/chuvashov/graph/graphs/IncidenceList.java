package ru.nsu.chuvashov.graph.graphs;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import ru.nsu.chuvashov.graph.Graph;
import ru.nsu.chuvashov.graph.structure.Edge;
import ru.nsu.chuvashov.graph.structure.Vertex;

public class IncidenceList implements Graph {
    public final Map<Vertex, ArrayList<Edge>> incidenceList;

    public IncidenceList() {
        incidenceList = new HashMap<>();
    }

    @Override
    public void addVertex(Vertex vertex) {
        if (!incidenceList.containsKey(vertex)) {
            incidenceList.put(vertex, new ArrayList<>());
        }
    }

    @Override
    public void addEdge(Edge edge) {
        if (!incidenceList.containsKey(edge.getFrom()) || !incidenceList.containsKey(edge.getTo())) {
            throw new IllegalArgumentException("Edge from " + edge.getFrom() + " to " + edge.getTo() + " is not included");
        }
        incidenceList.get(edge.getFrom()).add(edge);
        edge.getFrom().updateDegree(edge);
        edge.getTo().updateDegree(edge);
    }

    @Override
    public ArrayList<Vertex> getNeighbors(Vertex vertex) {
        if (!incidenceList.containsKey(vertex)) {
            throw new NoSuchElementException("Vertex not found");
        }
        ArrayList<Vertex> result = new ArrayList<>(incidenceList.get(vertex).size());
        for (Edge edge : incidenceList.get(vertex)) {
            result.add(edge.getTo());
        }
        return result;
    }

    @Override
    public void readFromFile(String fileName) {
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
                addVertex(new Vertex(scanner.nextInt()));
            }
            for (int i = 0; i < edgeCount; i++) {
                String[] line = scanner.nextLine().trim().split(" ");
                Edge e;
                try {
                    if (line.length == 2) {
                        e = new Edge(new Vertex(Integer.parseInt(line[0])), new Vertex(Integer.parseInt(line[1])));
                    } else {
                        throw new IllegalArgumentException("Wrong number of arguments");
                    }
                } catch (NumberFormatException ex) {
                    throw new IllegalArgumentException("Wrong type of arguments");
                }
                this.addEdge(e);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Vertex> toposort() {
        final Set<Vertex> visited = new HashSet<>();
        final ArrayList<Vertex> result = new ArrayList<>();

        for (Vertex vertex : incidenceList.keySet()) {
            if (!visited.contains(vertex)) {
                dfs(vertex, visited, result);
            }
        }
        return result;
    }

    private void dfs(Vertex vertex, Set<Vertex> visited, ArrayList<Vertex> result) {
        visited.add(vertex);
        for (Vertex v : this.getNeighbors(vertex)) {
            if (!visited.contains(v)) {
                dfs(v, visited, result);
            }
        }
        result.add(vertex);
    }
}
