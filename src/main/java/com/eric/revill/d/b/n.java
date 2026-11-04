package com.eric.revill.d.b;

import com.mojang.blaze3d.vertex.PoseStack;
import com.eric.revill.fabric.api.EnvType;
import com.eric.revill.fabric.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Font.DisplayMode;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
public final class n extends EntityRenderer<com.eric.revill.t.c> {
   private static final ResourceLocation a = ResourceLocation.fromNamespaceAndPath("minecraft", "textures/block/stone.png");
   private static final double b = 9216.0;

   public n(Context context) {
      super(context);
      this.shadowRadius = 0.0F;
   }

   public void render(com.eric.revill.t.c entity, float yaw, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light) {
      Minecraft client = Minecraft.getInstance();
      if (client.player != null && !(client.player.distanceToSqr(entity) > 9216.0)) {
         MutableComponent label = Component.literal(this.a(entity.c()) + " " + entity.b());
         Font textRenderer = client.font;
         int textWidth = textRenderer.width(label);
         float distance = Mth.sqrt((float)client.player.distanceToSqr(entity));
         float scale = 0.025F * Mth.clamp(48.0F / Math.max(1.0F, distance), 0.45F, 1.35F);
         int color = entity.d() | 0xFF000000;
         matrices.pushPose();
         matrices.translate(0.0, 0.25, 0.0);
         matrices.mulPose(client.gameRenderer.getMainCamera().rotation());
         matrices.scale(-scale, -scale, scale);
         textRenderer.drawInBatch(
            label,
            (float)(-textWidth) / 2.0F,
            0.0F,
            color,
            false,
            matrices.last().pose(),
            vertexConsumers,
            DisplayMode.NORMAL,
            Integer.MIN_VALUE,
            light
         );
         matrices.popPose();
      }
   }

   private String a(String icon) {
      return switch (icon) {
         case "star" -> "*";
         case "flag" -> "F";
         case "warning" -> "!";
         case "bunker" -> "B";
         default -> "+";
      };
   }

   @Override
   public ResourceLocation getTextureLocation(com.eric.revill.t.c entity) {
      return a;
   }
}
