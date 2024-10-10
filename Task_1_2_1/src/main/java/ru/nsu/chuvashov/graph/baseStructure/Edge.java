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
}
