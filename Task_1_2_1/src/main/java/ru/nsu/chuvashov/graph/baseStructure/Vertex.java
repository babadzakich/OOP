package ru.nsu.chuvashov.graph.baseStructure;

public class Vertex {
    private final int number;
    private int inDegree;
    private int outDegree;

    public Vertex(int name) {
        this.number = name;
        this.inDegree = 0;
        this.outDegree = 0;
    }

    public void updateDegree(Edge edge) {
        if (this.equals(edge.getFrom())) {
            this.inDegree += 1;
        }
        if (this.equals(edge.getTo())) {
            this.outDegree += 1;
        }
    }

    public int getName() {
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
        return number * 31;
    }

    @Override
    public String toString() {
        return String.format("%d", number);
    }
}
