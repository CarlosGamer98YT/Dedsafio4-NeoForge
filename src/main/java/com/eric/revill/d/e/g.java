package com.eric.revill.d.e;

import com.eric.revill.fabric.api.EnvType;
import com.eric.revill.fabric.api.Environment;
import com.eric.revill.fabric.fabric.api.client.networking.v1.ClientPlayNetworking;
import com.eric.revill.fabric.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

@Environment(EnvType.CLIENT)
public final class g extends AbstractContainerScreen<com.eric.revill.o.d> {
   private EditBox a;

   public g(com.eric.revill.o.d handler, Inventory inventory, Component title) {
      super(handler, inventory, title);
      this.imageWidth = 176;
      this.imageHeight = 194;
      this.inventoryLabelY = 95;
   }

   protected void init() {
      super.init();
      this.a = new EditBox(this.font, this.leftPos + 99, this.topPos + 6, 28, 16, Component.empty());
      this.a.setMaxLength(2);
      this.a.setValue(Integer.toString(((com.eric.revill.o.d)this.menu).b()));
      this.addRenderableWidget(this.a);
      this.addRenderableWidget(Button.builder(Component.literal("OK"), button -> this.a()).bounds(this.leftPos + 132, this.topPos + 5, 36, 18).build());
   }

   private void a() {
      try {
         int day = Integer.parseInt(this.a.getValue());
         if (day < 1 || day > 99) {
            throw new NumberFormatException();
         }

         FriendlyByteBuf buf = PacketByteBufs.create();
         buf.writeBlockPos(((com.eric.revill.o.d)this.menu).a());
         buf.writeVarInt(day);
         ClientPlayNetworking.send(com.eric.revill.o.c.a, buf);
      } catch (NumberFormatException var3) {
         if (this.minecraft.player != null) {
            this.minecraft.player.displayClientMessage(Component.translatable("block.dedsafio-4-mechanics.daily_pedestal.invalid_config"), false);
         }
      }
   }

   protected void renderBg(GuiGraphics context, float delta, int mouseX, int mouseY) {
      context.fill(this.leftPos, this.topPos, this.leftPos + this.imageWidth, this.topPos + this.imageHeight, -15722464);
      context.renderOutline(this.leftPos, this.topPos, this.imageWidth, this.imageHeight, -13189142);
      context.fill(this.leftPos + 23, this.topPos + 32, this.leftPos + 151, this.topPos + 88, -14668490);
      context.renderOutline(this.leftPos + 23, this.topPos + 32, 128, 56, -12192257);

      for (int row = 0; row < 3; row++) {
         for (int column = 0; column < 7; column++) {
            o.a(context, this.leftPos + 25 + column * 18, this.topPos + 34 + row * 18);
         }
      }

      context.fill(this.leftPos + 6, this.topPos + 100, this.leftPos + 170, this.topPos + 184, -14668490);
      context.renderOutline(this.leftPos + 6, this.topPos + 100, 164, 84, -11312275);

      for (int var9 = 0; var9 < 4; var9++) {
         for (int column = 0; column < 9; column++) {
            int slotY = var9 < 3 ? 108 + var9 * 18 : 166;
            o.a(context, this.leftPos + 8 + column * 18, this.topPos + slotY);
         }
      }
   }

   protected void renderLabels(GuiGraphics context, int mouseX, int mouseY) {
      context.drawString(this.font, Component.translatable("block.dedsafio-4-mechanics.daily_pedestal.title"), 8, 10, -10098433, false);
      context.drawString(this.font, Component.translatable("block.dedsafio-4-mechanics.daily_pedestal.day_label"), 76, 10, -1, false);
      context.drawString(this.font, Component.translatable("container.inventory"), 8, 90, -2426625, false);
   }

   public boolean isPauseScreen() {
      return false;
   }
}
