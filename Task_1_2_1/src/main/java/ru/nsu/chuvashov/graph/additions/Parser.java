package ru.nsu.chuvashov.graph.additions;

import java.io.*;
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
     */
    public static <T> void parse(Graph<T> graph, String fileName, Function<String, T> parser) throws FileNotFoundException {
        InputStream inputStream;
        inputStream = Parser.class.getClassLoader().getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new FileNotFoundException("File not found: " + fileName);
        }

        BufferedReader reader;
        reader = new BufferedReader(new InputStreamReader(inputStream));

        int vertexCount;
        int edgeCount;
        try {
            String ln = reader.readLine();
            if (ln == null) {
                throw new IllegalArgumentException("Empty file");
            }
            String[] sizes = ln.trim().split(" ");
            if (sizes.length != 2) {
                throw new IllegalArgumentException("First line isn`t made of 2 numbers");
            } else {
                vertexCount = Integer.parseInt(sizes[0]);
                edgeCount = Integer.parseInt(sizes[1]);
            }
            for (int i = 0; i < vertexCount; i++) {
                String ln2 = reader.readLine();
                if (ln2 == null) {
                    throw new IllegalArgumentException("Not enough lines of vertexes");
                }
                graph.addVertex(new Vertex<>(parser.apply(ln2.trim())));
            }
            for (int i = 0; i < edgeCount; i++) {
                String ln2 = reader.readLine();
                if (ln2 == null) {
                    throw new IllegalArgumentException("Not enough lines of edges");
                }
                String[] line = ln2.trim().split(" ");
                Edge<T> e;

                if (line.length == 3) {
                    e = new Edge<>(new Vertex<>(parser.apply(line[0])),
                            new Vertex<>(parser.apply(line[1])), Integer.parseInt(line[2]));
                } else {
                    throw new IllegalArgumentException("Wrong number of edge arguments");
                }

                graph.addEdge(e);
            }
        } catch (IOException | NumberFormatException ex) {
            if (ex instanceof IOException e) {
                throw new UncheckedIOException(e);
            } else {
                throw new IllegalArgumentException("Wrong type of vertexes presented");
            }
        }
    }
}
