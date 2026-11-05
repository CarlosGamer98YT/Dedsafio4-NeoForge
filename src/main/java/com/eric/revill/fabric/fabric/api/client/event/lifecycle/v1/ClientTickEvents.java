package com.eric.revill.fabric.fabric.api.client.event.lifecycle.v1;

/**
 * Adaptador de eventos de ciclo de vida del cliente de Fabric API a NeoForge.
 * 
 * Traduce las suscripciones de inicio y fin de tick de cliente al bus de eventos de NeoForge
 * ({@link net.neoforged.neoforge.client.event.ClientTickEvent.Pre} y {@code Post}).
 */

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.client.Minecraft;

public final class ClientTickEvents {
    @FunctionalInterface
    public interface EndTick {
        void onEndTick(Minecraft client);
    }

    public static class Event<T> {
        private final List<T> listeners = new CopyOnWriteArrayList<>();
        public void register(T listener) { listeners.add(listener); }
        public List<T> getListeners() { return listeners; }
    }

    public static final Event<EndTick> END_CLIENT_TICK = new Event<>();
}
