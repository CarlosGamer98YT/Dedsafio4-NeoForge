package com.eric.revill.d.b;

import com.mojang.blaze3d.systems.RenderSystem;
import com.eric.revill.fabric.api.EnvType;
import com.eric.revill.fabric.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public final class l {
   private static final ResourceLocation a = ResourceLocation.fromNamespaceAndPath("dedsafio-4-mechanics", "textures/gui/title/fondo.png");
   private static final int b = 1360;
   private static final int c = 752;

   private l() {
   }

   public static void a(GuiGraphics context) {
      int screenWidth = context.guiWidth();
      int screenHeight = context.guiHeight();
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      context.blit(a, 0, 0, screenWidth, screenHeight, 0.0F, 0.0F, 1360, 752, 1360, 752);
   }
}
