package ru.nsu.chuvashov.graph.structure;

/**
 * Vertex record using generics.
 *
 * @param name - name of vertex.
 * @param <T> - type of vertex.
 */
public record Vertex<T>(T name) {
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (o instanceof Vertex<?> vertex) {
            return name == vertex.name;
        }
        return false;
    }

    @Override
    public String toString() {
        return name.toString();
    }
}
