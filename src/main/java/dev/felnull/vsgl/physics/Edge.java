package dev.felnull.vsgl.physics;

import java.util.Objects;

public class Edge {
    private final Vec3d start;
    private final Vec3d end;
    private final EdgeLocation edgeLocation;
    private final int number;

    public Edge(Vec3d start, Vec3d end, EdgeLocation edgeLocation, int number) {
        this.start = start;
        this.end = end;
        this.edgeLocation = edgeLocation;
        this.number = number;
    }

    public Vec3d getEnd() {
        return end;
    }

    public Vec3d getStart() {
        return start;
    }

    public EdgeLocation getEdgeLocation() {
        return edgeLocation;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return number == edge.number && Objects.equals(start, edge.start) && Objects.equals(end, edge.end) && edgeLocation == edge.edgeLocation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end, edgeLocation, number);
    }

    @Override
    public String toString() {
        return "Edge{" +
                "start=" + start +
                ", end=" + end +
                ", edgeLocation=" + edgeLocation +
                ", number=" + number +
                '}';
    }

    public static enum EdgeLocation {
        UP, NONE, DOWN;
    }
}
