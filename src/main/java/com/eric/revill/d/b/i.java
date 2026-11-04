package com.eric.revill.d.b;

import com.mojang.blaze3d.vertex.PoseStack;
import com.eric.revill.fabric.api.EnvType;
import com.eric.revill.fabric.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Font.DisplayMode;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

@Environment(EnvType.CLIENT)
public final class i implements BlockEntityRenderer<com.eric.revill.m.g> {
   private static final int a = -1610612736;

   public i(Context context) {
   }

   public void render(com.eric.revill.m.g blockEntity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
      Minecraft client = Minecraft.getInstance();
      if (client.player != null && !client.options.hideGui) {
         int unread = h.a();
         MutableComponent label = unread == 0
            ? Component.translatable("block.dedsafio-4-mechanics.mailbox.status.empty")
            : Component.translatable("block.dedsafio-4-mechanics.mailbox.status.unread", new Object[]{unread});
         Font textRenderer = client.font;
         int textWidth = textRenderer.width(label);
         float scale = Math.min(0.02F, 2.0F / (float)Math.max(1, textWidth));
         matrices.pushPose();
         matrices.translate(0.5, 1.45, 0.5);
         matrices.mulPose(client.gameRenderer.getMainCamera().rotation());
         matrices.scale(-scale, -scale, scale);
         textRenderer.drawInBatch(
            label, (float)(-textWidth) / 2.0F, 0.0F, -1, false, matrices.last().pose(), vertexConsumers, DisplayMode.NORMAL, -1610612736, 15728880
         );
         matrices.popPose();
      }
   }
}
