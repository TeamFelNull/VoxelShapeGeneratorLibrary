package dev.felnull.vsgl;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class ModelInfo {
    private final List<JsonObject> elements = new ArrayList<>();

    protected ModelInfo(JsonObject model) {
        model.getAsJsonArray("elements").forEach(e -> elements.add(e.getAsJsonObject()));
    }

    public List<JsonObject> getElements() {
        return elements;
    }

    public int getElementSize() {
        return elements.size();
    }
}
