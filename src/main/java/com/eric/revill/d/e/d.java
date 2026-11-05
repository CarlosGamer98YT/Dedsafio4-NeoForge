package com.eric.revill.d.e;

/**
 * Pantalla de Retirada de Saldo del Cajero Automático (ATM).
 * 
 * Interfaz con botones interactivos para convertir saldo virtual en monedas físicas
 * de Dedita Azul, Dedita Verde o Dedita Roja en el inventario del jugador.
 */

import com.eric.revill.fabric.api.EnvType;
import com.eric.revill.fabric.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public final class d extends Screen {
   private static final ResourceLocation a = ResourceLocation.fromNamespaceAndPath("dedsafio-4-mechanics", "textures/gui/atm/sacar.png");
   private static final int b = 176;
   private static final int c = 166;
   private static final int[] d = new int[]{51, 85, 119};
   private static final int e = 102;
   private static final int f = 75;
   private static final int g = 130;
   private static final int h = 101;
   private static final int i = 146;
   private static final com.eric.revill.h.c[] j = new com.eric.revill.h.c[]{com.eric.revill.h.c.b, com.eric.revill.h.c.c, com.eric.revill.h.c.a};
   private final int[] k = new int[3];
   private float l;
   private int m;
   private int n;
   private int o;
   private int p;

   public d() {
      super(Component.literal("Retirar deditas"));
   }

   protected void init() {
      this.l = Math.min(1.0F, Math.min((float)this.width / 176.0F, (float)this.height / 166.0F));
      this.o = Math.round(176.0F * this.l);
      this.p = Math.round(166.0F * this.l);
      this.m = (this.width - this.o) / 2;
      this.n = (this.height - this.p) / 2;
   }

   public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
      context.blit(a, this.m, this.n, 0.0F, 0.0F, this.o, this.p, 176, 166);
      int[] balances = com.eric.revill.d.b.d.a();

      for (int index = 0; index < d.length; index++) {
         int amount = Math.min(this.k[index], balances[j[index].ordinal()]);
         context.drawCenteredString(
            this.font,
            Component.literal(Integer.toString(amount)),
            this.m + Math.round((float)d[index] * this.l),
            this.n + Math.round(102.0F * this.l),
            -1
         );
      }

      super.render(context, mouseX, mouseY, delta);
   }

   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      if (button != 0) {
         return super.mouseClicked(mouseX, mouseY, button);
      } else {
         double imageX = (mouseX - (double)this.m) / (double)this.l;
         double imageY = (mouseY - (double)this.n) / (double)this.l;

         for (int index = 0; index < d.length; index++) {
            if (Math.abs(imageX - (double)d[index]) <= 12.0) {
               int[] balances = com.eric.revill.d.b.d.a();
               int balance = balances[j[index].ordinal()];
               if (imageY >= 54.0 && imageY <= 66.0) {
                  this.k[index] = Math.min(balance, this.k[index] + 1);
                  return true;
               }

               if (imageY >= 81.0 && imageY <= 95.0) {
                  this.k[index] = Math.max(0, this.k[index] - 1);
                  return true;
               }
            }
         }

         if (this.a(imageX, imageY, 75, 130, 101, 146)) {
            com.eric.revill.d.a.a.a(this.k[2], this.k[0], this.k[1]);
            this.onClose();
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
