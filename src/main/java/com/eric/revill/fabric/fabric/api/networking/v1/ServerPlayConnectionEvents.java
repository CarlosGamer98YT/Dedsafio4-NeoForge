package com.eric.revill.fabric.fabric.api.networking.v1;

/**
 * Adaptador de eventos de conexión y desconexión de jugadores en el servidor.
 */

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public final class ServerPlayConnectionEvents {
    @FunctionalInterface
    public interface Join {
        void onPlayReady(ServerGamePacketListenerImpl handler, PacketSender sender, MinecraftServer server);
    }

    @FunctionalInterface
    public interface Disconnect {
        void onPlayDisconnect(ServerGamePacketListenerImpl handler, MinecraftServer server);
    }

    public static final Event<Join> JOIN = new Event<>();
    public static final Event<Disconnect> DISCONNECT = new Event<>();

    public static class Event<T> {
        private final List<T> listeners = new CopyOnWriteArrayList<>();
        public void register(T listener) { listeners.add(listener); }
        public List<T> getListeners() { return listeners; }
    }
}
