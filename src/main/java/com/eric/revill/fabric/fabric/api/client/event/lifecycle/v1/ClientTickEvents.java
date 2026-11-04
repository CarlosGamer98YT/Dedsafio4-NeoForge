package com.eric.revill.fabric.fabric.api.client.event.lifecycle.v1;

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
