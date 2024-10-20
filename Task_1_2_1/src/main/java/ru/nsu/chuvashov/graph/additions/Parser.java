package ru.nsu.chuvashov.graph.additions;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.function.Function;
import ru.nsu.chuvashov.graph.Graph;
import ru.nsu.chuvashov.graph.structure.Edge;
import ru.nsu.chuvashov.graph.structure.Vertex;

/**
 * Class for parsing from file.
 */
public class Parser {
    /**
     * We read info from file to graph.
     *
     * @param graph which we build.
     * @param fileName - path to file.
     * @param parser - function to convert from string to our type.
     * @param <T> - type of graph.
     * @return our graph.
     */
    public static <T> Graph<T> parse(Graph<T> graph, String fileName, Function<String, T> parser) {
        File newStream;
        Scanner scanner;
        try {
            newStream = new File(fileName);
            scanner = new Scanner(newStream);
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            return null;
        }
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
        return graph;
    }
}
