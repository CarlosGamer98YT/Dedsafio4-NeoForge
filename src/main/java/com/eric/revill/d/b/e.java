package com.eric.revill.d.b;

/**
 * Renderizador 3D para el Pedestal de Desafíos Diarios (BlockEntityRenderer).
 * 
 * Renderiza el ítem objetivo de la misión actual flotando y rotando suavemente
 * sobre la superficie del bloque pedestal en el mundo.
 */

import com.mojang.blaze3d.vertex.PoseStack;
import com.eric.revill.fabric.api.EnvType;
import com.eric.revill.fabric.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Font.DisplayMode;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.block.state.BlockState;

@Environment(EnvType.CLIENT)
public final class e implements BlockEntityRenderer<com.eric.revill.o.b> {
   public e(Context context) {
   }

   public void render(com.eric.revill.o.b pedestal, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
      Minecraft client = Minecraft.getInstance();
      BlockState glowState = (BlockState)com.eric.revill.o.e.a.defaultBlockState().setValue(com.eric.revill.o.a.a, true);
      BakedModel glowModel = client.getBlockRenderer().getBlockModel(glowState);
      int emissiveLight = 15728880;
      client.getBlockRenderer()
         .getModelRenderer()
         .renderModel(matrices.last(), vertexConsumers.getBuffer(RenderType.cutout()), glowState, glowModel, 1.0F, 1.0F, 1.0F, emissiveLight, overlay);
      if (!client.options.hideGui) {
         Font textRenderer = client.font;
         MutableComponent title = Component.translatable("block.dedsafio-4-mechanics.daily_pedestal.label");
         MutableComponent day = Component.translatable("block.dedsafio-4-mechanics.daily_pedestal.day", new Object[]{pedestal.a()});
         matrices.pushPose();
         matrices.translate(0.5, 2.62, 0.5);
         matrices.mulPose(client.gameRenderer.getMainCamera().rotation());
         matrices.scale(-0.019F, -0.019F, 0.019F);
         textRenderer.drawInBatch(
            title,
            (float)(-textRenderer.width(title)) / 2.0F,
            0.0F,
            -1,
            false,
            matrices.last().pose(),
            vertexConsumers,
            DisplayMode.NORMAL,
            -1610612736,
            15728880
         );
         textRenderer.drawInBatch(
            day,
            (float)(-textRenderer.width(day)) / 2.0F,
            10.0F,
            -10098433,
            false,
            matrices.last().pose(),
            vertexConsumers,
            DisplayMode.NORMAL,
            -1610612736,
            15728880
         );
         matrices.popPose();
      }
   }
}
