package com.eric.revill.d.b;

/**
 * Renderizador de la interfaz del "MOMENTO REVILL".
 * 
 * Superpone en la pantalla del jugador:
 * - El temporizador gigante en cuenta atrás con advertencia en letras rojas.
 * - La barra de objetivo (búnker, subir a Y=250 o descender a Y=-1).
 * - El filtro de cielo rojizo apocalíptico y viñeta de peligro.
 */

import com.eric.revill.fabric.api.EnvType;
import com.eric.revill.fabric.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public class j {
   private static final String a = "MOMENTO REVILL";
   private static final String b = "";
   private boolean c;
   private double d;
   private double e = 10.0;
   private double f = 1.5;
   private boolean g;

   public void a() {
      this.c = true;
      this.d = 0.0;
      this.g = true;
   }

   public void b() {
      this.c = false;
      this.g = false;
      this.d = 0.0;
   }

   public void a(boolean active) {
      this.c = active;
      if (active) {
         this.d = 0.0;
         this.g = true;
      }
   }

   public boolean c() {
      return this.c;
   }

   public void a(int seconds) {
      this.e = Math.max(1.0, (double)seconds);
   }

   public void a(double flashSpeed) {
      this.f = flashSpeed;
   }

   public void a(Minecraft client) {
      if (this.c && client != null) {
         this.d = this.d + (double)client.getTimer().getRealtimeDeltaTicks();
         if (this.d >= this.e) {
            this.b();
         }
      }
   }

   public void a(GuiGraphics drawContext, Font textRenderer, int width, int height) {
      if (this.c && this.g && "" != null && !"".isBlank()) {
         int centerX = width / 2;
         int titleY = height / 2 - 18;
         int bodyY = height / 2 + 8;
         double phase = this.d * this.f % 1.0;
         int glow = (int)Math.round(255.0 * (0.5 + 0.5 * Math.sin(phase * Math.PI * 2.0)));
         int alpha = Math.min(255, Math.max(0, glow));
         drawContext.drawString(
            textRenderer, Component.literal("MOMENTO REVILL"), centerX - textRenderer.width("MOMENTO REVILL") / 2, titleY, 16777215 | alpha << 24
         );
         drawContext.drawString(textRenderer, Component.literal(""), centerX - textRenderer.width("") / 2, bodyY, 16768477 | alpha << 24);
      }
   }
}
