package com.eric.revill.mixin;

import com.eric.revill.RevillClient;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import com.mojang.math.Axis;
import com.eric.revill.fabric.api.EnvType;
import com.eric.revill.fabric.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin({LevelRenderer.class})
public abstract class WorldRendererMixin {
   private static final ResourceLocation CUSTOM_SKY_TEXTURE = ResourceLocation.fromNamespaceAndPath("revill", "textures/environment/vortex.png");

   @Inject(
      method = {"renderSky(Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;FLnet/minecraft/client/Camera;ZLjava/lang/Runnable;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void dedsafio4$renderCustomRedSky(
      Matrix4f frustumMatrix, Matrix4f projectionMatrix, float tickDelta, Camera camera, boolean thickFog, Runnable skyFogSetup, CallbackInfo ci
   ) {
      if (RevillClient.d.c()) {
         ci.cancel();
      } else if (RevillClient.a.b()) {
         ci.cancel();
         Minecraft client = Minecraft.getInstance();
         if (client != null) {
            float alpha = Math.min(1.0F, Math.max(0.0F, RevillClient.a.c()));
            if (!(alpha <= 0.01F)) {
               PoseStack matrices = new PoseStack();
               RenderSystem.disableCull();
               RenderSystem.enableBlend();
               RenderSystem.defaultBlendFunc();
               RenderSystem.depthMask(false);
               Tesselator tessellator = Tesselator.getInstance();
               RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
               RenderSystem.setShaderTexture(0, CUSTOM_SKY_TEXTURE);
               matrices.pushPose();
               float rotationAngle = (float)((double)(System.currentTimeMillis() % 360000L) * 0.005);
               matrices.mulPose(Axis.YP.rotationDegrees(rotationAngle));
               BufferBuilder bufferBuilder = tessellator.begin(Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
               int segments = 48;
               int stacks = 24;
               double radius = 100.0;

               for (int i2 = 0; i2 < stacks; i2++) {
                  float v0 = (float)i2 / (float)stacks;
                  float v1 = (float)(i2 + 1) / (float)stacks;
                  double phi0 = Math.PI * (double)v0;
                  double phi1 = Math.PI * (double)v1;
                  double y0 = radius * Math.cos(phi0);
                  double y1 = radius * Math.cos(phi1);
                  double r0 = radius * Math.sin(phi0);
                  double r1 = radius * Math.sin(phi1);
                  float stackCenter = (v0 + v1) * 0.5F;
                  float stackAlpha = alpha >= 1.0F ? 1.0F : (alpha <= 0.0F ? 0.0F : Math.min(1.0F, Math.max(0.0F, (alpha - stackCenter * 0.5F) / 0.6F)));
                  int sa = Math.round(stackAlpha * 255.0F);

                  for (int j2 = 0; j2 < segments; j2++) {
                     float u0 = (float)j2 / (float)segments;
                     float u1 = (float)(j2 + 1) / (float)segments;
                     double theta0 = (Math.PI * 2) * (double)u0;
                     double theta1 = (Math.PI * 2) * (double)u1;
                     double x00 = r0 * Math.cos(theta0);
                     double z00 = r0 * Math.sin(theta0);
                     double x01 = r0 * Math.cos(theta1);
                     double z01 = r0 * Math.sin(theta1);
                     double x10 = r1 * Math.cos(theta0);
                     double z10 = r1 * Math.sin(theta0);
                     double x11 = r1 * Math.cos(theta1);
                     double z11 = r1 * Math.sin(theta1);
                     Matrix4f matrix = matrices.last().pose();
                     bufferBuilder.addVertex(matrix, (float)x00, (float)y0, (float)z00).setUv(u0, v0).setColor(255, 255, 255, sa);
                     bufferBuilder.addVertex(matrix, (float)x10, (float)y1, (float)z10).setUv(u0, v1).setColor(255, 255, 255, sa);
                     bufferBuilder.addVertex(matrix, (float)x11, (float)y1, (float)z11).setUv(u1, v1).setColor(255, 255, 255, sa);
                     bufferBuilder.addVertex(matrix, (float)x01, (float)y0, (float)z01).setUv(u1, v0).setColor(255, 255, 255, sa);
                  }
               }

               BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());
               matrices.popPose();
               RenderSystem.depthMask(true);
               RenderSystem.disableBlend();
               RenderSystem.enableCull();
               RevillClient.e.a(matrices, RevillClient.a);
            }
         }
      }
   }

   @Inject(
      method = {"renderEndSky(Lcom/mojang/blaze3d/vertex/PoseStack;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void dedsafio4$hideEndSkyWhileBlack(PoseStack matrices, CallbackInfo ci) {
      if (RevillClient.d.c()) {
         ci.cancel();
      }
   }

   @Inject(
      method = {"renderClouds(Lcom/mojang/blaze3d/vertex/PoseStack;Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;FDDD)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void dedsafio4$hideCloudsDuringRedEvent(
      PoseStack matrices, Matrix4f modelViewMatrix, Matrix4f projectionMatrix, float tickDelta, double cameraX, double cameraY, double cameraZ, CallbackInfo ci
   ) {
      if (RevillClient.a.b()) {
         ci.cancel();
      }
   }

   @Inject(
      method = {"renderSectionLayer(Lnet/minecraft/client/renderer/RenderType;DDDLorg/joml/Matrix4f;Lorg/joml/Matrix4f;)V"},
      at = {@At("HEAD")}
   )
   private void dedsafio4$blackBlockTextureLayer(
      RenderType renderLayer, double cameraX, double cameraY, double cameraZ, Matrix4f modelViewMatrix, Matrix4f projectionMatrix, CallbackInfo ci
   ) {
      if (RevillClient.d.c()) {
         RenderSystem.setShaderColor(0.0F, 0.0F, 0.0F, 1.0F);
      }
   }

   @Inject(
      method = {"renderSectionLayer(Lnet/minecraft/client/renderer/RenderType;DDDLorg/joml/Matrix4f;Lorg/joml/Matrix4f;)V"},
      at = {@At("RETURN")}
   )
   private void dedsafio4$restoreBlockTextureLayer(
      RenderType renderLayer, double cameraX, double cameraY, double cameraZ, Matrix4f modelViewMatrix, Matrix4f projectionMatrix, CallbackInfo ci
   ) {
      if (RevillClient.d.c()) {
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      }
   }
}
