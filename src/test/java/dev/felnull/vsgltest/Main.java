package dev.felnull.vsgltest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dev.felnull.vsgl.VoxelShapeGenerator;

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    private static final Gson GSON = new Gson();

    public static void main(String[] args) throws Exception {
        VoxelShapeGenerator vsg = new VoxelShapeGenerator(new FileInputStream("samples/sample.json"));
        JsonObject jo = vsg.generateV1();
        Files.write(Paths.get("gen.json"), GSON.toJson(jo).getBytes(StandardCharsets.UTF_8));
    }
}
