package com.eric.revill.d.b;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.eric.revill.fabric.api.EnvType;
import com.eric.revill.fabric.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class k {
   private static final ResourceLocation a = ResourceLocation.fromNamespaceAndPath("revill", "textures/environment/vortex.png");
   private static final float b = 0.01F;
   private boolean c;
   private float d;

   public void a(boolean active) {
      if (active && !this.c) {
         this.d = 0.0F;
      }

      this.c = active;
   }

   public boolean a() {
      return this.d > 0.01F;
   }

   public boolean b() {
      return this.a();
   }

   public float c() {
      return this.d;
   }

   public void a(Minecraft client) {
      this.d = this.c ? Math.min(1.0F, this.d + 0.01F) : Math.max(0.0F, this.d - 0.01F);
   }

   public void a(PoseStack matrices, Camera camera, float tickDelta) {
      if (this.a()) {
         ;
      }
   }

   public void a(GuiGraphics context) {
      if (this.a()) {
         int width = context.guiWidth();
         int height = context.guiHeight();
         float reveal = Math.min(1.0F, Math.max(0.0F, this.d));
         int alpha = (int)(40.0F * reveal);
         int color = alpha << 24 | 0xFF0000;
         RenderSystem.enableBlend();
         RenderSystem.defaultBlendFunc();
         context.fill(0, 0, width, height, color);
         RenderSystem.disableBlend();
      }
   }
}
