package ru.nsu.chuvashov.graph.structure;

/**
 * Class for edge implementation.
 *
 * @param from - starting vertex.
 * @param to - endpoint.
 * @param weight - weight of edge.
 * @param <T> - type of vertexes in edge.
 */
public record Edge<T>(Vertex<T> from, Vertex<T> to, int weight) {

    @Override
    public String toString() {
        return String.format("Edge{from=%s, to=%s, weight=%d}", from, to, weight);
    }
}
