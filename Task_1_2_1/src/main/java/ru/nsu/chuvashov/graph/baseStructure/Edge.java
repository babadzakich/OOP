package ru.nsu.chuvashov.graph.baseStructure;

public class Edge {
    private final Vertex from;
    private final Vertex to;
    private final int weight;

    private Edge(Vertex from, Vertex to, int weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    public static Edge createWeightedEdge(Vertex from, Vertex to, int weight) {
        return new Edge(from, to, weight);
    }

    public static Edge createUnweightedEdge(Vertex from, Vertex to) {
        return new Edge(from, to, 0);
    }

    public Vertex getFrom() {
        return from;
    }

    public Vertex getTo() {
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
