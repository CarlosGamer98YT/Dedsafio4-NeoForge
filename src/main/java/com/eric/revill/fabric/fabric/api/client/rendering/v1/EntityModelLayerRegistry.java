package com.eric.revill.fabric.fabric.api.client.rendering.v1;

/**
 * Adaptador de registro de capas de modelos de entidad de Fabric API a NeoForge.
 * 
 * Registra definiciones de capas de modelos de entidades (como el modelo del Cajero Automático)
 * en el evento {@link net.neoforged.neoforge.client.event.EntityRenderersEvent.RegisterLayerDefinitions}.
 */

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
