package dev.felnull.vsgl.physics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class AABB {
    private final Vec3d from;
    private final Vec3d to;

    public AABB(Vec3d from, Vec3d to) {
        this.from = from;
        this.to = to;
    }

    public Vec3d getTo() {
        return to;
    }

    public Vec3d getFrom() {
        return from;
    }

    public int compareTo(AABB aabb) {
        if (equals(aabb)) return 0;
        {
            boolean fromFlg = from.getX() <= aabb.getFrom().getX() && from.getY() <= aabb.getFrom().getY() && from.getZ() <= aabb.getFrom().getZ();
            boolean toFlg = to.getX() >= aabb.getTo().getX() && to.getY() >= aabb.getTo().getY() && to.getZ() >= aabb.getTo().getZ();
            if (fromFlg && toFlg) return 1;
        }
        {
            boolean fromFlg = from.getX() >= aabb.getFrom().getX() && from.getY() >= aabb.getFrom().getY() && from.getZ() >= aabb.getFrom().getZ();
            boolean toFlg = to.getX() <= aabb.getTo().getX() && to.getY() <= aabb.getTo().getY() && to.getZ() <= aabb.getTo().getZ();
            if (fromFlg && toFlg) return -1;
        }
        return 2;
    }

    public boolean canCombine(AABB aabb) {
        boolean xflg = from.getX() == aabb.getFrom().getX() && to.getX() == aabb.getTo().getX();
        boolean yflg = from.getY() == aabb.getFrom().getY() && to.getY() == aabb.getTo().getY();
        boolean zflg = from.getZ() == aabb.getFrom().getZ() && to.getZ() == aabb.getTo().getZ();

        boolean xcflag = (from.getX() <= aabb.getFrom().getX() && to.getX() >= aabb.getFrom().getX()) || (from.getX() <= aabb.getTo().getX() && to.getX() >= aabb.getTo().getX());
        boolean ycflag = (from.getY() <= aabb.getFrom().getY() && to.getY() >= aabb.getFrom().getY()) || (from.getY() <= aabb.getTo().getY() && to.getY() >= aabb.getTo().getY());
        boolean zcflag = (from.getZ() <= aabb.getFrom().getZ() && to.getZ() >= aabb.getFrom().getZ()) || (from.getZ() <= aabb.getTo().getZ() && to.getZ() >= aabb.getTo().getZ());

        if (yflg && zflg && xcflag)
            return true;

        if (xflg && zflg && ycflag)
            return true;

        return yflg && xflg && zcflag;
    }

    public AABB combined(AABB aabb) {
        double fx = Math.min(from.getX(), aabb.getFrom().getX());
        double fy = Math.min(from.getY(), aabb.getFrom().getY());
        double fz = Math.min(from.getZ(), aabb.getFrom().getZ());
        double tx = Math.max(to.getX(), aabb.getTo().getX());
        double ty = Math.max(to.getY(), aabb.getTo().getY());
        double tz = Math.max(to.getZ(), aabb.getTo().getZ());
        return new AABB(new Vec3d(fx, fy, fz), new Vec3d(tx, ty, tz));
    }

    public Vec3d getUpPos() {
        return from.getY() > to.getY() ? from : to;
    }

    public Vec3d getDownPos() {
        return from.getY() < to.getY() ? from : to;
    }

    public Vec3d getXLeftPos() {
        return from.getX() < to.getX() ? from : to;
    }

    public Vec3d getXRightPos() {
        return from.getX() > to.getX() ? from : to;
    }

    public Vec3d getZLeftPos() {
        return from.getZ() < to.getZ() ? from : to;
    }

    public Vec3d getZRightPos() {
        return from.getZ() > to.getZ() ? from : to;
    }

    public List<Edge> getEdges() {
        List<Edge> edges = new ArrayList<>();
        for (Edge.EdgeLocation value : Edge.EdgeLocation.values()) {
            for (int i = 0; i < 4; i++) {
                edges.add(getEdge(value, i));
            }
        }
        return Collections.unmodifiableList(edges);
    }

    public Edge getEdge(Edge.EdgeLocation edgeLocation, int num) {
        if (num > 3) return null;
        double stY;
        if (edgeLocation == Edge.EdgeLocation.UP) {
            stY = getUpPos().getY();
        } else {
            stY = getDownPos().getY();
        }
        double enY;
        if (edgeLocation == Edge.EdgeLocation.NONE) {
            enY = getUpPos().getY();
        } else {
            enY = stY;
        }

        double stX;
        double stZ;

        double enX;
        double enZ;
        if (edgeLocation == Edge.EdgeLocation.NONE) {
            switch (num) {
                default:
                    stX = getXLeftPos().getX();
                    stZ = getZLeftPos().getZ();
                    break;
                case 1:
                    stX = getXRightPos().getX();
                    stZ = getZLeftPos().getZ();
                    break;
                case 2:
                    stX = getXLeftPos().getX();
                    stZ = getZRightPos().getZ();
                    break;
                case 3:
                    stX = getXRightPos().getX();
                    stZ = getZRightPos().getZ();
                    break;
            }
            enX = stX;
            enZ = stZ;
        } else {
            if (num == 0 || num == 2 || num == 3) {
                stX = getXLeftPos().getX();
            } else {
                stX = getXRightPos().getX();
            }

            if (num == 0 || num == 1 || num == 2) {
                stZ = getZLeftPos().getZ();
            } else {
                stZ = getZRightPos().getZ();
            }

            if (num == 1 || num == 2 || num == 3) {
                enX = getXRightPos().getX();
            } else {
                enX = getXLeftPos().getX();
            }

            if (num == 0 || num == 1 || num == 3) {
                enZ = getZRightPos().getZ();
            } else {
                enZ = getZLeftPos().getZ();
            }
        }

        return new Edge(new Vec3d(stX, stY, stZ), new Vec3d(enX, enY, enZ), edgeLocation, num);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AABB aabb = (AABB) o;
        return Objects.equals(from, aabb.from) && Objects.equals(to, aabb.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }

    @Override
    public String toString() {
        return "AABB{" +
                "from=" + from +
                ", to=" + to +
                '}';
    }
}
