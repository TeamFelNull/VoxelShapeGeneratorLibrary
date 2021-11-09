package dev.felnull.vsgl;

import dev.felnull.vsgl.physics.AABB;
import dev.felnull.vsgl.physics.AngledAABB;
import dev.felnull.vsgl.physics.Edge;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Util {
    public static Set<Edge> generateEdges(List<AABB> aabbs) {
        Set<Edge> edges = new HashSet<>();
        for (AABB aabb : aabbs) {

        }
        return edges;
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

}
