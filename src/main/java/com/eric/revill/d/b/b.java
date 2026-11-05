package com.eric.revill.d.b;

/**
 * Renderizador de la entidad viviente del Cajero Automático en el mundo.
 * 
 * Asocia el modelo 3D con la textura {@code textures/entity/cajeroent.png} y renderiza
 * la máquina en el mundo respetando iluminación, orientación y sombras.
 */

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.eric.revill.fabric.api.EnvType;
import com.eric.revill.fabric.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public final class b extends EntityRenderer<com.eric.revill.i.a> {
   private static final ResourceLocation a = ResourceLocation.fromNamespaceAndPath("dedsafio-4-mechanics", "textures/entity/cajeroent.png");
   private final a b;

   public b(Context context) {
      super(context);
      this.b = new a(context.bakeLayer(com.eric.revill.d.b.a.a));
      this.shadowRadius = 0.0F;
   }

   @Override
   public void render(com.eric.revill.i.a entity, float yaw, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light) {
      matrices.pushPose();
      matrices.mulPose(Axis.YP.rotationDegrees(180.0F - entity.getViewYRot(tickDelta)));
      matrices.translate(0.0F, 1.5F, 0.0F);
      matrices.scale(1.0F, -1.0F, 1.0F);
      this.b.setupAnim(entity, 0.0F, 0.0F, (float)entity.tickCount + tickDelta, 0.0F, 0.0F);
      this.b.renderToBuffer(matrices, vertexConsumers.getBuffer(this.b.renderType(a)), light, OverlayTexture.NO_OVERLAY, -1);
      matrices.popPose();
      super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
   }

   @Override
   public ResourceLocation getTextureLocation(com.eric.revill.i.a entity) {
      return a;
   }
}
