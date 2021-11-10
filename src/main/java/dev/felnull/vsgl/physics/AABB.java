package dev.felnull.vsgl.physics;

import dev.felnull.vsgl.Util;

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

    public Vec3d getRelativeFrom() {
        return new Vec3d(getXLeftPos().getX(), getDownPos().getY(), getZLeftPos().getZ());
    }

    public Vec3d getRelativeTo() {
        return new Vec3d(getXRightPos().getX(), getUpPos().getY(), getZRightPos().getZ());
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

        return new Edge(new Vec3d(stX, stY, stZ), new Vec3d(enX, enY, enZ));
    }

    public PenetrationPosEntry getPenetration(Vec3d start, Vec3d end) {
        if (isInPos(start) && isInPos(end))
            return new PenetrationPosEntry(null, null);

        List<Vec3d> poss = new ArrayList<>();
        for (FaceDirection value : FaceDirection.values()) {
            Vec3d cross = getCrossPos(value, start, end);
            if (cross != null) {
                poss.add(cross);
            }
            if (poss.size() >= 2)
                break;
        }
        if (poss.isEmpty())
            return null;
        if (poss.size() == 1) {
            Vec3d pos = poss.get(0);
            if (pos.getDistance(start) <= pos.getDistance(end)) {
                return new PenetrationPosEntry(pos, null);
            } else {
                return new PenetrationPosEntry(null, pos);
            }
        }
        Vec3d pos1 = poss.get(0);
        Vec3d pos2 = poss.get(1);
        return new PenetrationPosEntry(pos1.getDistance(start) <= pos2.getDistance(start) ? pos1 : pos2, pos1.getDistance(end) <= pos2.getDistance(end) ? pos1 : pos2);
    }

    public boolean isInPos(Vec3d pos) {
        Vec3d from = getRelativeFrom();
        Vec3d to = getRelativeTo();
        boolean fromFlg = pos.getX() > from.getX() && pos.getY() > from.getY() && pos.getZ() > from.getZ();
        if (!fromFlg)
            return false;
        return pos.getX() < to.getX() && pos.getY() < to.getY() && pos.getZ() < to.getZ();
    }

    public Vec3d getCrossPos(FaceDirection direction, Vec3d start, Vec3d end) {
        FacePos facePos = getFace(direction);
        if (facePos == null) return null;
        Vec2d xc;
        Vec2d zc;
        switch (direction.getAxis()) {
            case Y:
                xc = Util.getCrossPoint(new Vec2d(facePos.start.getX(), facePos.start.getY()), new Vec2d(facePos.end.getX(), facePos.end.getY()), new Vec2d(start.getX(), start.getY()), new Vec2d(end.getX(), end.getY()));
                zc = Util.getCrossPoint(new Vec2d(facePos.start.getZ(), facePos.start.getY()), new Vec2d(facePos.end.getZ(), facePos.end.getY()), new Vec2d(start.getZ(), start.getY()), new Vec2d(end.getZ(), end.getY()));
                break;
            case X:
                xc = Util.getCrossPoint(new Vec2d(facePos.start.getY(), facePos.start.getX()), new Vec2d(facePos.end.getY(), facePos.end.getX()), new Vec2d(start.getY(), start.getX()), new Vec2d(end.getY(), end.getX()));
                zc = Util.getCrossPoint(new Vec2d(facePos.start.getZ(), facePos.start.getX()), new Vec2d(facePos.end.getZ(), facePos.end.getX()), new Vec2d(start.getZ(), start.getX()), new Vec2d(end.getZ(), end.getX()));
                break;
            case Z:
                xc = Util.getCrossPoint(new Vec2d(facePos.start.getY(), facePos.start.getZ()), new Vec2d(facePos.end.getY(), facePos.end.getZ()), new Vec2d(start.getY(), start.getZ()), new Vec2d(end.getY(), end.getZ()));
                zc = Util.getCrossPoint(new Vec2d(facePos.start.getX(), facePos.start.getZ()), new Vec2d(facePos.end.getX(), facePos.end.getZ()), new Vec2d(start.getX(), start.getZ()), new Vec2d(end.getX(), end.getZ()));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + direction.getAxis());
        }
        if (xc == null || zc == null) return null;
        switch (direction.getAxis()) {
            case Y:
                if (xc.getX() != facePos.start.getX() && xc.getX() != facePos.end.getX() && zc.getX() != facePos.start.getZ() && zc.getX() != facePos.end.getZ())
                    return new Vec3d(xc.getX(), facePos.getStart().getY(), zc.getX());
            case X:
                if (xc.getX() != facePos.start.getZ() && xc.getX() != facePos.end.getZ() && zc.getX() != facePos.start.getY() && zc.getX() != facePos.end.getY())
                    return new Vec3d(facePos.getStart().getX(), xc.getX(), zc.getX());
            case Z:
                if (xc.getX() != facePos.start.getX() && xc.getX() != facePos.end.getX() && zc.getX() != facePos.start.getY() && zc.getX() != facePos.end.getY())
                    return new Vec3d(xc.getX(), xc.getX(), facePos.getStart().getZ());
            default:
                return null;
        }
    }

    public FacePos getFace(FaceDirection direction) {
        switch (direction) {
            case UP:
                return new FacePos(getRelativeTo(), new Vec3d(getRelativeFrom().getX(), getRelativeTo().getY(), getRelativeFrom().getZ()));
            case DOWN:
                return new FacePos(new Vec3d(getRelativeTo().getX(), getRelativeFrom().getY(), getRelativeTo().getZ()), getRelativeFrom());
            case NORTH:
                return new FacePos(new Vec3d(getRelativeTo().getX(), getRelativeTo().getY(), getRelativeFrom().getZ()), getRelativeFrom());
            case SOUTH:
                return new FacePos(getRelativeTo(), new Vec3d(getRelativeFrom().getX(), getRelativeFrom().getY(), getRelativeTo().getZ()));
            case EAST:
                return new FacePos(new Vec3d(getRelativeFrom().getX(), getRelativeTo().getY(), getRelativeTo().getZ()), getRelativeFrom());
            case WEST:
                return new FacePos(getRelativeTo(), new Vec3d(getRelativeTo().getX(), getRelativeFrom().getY(), getRelativeFrom().getZ()));
        }
        return null;
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

    public static class PenetrationPosEntry {
        private final Vec3d start;
        private final Vec3d end;

        public PenetrationPosEntry(Vec3d start, Vec3d end) {
            this.start = start;
            this.end = end;
        }

        public Vec3d getStart() {
            return start;
        }

        public Vec3d getEnd() {
            return end;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PenetrationPosEntry that = (PenetrationPosEntry) o;
            return Objects.equals(start, that.start) && Objects.equals(end, that.end);
        }

        @Override
        public int hashCode() {
            return Objects.hash(start, end);
        }
    }

    public static enum FaceDirection {
        UP,
        DOWN,
        SOUTH,
        NORTH,
        WEST,
        EAST;

        public FaceDirection revers() {
            switch (this) {
                case UP:
                    return DOWN;
                case DOWN:
                    return UP;
                case SOUTH:
                    return NORTH;
                case NORTH:
                    return SOUTH;
                case EAST:
                    return WEST;
                case WEST:
                    return EAST;
            }
            return NORTH;
        }

        public AngledAABB.Axis getAxis() {
            switch (this) {
                case UP:
                case DOWN:
                    return AngledAABB.Axis.Y;
                case SOUTH:
                case NORTH:
                    return AngledAABB.Axis.Z;
                case EAST:
                case WEST:
                    return AngledAABB.Axis.X;
            }
            return AngledAABB.Axis.X;
        }

    }

    private static class FacePos {
        private final Vec3d start;
        private final Vec3d end;

        public FacePos(Vec3d start, Vec3d end) {
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
            FacePos facePos = (FacePos) o;
            return Objects.equals(start, facePos.start) && Objects.equals(end, facePos.end);
        }

        @Override
        public int hashCode() {
            return Objects.hash(start, end);
        }

        @Override
        public String toString() {
            return "FacePos{" +
                    "start=" + start +
                    ", end=" + end +
                    '}';
        }
    }
}
