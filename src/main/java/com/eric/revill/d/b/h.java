package com.eric.revill.d.b;

import com.eric.revill.fabric.api.EnvType;
import com.eric.revill.fabric.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public final class h {
   private static final ResourceLocation a = ResourceLocation.fromNamespaceAndPath("revill", "textures/gui/mensaje.png");
   private static int b;

   private h() {
   }

   public static void a(int count) {
      b = Math.max(0, count);
   }

   public static int a() {
      return b;
   }

   public static void a(GuiGraphics context) {
      Minecraft client = Minecraft.getInstance();
      if (b != 0 && client != null && client.player != null && !client.options.hideGui) {
         String count = Integer.toString(b);
         int panelWidth = 39 + client.font.width(count);
         int right = context.guiWidth() - 10;
         int left = right - panelWidth;
         int top = 40;
         int height = 26;
         context.fill(left + 4, top, left + panelWidth - 4, top + height, -921561829);
         context.fill(left + 2, top + 2, left + panelWidth - 2, top + height - 2, -921561829);
         context.fill(left, top + 4, left + panelWidth, top + height - 4, -921561829);
         context.drawString(client.font, Component.literal(count), left + 7, top + 9, -920587);
         context.blit(a, left + 12 + client.font.width(count), top + 3, 20, 20, 0.0F, 0.0F, 32, 32, 32, 32);
      }
   }
}
