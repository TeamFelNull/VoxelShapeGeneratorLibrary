package dev.felnull.vsgl.physics;

import java.util.List;
import java.util.Objects;

public class AngledAABB extends AABB {
    private final Rotation rotation;

    public AngledAABB(double angle, Axis axis, Vec3d origin, Vec3d from, Vec3d to) {
        super(from, to);
        this.rotation = new Rotation(angle, axis, origin);
    }

    public Rotation getRotation() {
        return rotation;
    }

    @Override
    public int compareTo(AABB aabb) {
        return 2;
    }

    @Override
    public boolean canCombine(AABB aabb) {
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AngledAABB that = (AngledAABB) o;
        return Objects.equals(rotation, that.rotation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), rotation);
    }

    public static enum Axis {
        X("x"), Y("y"), Z("z");
        private final String name;

        private Axis(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public static Axis getAxisByName(String name) {
            for (Axis axis : Axis.values()) {
                if (axis.getName().equalsIgnoreCase(name)) {
                    return axis;
                }
            }
            return null;
        }
    }

    @Override
    public List<Edge> getEdges() {
        return List.of();
    }

    public static class Rotation {
        protected final double angle;
        private final Axis axis;
        private final Vec3d origin;

        protected Rotation(double angle, Axis axis, Vec3d origin) {
            this.angle = angle;
            this.axis = axis;
            this.origin = origin;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Rotation rotation = (Rotation) o;
            return Double.compare(rotation.angle, angle) == 0 && axis == rotation.axis && Objects.equals(origin, rotation.origin);
        }

        @Override
        public int hashCode() {
            return Objects.hash(angle, axis, origin);
        }
    }
}
