package com.eric.revill.d.e;

/**
 * Pantalla de Depósito de Monedas en el Cajero Automático (ATM).
 * 
 * Permite al jugador colocar monedas físicas en los slots del cajero e ingresarlas a su cuenta,
 * calculando automáticamente el total de Dedita a acreditar.
 */

import com.eric.revill.fabric.api.EnvType;
import com.eric.revill.fabric.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

@Environment(EnvType.CLIENT)
public final class b extends AbstractContainerScreen<com.eric.revill.h.a> {
   private static final ResourceLocation a = ResourceLocation.fromNamespaceAndPath("dedsafio-4-mechanics", "textures/gui/atm/ingresar.png");
   private static final int b = 176;
   private static final int c = 166;
   private float d;
   private int e;
   private int f;
   private int g;
   private int h;

   public b(com.eric.revill.h.a handler, Inventory inventory, Component title) {
      super(handler, inventory, title);
      this.imageWidth = 176;
      this.imageHeight = 166;
      this.titleLabelX = 0;
      this.titleLabelY = 0;
   }

   protected void init() {
      super.init();
      this.d = Math.min(1.0F, Math.min((float)this.width / 176.0F, (float)this.height / 166.0F));
      this.g = Math.round(176.0F * this.d);
      this.h = Math.round(166.0F * this.d);
      this.e = (this.width - this.g) / 2;
      this.f = (this.height - this.h) / 2;
      this.leftPos = 0;
      this.topPos = 0;
      ((com.eric.revill.h.a)this.menu).a(this.a());
   }

   private int[][] a() {
      int[][] positions = new int[45][2];

      for (int row = 0; row < 3; row++) {
         for (int column = 0; column < 3; column++) {
            int index = row * 3 + column;
            positions[index][0] = 39 + column * 18;
            positions[index][1] = 15 + row * 18;
         }
      }

      for (int var9 = 0; var9 < 3; var9++) {
         for (int column = 0; column < 9; column++) {
            int index = 9 + var9 * 9 + column;
            positions[index][0] = 8 + column * 18;
            positions[index][1] = 85 + var9 * 18;
         }
      }

      for (int column2 = 0; column2 < 9; column2++) {
         int index2 = 36 + column2;
         positions[index2][0] = 8 + column2 * 18;
         positions[index2][1] = 141;
      }

      return positions;
   }

   protected void renderBg(GuiGraphics context, float delta, int mouseX, int mouseY) {
      context.blit(a, 0, 0, 0.0F, 0.0F, 176, 166, 176, 166);
      this.a(context);
   }

   public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
      context.pose().pushPose();
      context.pose().translate((float)this.e, (float)this.f, 0.0F);
      context.pose().scale(this.d, this.d, 1.0F);
      super.render(context, Math.round((float)(mouseX - this.e) / this.d), Math.round((float)(mouseY - this.f) / this.d), delta);
      context.pose().popPose();
   }

   protected void renderLabels(GuiGraphics context, int mouseX, int mouseY) {
   }

   private void a(GuiGraphics context) {
      context.pose().pushPose();
      context.pose().translate(135.0F, 56.0F, 0.0F);
      context.pose().scale(0.65F, 0.65F, 1.0F);
      context.drawCenteredString(this.font, Component.literal("Se ingresará:"), 0, 0, -1);
      context.pose().popPose();
      int[] amounts = ((com.eric.revill.h.a)this.menu).c();
      com.eric.revill.h.c[] types = com.eric.revill.h.c.a();
      int totalWidth = 0;
      int visibleTypes = 0;

      for (com.eric.revill.h.c type : types) {
         int amount = amounts[type.ordinal()];
         if (amount > 0) {
            totalWidth += 18 + this.font.width(Integer.toString(amount));
            visibleTypes++;
         }
      }

      if (visibleTypes != 0) {
         float previewScale = 0.65F;
         int var14;
         float x = 135.0F - (float)(var14 = totalWidth + (visibleTypes - 1) * 3) * previewScale / 2.0F;

         for (com.eric.revill.h.c typex : types) {
            int amount = amounts[typex.ordinal()];
            if (amount > 0) {
               String amountText = Integer.toString(amount);
               context.pose().pushPose();
               context.pose().translate(x, 64.0F, 0.0F);
               context.pose().scale(previewScale, previewScale, 1.0F);
               context.renderItem(new ItemStack(typex.c()), 0, 0);
               context.drawString(this.font, amountText, 18, 4, -1);
               context.pose().popPose();
               x += (float)(18 + this.font.width(amountText) + 3) * previewScale;
            }
         }
      }
   }

   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      if (button == 0) {
         double imageX = (mouseX - (double)this.e) / (double)this.d;
         double imageY = (mouseY - (double)this.f) / (double)this.d;
         if (imageX >= 105.0 && imageX <= 145.0 && imageY >= 36.0 && imageY <= 55.0) {
            Minecraft client = Minecraft.getInstance();
            if (client != null && client.gameMode != null && client.player != null) {
               client.gameMode.handleInventoryButtonClick(((com.eric.revill.h.a)this.menu).containerId, 0);
               client.player.closeContainer();
            }

            return true;
         }
      }

      return super.mouseClicked((mouseX - (double)this.e) / (double)this.d, (mouseY - (double)this.f) / (double)this.d, button);
   }

   public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
      return super.mouseDragged(
         (mouseX - (double)this.e) / (double)this.d, (mouseY - (double)this.f) / (double)this.d, button, deltaX / (double)this.d, deltaY / (double)this.d
      );
   }

   public boolean mouseReleased(double mouseX, double mouseY, int button) {
      return super.mouseReleased((mouseX - (double)this.e) / (double)this.d, (mouseY - (double)this.f) / (double)this.d, button);
   }

   public boolean isPauseScreen() {
      return false;
   }
}
