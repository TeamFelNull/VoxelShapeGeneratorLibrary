package dev.felnull.vsgl;

import java.util.Objects;

public class Vec3d {
    private final double x;
    private final double y;
    private final double z;

    protected Vec3d(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vec3d vec3d = (Vec3d) o;
        return Double.compare(vec3d.x, x) == 0 && Double.compare(vec3d.y, y) == 0 && Double.compare(vec3d.z, z) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public String toString() {
        return "Vec3d{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
