package com.eric.revill.fabric.fabric.api.client.rendering.v1;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;

public final class EntityModelLayerRegistry {
    @FunctionalInterface
    public interface TexturedModelDataProvider {
        LayerDefinition createModelData();
    }

    public static final Map<ModelLayerLocation, TexturedModelDataProvider> PROVIDERS = new ConcurrentHashMap<>();

    public static void registerModelLayer(ModelLayerLocation layer, TexturedModelDataProvider provider) {
        PROVIDERS.put(layer, provider);
    }
}
