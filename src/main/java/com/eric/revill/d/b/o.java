package com.eric.revill.d.b;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import com.eric.revill.fabric.api.EnvType;
import com.eric.revill.fabric.api.Environment;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

@Environment(EnvType.CLIENT)
public final class o {
   private static final int a = 12;
   private static final long b = 83333333L;
   private static final int c = 165;
   private static final int d = 141;
   private static final float e = 60.0F;
   private static final float f = 30.0F;
   private static final float g = 112.0F;
   private static final String h = "textures/environment/zenith/";
   private o.SkyAnimationState i = o.SkyAnimationState.a;
   private int j;
   private long k;
   private long l;
   private boolean m = true;

   public void a() {
      if (this.i == o.SkyAnimationState.a || this.i == o.SkyAnimationState.e) {
         this.i = o.SkyAnimationState.b;
         this.k = 0L;
         this.l = 0L;
      }
   }

   public void a(k skyRenderer) {
      this.m = true;
      this.b(skyRenderer);
   }

   public void b() {
      this.m = false;
      if (this.i != o.SkyAnimationState.a && this.i != o.SkyAnimationState.e) {
         this.e();
      }
   }

   private void b(k skyRenderer) {
      if (this.i == o.SkyAnimationState.a) {
         skyRenderer.a(false);
      } else if (this.i != o.SkyAnimationState.e) {
         this.e();
      }
   }

   private void e() {
      this.i = o.SkyAnimationState.e;
      this.j = 0;
      this.k = 0L;
      this.l = System.nanoTime();
      com.eric.revill.d.c.a.g();
   }

   public float c() {
      float progress;
      if (this.i == o.SkyAnimationState.c) {
         progress = (float)(164 - this.j) / 164.0F;
      } else {
         if (this.i != o.SkyAnimationState.e) {
            return 0.0F;
         }

         progress = (float)this.j / 164.0F;
      }

      float fade = Math.min(progress, 1.0F - progress) / 0.1F;
      return Math.max(0.0F, Math.min(1.0F, fade));
   }

   public void d() {
      com.eric.revill.d.c.a.h();
      this.i = o.SkyAnimationState.a;
      this.j = 0;
      this.k = 0L;
      this.l = 0L;
   }

   public void a(PoseStack matrices, k skyRenderer) {
      long now = System.nanoTime();
      if (this.i == o.SkyAnimationState.b && skyRenderer.c() >= 1.0F) {
         this.i = o.SkyAnimationState.c;
         this.j = 164;
         this.k = 0L;
         this.l = now;
         com.eric.revill.d.c.a.g();
      }

      if (this.i != o.SkyAnimationState.a) {
         if (this.l == 0L) {
            this.l = now;
         } else {
            this.k = this.k + (now - this.l);
            this.l = now;
         }

         while (this.k >= 83333333L) {
            this.k -= 83333333L;
            this.c(skyRenderer);
            if (this.i != o.SkyAnimationState.a) {
            }
         }

         if (this.i != o.SkyAnimationState.a) {
            ResourceLocation texture = this.f();
            if (texture != null) {
               RenderSystem.enableBlend();
               RenderSystem.defaultBlendFunc();
               RenderSystem.disableDepthTest();
               RenderSystem.depthMask(false);
               RenderSystem.disableCull();
               RenderSystem.setShader(GameRenderer::getPositionTexShader);
               RenderSystem.setShaderTexture(0, texture);
               Matrix4f matrix = matrices.last().pose();
               BufferBuilder buffer = Tesselator.getInstance().begin(Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
               buffer.addVertex(matrix, -60.0F, 112.0F, -30.0F).setUv(0.0F, 0.0F);
               buffer.addVertex(matrix, -60.0F, 112.0F, 30.0F).setUv(0.0F, 1.0F);
               buffer.addVertex(matrix, 60.0F, 112.0F, 30.0F).setUv(1.0F, 1.0F);
               buffer.addVertex(matrix, 60.0F, 112.0F, -30.0F).setUv(1.0F, 0.0F);
               BufferUploader.drawWithShader(buffer.buildOrThrow());
               RenderSystem.depthMask(true);
               RenderSystem.enableDepthTest();
               RenderSystem.enableCull();
               RenderSystem.disableBlend();
            }
         }
      }
   }

   private void c(k skyRenderer) {
      switch (this.i) {
         case c:
            this.j--;
            if (this.j < 0) {
               this.i = o.SkyAnimationState.d;
               this.j = 0;
               com.eric.revill.d.c.a.h();
            }
            break;
         case b:
            this.j = (this.j + 1) % 141;
            break;
         case e:
            this.j++;
            if (this.j >= 165) {
               com.eric.revill.d.c.a.h();
               this.d();
               if (this.m) {
                  skyRenderer.a(false);
               }
            }
      }
   }

   private ResourceLocation f() {
      return switch (this.i) {
         case c, e -> this.a(this.j);
         case b -> this.b(this.j);
         default -> null;
      };
   }

   private ResourceLocation a(int index) {
      return ResourceLocation.fromNamespaceAndPath("revill", "textures/environment/zenith/" + "salir/frame_%04d.png".formatted(index + 1));
   }

   private ResourceLocation b(int index) {
      return ResourceLocation.fromNamespaceAndPath("revill", "textures/environment/zenith/" + "loop/frame_%04d.png".formatted(index + 1));
   }

   @Environment(EnvType.CLIENT)
   private static enum SkyAnimationState {
      a,
      b,
      c,
      d,
      e;

      public static o.SkyAnimationState[] a() {
         return values();
      }

      public static o.SkyAnimationState a(String name) {
         return valueOf(name);
      }
   }
}
