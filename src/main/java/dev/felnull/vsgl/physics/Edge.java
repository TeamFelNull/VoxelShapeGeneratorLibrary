package dev.felnull.vsgl.physics;

import java.util.Objects;

public class Edge {
    private final Vec3d start;
    private final Vec3d end;

    public Edge(Vec3d start, Vec3d end) {
        this.start = start;
        this.end = end;
    }

    public Vec3d getEnd() {
        return end;
    }

    public Vec3d getStart() {
        return start;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return Objects.equals(start, edge.start) && Objects.equals(end, edge.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }

    @Override
    public String toString() {
        return "Edge{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }

    public static enum EdgeLocation {
        UP, NONE, DOWN;
    }
}
