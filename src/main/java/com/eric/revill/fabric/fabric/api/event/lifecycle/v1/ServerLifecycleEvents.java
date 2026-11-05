package com.eric.revill.fabric.fabric.api.event.lifecycle.v1;

/**
 * Adaptador de eventos del ciclo de vida del servidor de Fabric API a NeoForge.
 * 
 * Gestiona los callbacks de servidor iniciándose, iniciado, deteniéndose y detenido
 * para guardar datos del evento Revill, economías y buzones.
 */

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.server.MinecraftServer;

public final class ServerLifecycleEvents {
    @FunctionalInterface
    public interface ServerStarted {
        void onServerStarted(MinecraftServer server);
    }

    @FunctionalInterface
    public interface ServerStopping {
        void onServerStopping(MinecraftServer server);
    }

    public static class Event<T> {
        private final List<T> listeners = new CopyOnWriteArrayList<>();
        public void register(T listener) { listeners.add(listener); }
        public List<T> getListeners() { return listeners; }
    }

    public static final Event<ServerStarted> SERVER_STARTED = new Event<>();
    public static final Event<ServerStopping> SERVER_STOPPING = new Event<>();
}
