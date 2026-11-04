package com.eric.revill.fabric.fabric.api.client.rendering.v1;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.MultiBufferSource;

public final class WorldRenderEvents {
    public record Context(PoseStack matrixStack, MultiBufferSource consumers, Camera camera, float tickDelta) {}

    @FunctionalInterface
    public interface AfterEntities {
        void afterEntities(Context context);
    }

    public static class Event<T> {
        private final List<T> listeners = new CopyOnWriteArrayList<>();
        public void register(T listener) { listeners.add(listener); }
        public List<T> getListeners() { return listeners; }
    }

    public static final Event<AfterEntities> AFTER_ENTITIES = new Event<>();
}
