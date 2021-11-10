package dev.felnull.vsgl;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.felnull.vsgl.physics.AngledAABB;
import dev.felnull.vsgl.physics.Vec3d;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.stream.StreamSupport;

/**
 * VoxelShape自動生成する
 *
 * @author MORIMORI0317
 * @since 1.0
 */
public class VoxelShapeGenerator {
    private static final Gson GSON = new Gson();
    private final JsonObject model;
    private final ModelInfo modelInfo;

    public VoxelShapeGenerator(InputStream stream) {
        this(GSON.fromJson(new InputStreamReader(new BufferedInputStream(stream)), JsonObject.class));
    }

    public VoxelShapeGenerator(byte[] bytes) {
        this(GSON.fromJson(new String(bytes, StandardCharsets.UTF_8), JsonObject.class));
    }

    public VoxelShapeGenerator(JsonObject model) {
        this.model = Objects.requireNonNull(model);
        checkJsonModel(model);
        this.modelInfo = new ModelInfo(model);
    }

    public JsonObject getModel() {
        return model;
    }

    public ModelInfo getModelInfo() {
        return modelInfo;
    }

    /**
     * バージョン1のJsonを生成
     *
     * @return 生成したjson
     */
    public JsonObject generateV1() {
        if (modelInfo.isAngled())
            throw new VoxelShapeException("V1 has not support angled cube model");

        JsonObject jo = new JsonObject();
        jo.addProperty("time", System.currentTimeMillis());
        jo.addProperty("meta", "VoxelShapeGeneratorLibrary V" + BuildIn.VERSION);
        jo.addProperty("version", 1);

        JsonArray shapes = new JsonArray();
        modelInfo.getAabbs().forEach(n -> {
            JsonArray aabb = new JsonArray();
            Vec3d from = n.getFrom();
            Vec3d to = n.getTo();
            aabb.add(from.getX());
            aabb.add(from.getY());
            aabb.add(from.getZ());
            aabb.add(to.getX());
            aabb.add(to.getY());
            aabb.add(to.getZ());
            shapes.add(aabb);
        });

        jo.add("shapes", shapes);
        return jo;
    }

    /**
     * バージョン2のJsonを生成
     *
     * @return 生成したjson
     */
    public JsonObject generateV2() {
        long st = System.currentTimeMillis();

        JsonObject jo = new JsonObject();
        jo.addProperty("time", System.currentTimeMillis());
        jo.addProperty("meta", "VoxelShapeGeneratorLibrary V" + BuildIn.VERSION);
        jo.addProperty("version", 2);

        JsonArray shapes = new JsonArray();
        modelInfo.getAabbs().forEach(n -> {
            JsonArray aabb = new JsonArray();
            Vec3d from = n.getFrom();
            Vec3d to = n.getTo();
            aabb.add(from.getX());
            aabb.add(from.getY());
            aabb.add(from.getZ());
            aabb.add(to.getX());
            aabb.add(to.getY());
            aabb.add(to.getZ());
            shapes.add(aabb);
        });
        jo.add("shapes", shapes);

        JsonArray edges = new JsonArray();

        Util.generateEdges(modelInfo.getAabbs()).forEach(n -> {
            JsonArray aabb = new JsonArray();
            Vec3d from = n.getStart();
            Vec3d to = n.getEnd();
            aabb.add(from.getX());
            aabb.add(from.getY());
            aabb.add(from.getZ());
            aabb.add(to.getX());
            aabb.add(to.getY());
            aabb.add(to.getZ());
            edges.add(aabb);
        });

        jo.add("edges", edges);

        jo.addProperty("elapsed", System.currentTimeMillis() - st);
        return jo;
    }

    private void checkJsonModel(JsonObject jo) {
        if (!(jo.get("elements") instanceof JsonArray) || !StreamSupport.stream(jo.getAsJsonArray("elements").spliterator(), false).map(JsonElement::getAsJsonObject).allMatch(element -> {
            if (!(element.get("from") instanceof JsonArray) || !(element.get("to") instanceof JsonArray))
                return false;

            var from = element.getAsJsonArray("from");
            var to = element.getAsJsonArray("to");

            if (from.size() != 3 || to.size() != 3)
                return false;
            try {
                from.get(0).getAsDouble();
                to.get(0).getAsDouble();
                from.get(1).getAsDouble();
                to.get(1).getAsDouble();
                from.get(2).getAsDouble();
                to.get(2).getAsDouble();
            } catch (Exception ex) {
                return false;
            }

            if (element.get("rotation") == null)
                return true;

            if (!(element.get("rotation") instanceof JsonObject))
                return false;

            var rotation = element.getAsJsonObject("rotation");

            try {
                rotation.get("angle").getAsDouble();
                var axis = AngledAABB.Axis.getAxisByName(rotation.get("axis").getAsString());
                if (axis == null)
                    return false;
                if (!(rotation.get("origin") instanceof JsonArray))
                    return false;
                var origin = rotation.getAsJsonArray("origin");
                origin.get(0).getAsDouble();
                origin.get(1).getAsDouble();
                origin.get(2).getAsDouble();
            } catch (Exception ex) {
                return false;
            }

            return true;
        }))
            throw new VoxelShapeException("No json model");
    }

}
