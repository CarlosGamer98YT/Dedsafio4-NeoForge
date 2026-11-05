package com.eric.revill.d.e;

/**
 * Pantalla de Cinemática de Muerte / Eliminación.
 * 
 * Reproduce una secuencia visual dramática antes de cambiar al jugador al modo espectador,
 * informando la causa de su eliminación del desafío.
 */

import com.eric.revill.fabric.api.EnvType;
import com.eric.revill.fabric.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public final class j extends Screen {
   private final com.eric.revill.d.b.g a;

   public j(com.eric.revill.d.b.g renderer) {
      super(Component.empty());
      this.a = renderer;
   }

   public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
      this.a.a(context);
   }

   public boolean isPauseScreen() {
      return false;
   }

   public boolean shouldCloseOnEsc() {
      return false;
   }
}
