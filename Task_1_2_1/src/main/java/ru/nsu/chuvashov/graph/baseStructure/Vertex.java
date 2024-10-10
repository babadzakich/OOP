package ru.nsu.chuvashov.graph.baseStructure;

public class Vertex {
    private final int number;

    public Vertex(int name) {
        this.number = name;
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
}
