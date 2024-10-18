package ru.nsu.chuvashov.graph.structure;

public class Vertex<T> {
    private final T number;
    private int inDegree;
    private int outDegree;

    public Vertex(T name) {
        this.number = name;
        this.inDegree = 0;
        this.outDegree = 0;
    }

    public void updateDegree(Edge<T> edge) {
        if (this.equals(edge.getFrom())) {
            this.inDegree += 1;
        }
        if (this.equals(edge.getTo())) {
            this.outDegree += 1;
        }
    }

    public T getName() {
        return number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (o instanceof Vertex vertex) {
            return number == vertex.number;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return number.hashCode();
    }

    @Override
    public String toString() {
        return number.toString();
    }
}
