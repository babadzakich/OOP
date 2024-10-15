package ru.nsu.chuvashov.graph.structure;

public class Edge {
    private final Vertex from;
    private final Vertex to;

    public Edge(Vertex from, Vertex to) {
        this.from = from;
        this.to = to;
    }

    public Vertex getFrom() {
        return from;
    }

    public Vertex getTo() {
        return to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null ) return false;
        if (o instanceof Edge e) {
            return from.equals(e.from) && to.equals(e.to);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = from.hashCode();
        result = 31 * result + to.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format("Edge{from=%s, to=%s}", from, to);
    }
}
