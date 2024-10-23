package ru.nsu.chuvashov.graph.structure;

/**
 * Vertex record using generics.
 *
 * @param name - name of vertex.
 * @param <T> - type of vertex.
 */
public record Vertex<T>(T name) {

}
