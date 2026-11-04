package com.eric.revill.d.e;

import com.eric.revill.fabric.api.EnvType;
import com.eric.revill.fabric.api.Environment;
import com.eric.revill.fabric.fabric.api.client.networking.v1.ClientPlayNetworking;
import com.eric.revill.fabric.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

@Environment(EnvType.CLIENT)
public final class m extends AbstractContainerScreen<com.eric.revill.m.b> {
   private static final int a = 176;
   private static final int b = 236;
   private EditBox c;
   private EditBox d;

   public m(com.eric.revill.m.b handler, Inventory inventory, Component title) {
      super(handler, inventory, title);
      this.imageWidth = 176;
      this.imageHeight = 236;
      this.titleLabelX = 0;
      this.titleLabelY = 0;
   }

   protected void init() {
      super.init();
      this.c = new EditBox(this.font, this.leftPos + 8, this.topPos + 29, 160, 18, Component.literal("Destinatario"));
      this.c.setMaxLength(64);
      this.c.setHint(Component.literal("Para..."));
      this.addRenderableWidget(this.c);
      this.d = new EditBox(this.font, this.leftPos + 8, this.topPos + 51, 160, 18, Component.literal("Mensaje"));
      this.d.setMaxLength(512);
      this.d.setHint(Component.literal("Mensaje..."));
      this.addRenderableWidget(this.d);
      ((com.eric.revill.m.b)this.menu).a(this.a());
   }

   private int[][] a() {
      int[][] positions = new int[57][2];

      for (int row = 0; row < 3; row++) {
         for (int column = 0; column < 7; column++) {
            int index = row * 7 + column;
            positions[index][0] = 25 + column * 18;
            positions[index][1] = 85 + row * 18;
         }
      }

      for (int var8 = 0; var8 < 3; var8++) {
         for (int column = 0; column < 9; column++) {
            int index = 21 + var8 * 9 + column;
            positions[index][0] = 7 + column * 18;
            positions[index][1] = 155 + var8 * 18;
         }
      }

      for (int column2 = 0; column2 < 9; column2++) {
         positions[48 + column2][0] = 7 + column2 * 18;
         positions[48 + column2][1] = 209;
      }

      return positions;
   }

   protected void renderBg(GuiGraphics context, float delta, int mouseX, int mouseY) {
      o.a(context, this.leftPos, this.topPos, 176, 236);
      context.fill(this.leftPos + 7, this.topPos + 27, this.leftPos + 169, this.topPos + 48, -13286580);
      context.fill(this.leftPos + 8, this.topPos + 51, this.leftPos + 168, this.topPos + 70, -13286580);
      context.fill(this.leftPos + 9, this.topPos + 28, this.leftPos + 167, this.topPos + 46, -15656415);
      context.fill(this.leftPos + 9, this.topPos + 52, this.leftPos + 167, this.topPos + 68, -15656415);
      o.b(context, this.leftPos + 22, this.topPos + 80, 130, 58);

      for (int row = 0; row < 3; row++) {
         for (int column = 0; column < 7; column++) {
            o.a(context, this.leftPos + 25 + column * 18, this.topPos + 85 + row * 18);
         }
      }

      for (int var8 = 0; var8 < 4; var8++) {
         for (int column = 0; column < 9; column++) {
            o.a(context, this.leftPos + 7 + column * 18, this.topPos + 155 + var8 * 18);
         }
      }
   }

   protected void renderLabels(GuiGraphics context, int mouseX, int mouseY) {
      context.drawString(this.font, Component.literal("<<"), 8, 9, -9963521, false);
      context.drawString(this.font, Component.literal("MENSAJE"), 63, 9, -2426625, false);
      o.a(context, 132, 5, 37, 19, -12192257);
      context.drawString(this.font, Component.literal("ENVIAR"), 136, 11, -9963521, false);
      context.drawString(this.font, Component.literal("ADJUNTOS"), 8, 73, -9963521, false);
      context.drawString(this.font, Component.literal("INVENTARIO"), 8, 143, -2426625, false);
   }

   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      double localX = mouseX - (double)this.leftPos;
      double localY = mouseY - (double)this.topPos;
      if (button == 0 && this.a(localX, localY, 5, 5, 37, 24)) {
         ClientPlayNetworking.send(com.eric.revill.m.e.b, PacketByteBufs.empty());
         return true;
      } else if (button == 0 && this.a(localX, localY, 132, 5, 169, 24)) {
         FriendlyByteBuf buf = PacketByteBufs.create();
         buf.writeUtf(this.c.getValue().trim(), 64);
         buf.writeUtf(this.d.getValue().trim(), 512);
         ClientPlayNetworking.send(com.eric.revill.m.e.c, buf);
         return true;
      } else {
         return super.mouseClicked(mouseX, mouseY, button);
      }
   }

   private boolean a(double pointX, double pointY, int minX, int minY, int maxX, int maxY) {
      return pointX >= (double)minX && pointX < (double)maxX && pointY >= (double)minY && pointY < (double)maxY;
   }

   public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
      return this.minecraft != null && this.minecraft.options.keyInventory.matches(keyCode, scanCode)
         ? (this.c == null || !this.c.isFocused()) && (this.d == null || !this.d.isFocused())
         : super.keyPressed(keyCode, scanCode, modifiers);
   }

   public boolean isPauseScreen() {
      return false;
   }
}
