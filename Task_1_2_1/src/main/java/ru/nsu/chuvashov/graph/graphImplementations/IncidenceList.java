package ru.nsu.chuvashov.graph.graphImplementations;

import ru.nsu.chuvashov.graph.Graph;
import ru.nsu.chuvashov.graph.baseStructure.Edge;
import ru.nsu.chuvashov.graph.baseStructure.Vertex;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

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
        if (!incidenceList.containsKey(edge.getFrom())) {
            incidenceList.put(edge.getFrom(), new ArrayList<>());
        }
        incidenceList.get(edge.getFrom()).add(edge);
    }

    @Override
    public List<Vertex> getNeighbors(Vertex vertex) {
        if (!incidenceList.containsKey(vertex)) {
            throw new IllegalArgumentException("Vertex " + vertex + " does not exist");
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

            while (scanner.hasNextLine()) {
                String[] line = scanner.nextLine().trim().split(" ");
                Edge e;
                try {
                    if (line.length == 2) {
                        e = Edge.createUnweightedEdge(new Vertex(Integer.parseInt(line[0])), new Vertex(Integer.parseInt(line[1])));
                    } else if (line.length == 3) {
                        e = Edge.createWeightedEdge(new Vertex(Integer.parseInt(line[0])), new Vertex(Integer.parseInt(line[1])), Integer.parseInt(line[2]));
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
    public List<Vertex> toposort() {
        final Set<Vertex> visited = new HashSet<>();
        final ArrayList<Vertex> result = new ArrayList<>();

        for (Vertex vertex : incidenceList.keySet()) {
            if (!visited.contains(vertex)) {
                dfs(vertex, visited, result);
            }
        }
        return result.reversed();
    }

    private void dfs(Vertex vertex, Set<Vertex> visited, ArrayList<Vertex> result) {
        visited.add(vertex);
        for (Vertex v : this.getNeighbors(vertex)) {
            if (!visited.contains(v)) {
                dfs(v, visited, result);
            }
            result.add(v);
        }
    }
}
