package com.eric.revill.d.e;

/**
 * Pantalla de Lectura y Reclamo de Correo del Buzón.
 * 
 * Muestra la bandeja de entrada del jugador con las cartas recibidas, el remitente,
 * el cuerpo del mensaje y un botón para reclamar los ítems adjuntos a su inventario.
 */

import java.util.List;
import com.eric.revill.fabric.api.EnvType;
import com.eric.revill.fabric.api.Environment;
import com.eric.revill.fabric.fabric.api.client.networking.v1.ClientPlayNetworking;
import com.eric.revill.fabric.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

@Environment(EnvType.CLIENT)
public final class n extends AbstractContainerScreen<com.eric.revill.m.c> {
   private static final int a = 176;
   private static final int b = 228;
   private static final int c = 4;
   private int d = -1;
   private int e;

   public n(com.eric.revill.m.c handler, Inventory inventory, Component title) {
      super(handler, inventory, title);
      this.imageWidth = 176;
      this.imageHeight = 228;
      this.titleLabelX = 0;
      this.titleLabelY = 0;
   }

   protected void renderBg(GuiGraphics context, float delta, int mouseX, int mouseY) {
      o.a(context, this.leftPos, this.topPos, 176, 228);
      context.fill(this.leftPos + 7, this.topPos + 28, this.leftPos + 169, this.topPos + 220, -15260884);
      if (this.d >= 0 && this.d < ((com.eric.revill.m.c)this.menu).c().size()) {
         this.a(context, this.leftPos, this.topPos);
      }
   }

   private void a(GuiGraphics context, int panelX, int panelY) {
      for (int row = 0; row < 3; row++) {
         for (int column = 0; column < 7; column++) {
            o.a(context, panelX + 13 + column * 18, panelY + 128 + row * 18);
         }
      }
   }

   protected void renderLabels(GuiGraphics context, int mouseX, int mouseY) {
      List<com.eric.revill.m.c.a> mails = ((com.eric.revill.m.c)this.menu).c();
      if (this.d >= 0 && this.d < mails.size()) {
         this.a(context, mails.get(this.d));
      } else {
         this.a(context, mails);
      }
   }

   private void a(GuiGraphics context, List<com.eric.revill.m.c.a> mails) {
      context.drawString(this.font, Component.literal("BUZÓN"), 69, 9, -2426625, false);
      o.a(context, 145, 5, 24, 19, -12192257);
      context.drawString(this.font, Component.literal("+"), 153, 9, -9963521, false);
      int first = this.e * 4;
      int visible = Math.min(4, Math.max(0, mails.size() - first));

      for (int row = 0; row < visible; row++) {
         com.eric.revill.m.c.a mail = mails.get(first + row);
         int cardY = 32 + row * 39;
         context.fill(10, cardY, 166, cardY + 35, -14668490);
         context.fill(10, cardY, 12, cardY + 35, mail.c() ? -9732729 : -12192257);
         context.drawString(this.font, Component.literal(this.font.plainSubstrByWidth(mail.a(), 138)), 17, cardY + 5, mail.c() ? -4667701 : -7798, false);
         context.drawString(this.font, Component.literal(this.font.plainSubstrByWidth(mail.b().replace('\n', ' '), 138)), 17, cardY + 20, -3155490, false);
      }

      if (mails.isEmpty()) {
         context.drawString(this.font, Component.literal("Sin correos recibidos"), 25, 90, -6378060, false);
      }

      int pageCount = Math.max(1, (mails.size() + 4 - 1) / 4);
      o.a(context, 10, 198, 42, 19, -8219747);
      context.drawString(this.font, Component.literal("<<<"), 18, 204, -2426625, false);
      context.drawString(this.font, Component.literal(this.e + 1 + "/" + pageCount), 66, 204, -4667701, false);
      o.a(context, 124, 198, 42, 19, -8219747);
      context.drawString(this.font, Component.literal(">>>"), 132, 204, -2426625, false);
   }

