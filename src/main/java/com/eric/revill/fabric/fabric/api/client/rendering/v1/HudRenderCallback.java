package com.eric.revill.fabric.fabric.api.client.rendering.v1;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.client.gui.GuiGraphics;

@FunctionalInterface
public interface HudRenderCallback {
    void onHudRender(GuiGraphics drawContext, float tickDelta);

    class Event {
        private final List<HudRenderCallback> listeners = new CopyOnWriteArrayList<>();
        public void register(HudRenderCallback listener) { listeners.add(listener); }
        public List<HudRenderCallback> getListeners() { return listeners; }
    }

    Event EVENT = new Event();
}
