package ru.nsu.chuvashov.graph.structure;

public class Edge <T> {
    private final Vertex<T> from;
    private final Vertex<T> to;
    private final int weight;

    public Edge(Vertex<T> from, Vertex<T> to, int weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    public Vertex<T> getFrom() {
        return from;
    }

    public Vertex<T> getTo() {
        return to;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null ) return false;
        if (o instanceof Edge e) {
            return from.equals(e.from) && to.equals(e.to) && weight == e.weight;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = from.hashCode();
        result = 31 * result + to.hashCode();
        result = 31 * result + weight;
        return result;
    }

    @Override
    public String toString() {
        return String.format("Edge{from=%s, to=%s, weight=%d}", from, to, weight);
    }
}
