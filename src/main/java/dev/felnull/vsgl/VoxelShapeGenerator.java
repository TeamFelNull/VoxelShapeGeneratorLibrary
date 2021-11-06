package dev.felnull.vsgl;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class VoxelShapeGenerator {
    private static final Gson GSON = new Gson();
    private final JsonObject model;
    private final ModelInfo modelInfo;

    public VoxelShapeGenerator(InputStream stream) throws IOException {
        this(GSON.fromJson(new String(Util.streamToByteArray(new BufferedInputStream(stream)), StandardCharsets.UTF_8), JsonObject.class));
    }

    public VoxelShapeGenerator(byte[] bytes) {
        this(GSON.fromJson(new String(bytes, StandardCharsets.UTF_8), JsonObject.class));
    }

    public VoxelShapeGenerator(JsonObject model) {
        this.model = Objects.requireNonNull(model);
        this.modelInfo = new ModelInfo(model);
    }

    public JsonObject getModel() {
        return model;
    }

    public ModelInfo getModelInfo() {
        return modelInfo;
    }

    public JsonObject generate() {
        JsonObject jo = new JsonObject();
        jo.addProperty("time", System.currentTimeMillis());
        jo.addProperty("meta", "VoxelShapeGeneratorLibrary V" + BuildIn.VERSION);
        jo.addProperty("version", 1);

        JsonArray shapes = new JsonArray();
        modelInfo.getElements().forEach(n -> {
            JsonArray aabb = new JsonArray();
            JsonArray fromAry = n.getAsJsonArray("from");
            Vec3d from = new Vec3d(fromAry.get(0).getAsDouble(), fromAry.get(1).getAsDouble(), fromAry.get(2).getAsDouble());
            JsonArray toAry = n.getAsJsonArray("to");
            Vec3d to = new Vec3d(toAry.get(0).getAsDouble(), toAry.get(1).getAsDouble(), toAry.get(2).getAsDouble());
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
}
