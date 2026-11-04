package com.eric.revill.fabric.fabric.api.event.lifecycle.v1;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;

public final class ServerEntityEvents {
    @FunctionalInterface
    public interface Load {
        void onLoad(Entity entity, ServerLevel world);
    }

    public static class Event<T> {
        private final List<T> listeners = new CopyOnWriteArrayList<>();
        public void register(T listener) { listeners.add(listener); }
        public List<T> getListeners() { return listeners; }
    }

    public static final Event<Load> ENTITY_LOAD = new Event<>();
}
