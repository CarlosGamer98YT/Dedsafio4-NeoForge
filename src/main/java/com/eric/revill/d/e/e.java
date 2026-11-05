package com.eric.revill.d.e;

/**
 * Pantalla contenedora de la Mochila Portátil (Ender Bag).
 * 
 * Renderiza la interfaz de cuadrícula 3x9 de la mochila vinculada al contenedor
 * {@link com.eric.revill.a.a}, permitiendo mover ítems entre inventarios.
 */

import com.eric.revill.fabric.api.EnvType;
import com.eric.revill.fabric.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

@Environment(EnvType.CLIENT)
public final class e extends AbstractContainerScreen<com.eric.revill.a.a> {
   private static final ResourceLocation a = ResourceLocation.fromNamespaceAndPath("minecraft", "textures/gui/container/generic_54.png");

   public e(com.eric.revill.a.a handler, Inventory inventory, Component title) {
      super(handler, inventory, title);
      this.imageWidth = 176;
      this.imageHeight = 167;
      this.inventoryLabelY = 72;
   }

   protected void renderBg(GuiGraphics context, float delta, int mouseX, int mouseY) {
      int left = (this.width - this.imageWidth) / 2;
      int top = (this.height - this.imageHeight) / 2;
      context.blit(a, left, top, 0, 0, this.imageWidth, 71);
      context.blit(a, left, top + 54 + 17, 0, 126, this.imageWidth, 96);
   }
}
