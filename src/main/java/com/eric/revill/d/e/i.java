package com.eric.revill.d.e;

/**
 * Subpantalla de Administración de Economía.
 * 
 * Permite a los administradores añadir o sustraer monedas (Azul, Roja, Verde)
 * directamente al saldo de cualquier jugador conectado.
 */

import com.eric.revill.fabric.api.EnvType;
import com.eric.revill.fabric.api.Environment;
import com.eric.revill.fabric.fabric.api.client.networking.v1.ClientPlayNetworking;
import com.eric.revill.fabric.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public final class i extends Screen {
   private static final String[] a = new String[]{"Añadir", "Quitar", "Ver"};
   private static final String[] b = new String[]{"Azul", "Roja", "Verde"};
   private int c = 0;
   private int d = 0;
   private int e = 1;

   public i() {
      super(Component.literal("Coins"));
   }

   protected void init() {
      int centerX = this.width / 2;
      int centerY = this.height / 2;
      this.addRenderableWidget(Button.builder(Component.literal(a[0]), button -> {
         this.c = 0;
         this.a();
      }).bounds(centerX - 170, centerY - 80, 120, 20).build());
      this.addRenderableWidget(Button.builder(Component.literal(a[1]), button -> {
         this.c = 1;
         this.a();
      }).bounds(centerX - 170, centerY - 50, 120, 20).build());
      this.addRenderableWidget(Button.builder(Component.literal(a[2]), button -> {
         this.c = 2;
         this.a();
      }).bounds(centerX - 170, centerY - 20, 120, 20).build());
      this.addRenderableWidget(
         Button.builder(Component.literal("Tipo: " + b[this.d]), button -> this.d = (this.d + 1) % b.length)
            .bounds(centerX + 10, centerY - 80, 170, 20)
            .build()
      );
      this.addRenderableWidget(
         Button.builder(Component.literal("-"), button -> this.e = Math.max(1, this.e - 1)).bounds(centerX + 10, centerY - 50, 40, 20).build()
      );
      this.addRenderableWidget(
         Button.builder(Component.literal("+"), button -> this.e = Math.min(10000, this.e + 1)).bounds(centerX + 60, centerY - 50, 40, 20).build()
      );
      this.addRenderableWidget(Button.builder(Component.literal("Cantidad: " + this.e), button -> {
      }).bounds(centerX + 110, centerY - 50, 70, 20).build());
      this.addRenderableWidget(Button.builder(Component.literal("Cerrar"), button -> this.onClose()).bounds(centerX - 60, centerY + 30, 120, 20).build());
   }

   public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
      context.fill(0, 0, this.width, this.height, -16777216);
      super.render(context, mouseX, mouseY, delta);
      int centerX = this.width / 2;
      int centerY = this.height / 2;
      context.drawCenteredString(this.font, Component.literal("Menú de monedas"), centerX, centerY - 120, -1);
      context.drawCenteredString(this.font, Component.literal("Acción actual: " + a[this.c]), centerX, centerY - 100, -1);
      context.drawCenteredString(this.font, Component.literal("Tipo: " + b[this.d]), centerX + 90, centerY - 85, -1);
      context.drawCenteredString(this.font, Component.literal("Cantidad: " + this.e), centerX + 90, centerY - 25, -1);
   }

   private void a() {
      FriendlyByteBuf buf = PacketByteBufs.create();
      buf.writeVarInt(this.c);
      buf.writeVarInt(this.d);
      buf.writeVarInt(this.e);
      ClientPlayNetworking.send(com.eric.revill.h.d.f, buf);
      this.onClose();
   }

   public boolean isPauseScreen() {
      return false;
   }
}