   private void a(GuiGraphics context, com.eric.revill.m.c.a mail) {
      context.drawString(this.font, Component.literal("<<"), 9, 9, -9963521, false);
      context.drawString(this.font, Component.literal("CORREO"), 68, 9, -2426625, false);
      context.drawString(this.font, Component.literal("DE: " + this.font.plainSubstrByWidth(mail.a(), 132)), 12, 32, -2426625, false);
      context.fill(11, 47, 165, 48, -11378325);
      this.a(context, mail.b(), 12, 54, 150, 9, 6);
      List<ItemStack> items = mail.d();
      boolean hasAttachments = items.stream().anyMatch(stack -> !stack.isEmpty());
      context.drawString(this.font, Component.literal("OBJETOS ADJUNTOS"), 12, 112, -9963521, false);
      int shown = Math.min(items.size(), 21);

      for (int index = 0; index < shown; index++) {
         ItemStack stack2 = items.get(index);
         if (!stack2.isEmpty()) {
            int itemX = 13 + index % 7 * 18;
            int itemY = 128 + index / 7 * 18;
            context.renderItem(stack2, itemX, itemY);
            context.renderItemDecorations(this.font, stack2, itemX, itemY);
         }
      }

      if (hasAttachments) {
         o.a(context, 8, 198, 76, 19, -12192257);
         context.drawString(this.font, Component.literal("RECOGER"), 23, 204, -9963521, false);
      }

      o.a(context, 92, 198, 76, 19, -42650);
      context.drawString(this.font, Component.literal("ELIMINAR"), 103, 204, -28005, false);
   }

   private void a(GuiGraphics context, String message, int x, int y, int maxWidth, int lineHeight, int maxLines) {
      List lines = this.font.split(Component.literal(message), maxWidth);

      for (int line = 0; line < Math.min(lines.size(), maxLines); line++) {
         context.drawString(this.font, (FormattedCharSequence)lines.get(line), x, y + line * lineHeight, -1774610, false);
      }
   }

   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      double localX = mouseX - (double)this.leftPos;
      double localY = mouseY - (double)this.topPos;
      if (button != 0) {
         return super.mouseClicked(mouseX, mouseY, button);
      } else if (this.d >= 0 && this.d < ((com.eric.revill.m.c)this.menu).c().size()) {
         if (this.a(localX, localY, 5, 5, 40, 25)) {
            this.d = -1;
            return true;
         } else if (this.a(localX, localY, 92, 198, 168, 217)) {
            FriendlyByteBuf buf = PacketByteBufs.create();
            buf.writeVarInt(((com.eric.revill.m.c)this.menu).containerId);
            buf.writeVarInt(this.d);
            ClientPlayNetworking.send(com.eric.revill.m.e.f, buf);
            return true;
         } else if (this.a(localX, localY, 8, 198, 84, 217) && this.a()) {
            FriendlyByteBuf buf = PacketByteBufs.create();
            buf.writeVarInt(((com.eric.revill.m.c)this.menu).containerId);
            buf.writeVarInt(this.d);
            ClientPlayNetworking.send(com.eric.revill.m.e.e, buf);
            return true;
         } else {
            return super.mouseClicked(mouseX, mouseY, button);
         }
      } else if (this.a(localX, localY, 145, 5, 169, 24)) {
         ClientPlayNetworking.send(com.eric.revill.m.e.a, PacketByteBufs.empty());
         return true;
      } else if (this.a(localX, localY, 10, 198, 52, 217)) {
         this.e = Math.max(0, this.e - 1);
         return true;
      } else if (this.a(localX, localY, 124, 198, 166, 217)) {
         int pageCount = Math.max(1, (((com.eric.revill.m.c)this.menu).c().size() + 4 - 1) / 4);
         this.e = Math.min(pageCount - 1, this.e + 1);
         return true;
      } else if (this.a(localX, localY, 10, 32, 166, 188)) {
         int row = (int)((localY - 32.0) / 39.0);
         int index = this.e * 4 + row;
         if (row >= 0 && row < 4 && index < ((com.eric.revill.m.c)this.menu).c().size()) {
            this.d = index;
            FriendlyByteBuf buf = PacketByteBufs.create();
            buf.writeVarInt(((com.eric.revill.m.c)this.menu).containerId);
            buf.writeVarInt(index);
            ClientPlayNetworking.send(com.eric.revill.m.e.d, buf);
         }

         return true;
      } else {
         return super.mouseClicked(mouseX, mouseY, button);
      }
   }

   private boolean a() {
      return this.d >= 0
         && this.d < ((com.eric.revill.m.c)this.menu).c().size()
         && ((com.eric.revill.m.c)this.menu).c().get(this.d).d().stream().anyMatch(stack -> !stack.isEmpty());
   }

   private boolean a(double pointX, double pointY, int minX, int minY, int maxX, int maxY) {
      return pointX >= (double)minX && pointX < (double)maxX && pointY >= (double)minY && pointY < (double)maxY;
   }

   public boolean isPauseScreen() {
      return false;
   }
}
