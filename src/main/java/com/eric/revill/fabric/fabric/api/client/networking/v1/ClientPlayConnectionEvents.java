package com.eric.revill.fabric.fabric.api.client.networking.v1;

/**
 * Adaptador de eventos de conexión del cliente en Fabric API a NeoForge.
 * 
 * Notifica a los listeners cuando el cliente se conecta o desconecta de un servidor.
 */

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;

public final class ClientPlayConnectionEvents {
    @FunctionalInterface
    public interface Disconnect {
        void onPlayDisconnect(ClientPacketListener handler, Minecraft client);
    }

    public static final Event<Disconnect> DISCONNECT = new Event<>();

    public static class Event<T> {
        private final List<T> listeners = new CopyOnWriteArrayList<>();
        public void register(T listener) { listeners.add(listener); }
        public List<T> getListeners() { return listeners; }
    }
}
