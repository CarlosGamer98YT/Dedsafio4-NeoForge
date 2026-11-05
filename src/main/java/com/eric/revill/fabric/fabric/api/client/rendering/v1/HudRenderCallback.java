package com.eric.revill.fabric.fabric.api.client.rendering.v1;

/**
 * Interfaz de callback de Fabric API para renderizado del HUD en pantalla.
 * 
 * Enlazada con el evento de renderizado de interfaz gráfica de NeoForge para dibujar elementos en pantalla.
 */

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
