package dev.felnull.vsgl;

import dev.felnull.vsgl.physics.AABB;
import dev.felnull.vsgl.physics.AngledAABB;
import dev.felnull.vsgl.physics.Edge;
import dev.felnull.vsgl.physics.Vec2d;

import java.util.*;

public class Util {
    public static Set<Edge> generateEdges(Set<AABB> aabbs) {
        Map<Edge, AABB> edges = new HashMap<>();
        for (AABB aabb : aabbs) {
            for (Edge edge : aabb.getEdges()) {
                edges.put(edge, aabb);
            }
        }
        boolean upFlg;
        do {
            upFlg = false;
            Edge tEdge = null;
            AABB.PenetrationPosEntry penet = null;
            for (Edge edge : edges.keySet()) {
                AABB edgAabb = edges.get(edge);
                for (AABB aabb : aabbs) {
                    if (aabb == edgAabb) continue;
                    AABB.PenetrationPosEntry pe = aabb.getPenetration(edge.getStart(), edge.getEnd());
                    if (pe != null) {
                        tEdge = edge;
                        penet = pe;
                        break;
                    }
                }
                if (tEdge != null) break;
            }
            if (tEdge != null) {
                AABB edbb = edges.remove(tEdge);
                if (penet.getStart() != null) {
                    edges.put(new Edge(tEdge.getStart(), penet.getStart()), edbb);
                }
                if (penet.getEnd() != null) {
                    edges.put(new Edge(penet.getEnd(), tEdge.getEnd()), edbb);
                }
                upFlg = true;
            }
        } while (upFlg);
        return edges.keySet();
    }

    public static Set<AABB> optimisationAabbs(List<AABB> aabbs) {
        Set<AABB> optAabbs = new HashSet<>();
        for (AABB aabb : aabbs) {
            if (optAabbs.contains(aabb))
                continue;
            if (aabb instanceof AngledAABB) {
                optAabbs.add(aabb);
                continue;
            }
            boolean conFlg = false;
            for (AABB tAabb : aabbs) {
                if (tAabb instanceof AngledAABB) {
                    continue;
                }
                if (aabb.compareTo(tAabb) == -1) {
                    conFlg = true;
                    break;
                }
            }
            if (conFlg)
                continue;
            optAabbs.add(aabb);
        }
        boolean upFlg;
        do {
            upFlg = false;
            AABB comb = null;
            AABB tcomb = null;
            for (AABB optAabb : optAabbs) {
                for (AABB tOptAabb : optAabbs) {
                    if (optAabb == tOptAabb) continue;
                    if (optAabb.canCombine(tOptAabb)) {
                        comb = optAabb;
                        tcomb = tOptAabb;
                        break;
                    }
                }
                if (comb != null) break;
            }
            if (comb != null) {
                optAabbs.remove(comb);
                optAabbs.remove(tcomb);
                optAabbs.add(comb.combined(tcomb));
                upFlg = true;
            }
        } while (upFlg);

        return optAabbs;
    }

    public static Vec2d getCrossPoint(Vec2d start1, Vec2d end1, Vec2d start2, Vec2d end2) {
        double a = start1.getX();
        double b = start1.getY();
        double c = end1.getX();
        double d = end1.getY();
        double e = start2.getX();
        double f = start2.getY();
        double g = end2.getX();
        double h = end2.getY();

        double ux = (f * g - e * h) * (c - a) - (b * c - a * d) * (g - e);
        double sx = (d - b) * (g - e) - (c - a) * (h - f);

        double uy = (f * g - e * h) * (d - b) - (b * c - a * d) * (h - f);
        double sy = (d - b) * (g - e) - (c - a) * (h - f);

        double x = ux / sx;
        double y = uy / sy;

        if (Double.isInfinite(x) || Double.isInfinite(y) || Double.isNaN(x) || Double.isNaN(y))
            return null;

        Vec2d poi = new Vec2d(x, y);

        boolean x1Flg = isInnerNum(poi.getX(), start1.getX(), end1.getX());
        boolean y1Flg = isInnerNum(poi.getY(), start1.getY(), end1.getY());
        boolean x2Flg = isInnerNum(poi.getX(), start2.getX(), end2.getX());
        boolean y2Flg = isInnerNum(poi.getY(), start2.getY(), end2.getY());
        if (x1Flg && y1Flg && x2Flg && y2Flg)
            return poi;

        return null;
    }

    public static boolean isInnerNum(double target, double num, double num2) {
        return Math.min(num, num2) <= target && target <= Math.max(num, num2);
    }
}
