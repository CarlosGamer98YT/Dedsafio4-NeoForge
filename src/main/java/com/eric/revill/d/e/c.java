package com.eric.revill.d.e;

/**
 * Pantalla Principal de Navegación del Cajero Automático (ATM).
 * 
 * Muestra el menú de bienvenida con botones de acceso rápido para "Ingresar monedas",
 * "Retirar saldo" y consultar el balance actual del jugador.
 */

import com.eric.revill.fabric.api.EnvType;
import com.eric.revill.fabric.api.Environment;
import com.eric.revill.fabric.fabric.api.client.networking.v1.ClientPlayNetworking;
import com.eric.revill.fabric.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public final class c extends Screen {
   private static final ResourceLocation a = ResourceLocation.fromNamespaceAndPath("dedsafio-4-mechanics", "textures/gui/atm/cajero.png");
   private static final int b = 176;
   private static final int c = 166;
   private float d;
   private int e;
   private int f;
   private int g;
   private int h;

   public c() {
      super(Component.literal("Cajero Automático"));
   }

   protected void init() {
      this.d = Math.min(1.0F, Math.min((float)this.width / 176.0F, (float)this.height / 166.0F));
      this.g = Math.round(176.0F * this.d);
      this.h = Math.round(166.0F * this.d);
      this.e = (this.width - this.g) / 2;
      this.f = (this.height - this.h) / 2;
   }

   public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
      context.blit(a, this.e, this.f, 0.0F, 0.0F, this.g, this.h, 176, 166);
      super.render(context, mouseX, mouseY, delta);
   }

   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      if (button != 0) {
         return super.mouseClicked(mouseX, mouseY, button);
      } else {
         double imageX = (mouseX - (double)this.e) / (double)this.d;
         double imageY = (mouseY - (double)this.f) / (double)this.d;
         if (this.a(imageX, imageY, 25, 120, 82, 141)) {
            ClientPlayNetworking.send(com.eric.revill.h.d.d, PacketByteBufs.empty());
            return true;
         } else if (this.a(imageX, imageY, 90, 120, 150, 141)) {
            ClientPlayNetworking.send(com.eric.revill.h.d.b, PacketByteBufs.empty());
            return true;
         } else {
            return super.mouseClicked(mouseX, mouseY, button);
         }
      }
   }

   private boolean a(double x, double y, int left, int top, int right, int bottom) {
      return x >= (double)left && x <= (double)right && y >= (double)top && y <= (double)bottom;
   }

   public boolean isPauseScreen() {
      return false;
   }
}
