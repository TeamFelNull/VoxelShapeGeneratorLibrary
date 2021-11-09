package dev.felnull.vsgl;

import com.google.gson.JsonObject;
import dev.felnull.vsgl.physics.AABB;
import dev.felnull.vsgl.physics.AngledAABB;
import dev.felnull.vsgl.physics.Vec3d;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class ModelInfo {
    private final Set<AABB> aabbs;
    private final boolean angled;

    protected ModelInfo(JsonObject model) {
        List<AABB> rawAabbs = new ArrayList<>();
        AtomicBoolean angleble = new AtomicBoolean();
        model.getAsJsonArray("elements").forEach(e -> {
            JsonObject element = e.getAsJsonObject();
            var aabb = createAabb(element);
            if (aabb instanceof AngledAABB)
                angleble.set(true);
            rawAabbs.add(aabb);
        });
        this.aabbs = Collections.unmodifiableSet(Util.optimisationAabbs(rawAabbs));
        this.angled = angleble.get();
    }

    public Set<AABB> getAabbs() {
        return aabbs;
    }

    public int getSize() {
        return aabbs.size();
    }

    public boolean isAngled() {
        return angled;
    }

    private AABB createAabb(JsonObject element) {
        AABB aabb;
        if (element.get("rotation") == null) {
            var from = element.getAsJsonArray("from");
            var to = element.getAsJsonArray("to");
            aabb = new AABB(new Vec3d(from.get(0).getAsDouble(), from.get(1).getAsDouble(), from.get(2).getAsDouble()), new Vec3d(to.get(0).getAsDouble(), to.get(1).getAsDouble(), to.get(2).getAsDouble()));
        } else {
            var from = element.getAsJsonArray("from");
            var to = element.getAsJsonArray("to");
            var rotation = element.getAsJsonObject("rotation");
            var axis = rotation.get("axis").getAsString();
            var origin = rotation.getAsJsonArray("origin");
            aabb = new AngledAABB(rotation.get("angle").getAsDouble(), AngledAABB.Axis.getAxisByName(axis), new Vec3d(origin.get(0).getAsDouble(), origin.get(1).getAsDouble(), origin.get(2).getAsDouble()), new Vec3d(from.get(0).getAsDouble(), from.get(1).getAsDouble(), from.get(2).getAsDouble()), new Vec3d(to.get(0).getAsDouble(), to.get(1).getAsDouble(), to.get(2).getAsDouble()));
        }
        return aabb;
    }

}
