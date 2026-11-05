package com.eric.revill.d.b;

/**
 * Renderizador de la animación visual de Fusión / Transformación.
 * 
 * Proyecta una secuencia de texturas animadas en el centro de la pantalla al ejecutarse
 * el comando {@code /fusion} o activarse el evento correspondiente.
 */

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.platform.NativeImage.Format;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.imageio.ImageIO;
import com.eric.revill.fabric.api.EnvType;
import com.eric.revill.fabric.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.sounds.SoundEvent;

@Environment(EnvType.CLIENT)
public class g {
   private static final ResourceLocation a = ResourceLocation.fromNamespaceAndPath("revill", "fusion_gif_texture");
   private static final ResourceLocation b = ResourceLocation.fromNamespaceAndPath("revill", "fusion");
   private static final int c = 60;
   private static final int d = 12;
   private static final int e = 40;
   private static final int f = 200;
   private final List<BufferedImage> g = new ArrayList<>();
   private DynamicTexture h;
   private boolean i;
   private boolean j;
   private boolean k;
   private boolean l;
   private int m;
   private int n;
   private int o;
   private boolean p;
   private FusionState q = FusionState.a;
   private SimpleSoundInstance r;
   private boolean s;

   public void a(boolean enabled) {
      if (enabled != this.j || this.k) {
         this.l = enabled;
         this.k = true;
         this.m = 0;
         this.n = 0;
         this.i = true;
         this.q = FusionState.b;
         this.o = 0;
         this.p = false;
         this.j();
         this.g();
         Minecraft client = Minecraft.getInstance();
         if (client != null) {
            client.setScreen(new com.eric.revill.d.e.j(this));
         }
      }
   }

   public void a() {
      if (!this.k) {
         boolean nextState;
         this.l = nextState = !this.j;
         this.k = true;
         this.m = 0;
         this.n = 0;
         this.i = true;
         this.q = FusionState.b;
         this.o = 0;
         this.p = false;
         this.j();
         this.g();
         Minecraft client = Minecraft.getInstance();
         if (client != null) {
            client.setScreen(new com.eric.revill.d.e.j(this));
         }
      }
   }

   public boolean b() {
      return this.i;
   }

   public boolean c() {
      return this.j;
   }

   public void d() {
      this.k = false;
      this.i = false;
      this.a(Minecraft.getInstance(), false);
      this.l = false;
      this.q = FusionState.a;
      this.n = 0;
      this.r = null;
      Minecraft client = Minecraft.getInstance();
      if (client != null && client.screen instanceof com.eric.revill.d.e.j) {
         client.setScreen(null);
      }
   }

   public void e() {
      if (!this.k) {
         this.i = this.j;
      } else {
         this.m++;
         this.n++;
         Minecraft client = Minecraft.getInstance();
         boolean soundPlaying = client != null && this.r != null && client.getSoundManager().isActive(this.r);
         if (soundPlaying) {
            this.p = true;
         }

         this.i = true;
         switch (this.q) {
            case b:
               if (this.n >= 40) {
                  this.q = FusionState.c;
                  this.n = 0;
               }
               break;
            case c:
               if (!soundPlaying && (this.p || ++this.o >= 200)) {
                  this.q = FusionState.d;
                  this.n = 0;
                  this.a(client, this.l);
                  this.i = this.j;
               }
               break;
            case d:
               if (this.n >= 40) {
                  this.a(client);
               }
         }
      }
   }

   public void a(GuiGraphics context) {
      if (this.k && !this.g.isEmpty()) {
         int width = context.guiWidth();
         int height = context.guiHeight();
         this.h();
         float gifAlpha = this.f();
         RenderSystem.enableBlend();
         RenderSystem.defaultBlendFunc();
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, gifAlpha);
         context.blit(a, 0, 0, 0.0F, 0.0F, width, height, width, height);
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
         RenderSystem.disableBlend();
      }
   }

   private void a(Minecraft client) {
      this.k = false;
      this.a(client, this.l);
      this.i = this.j;
      this.m = 0;
      this.n = 0;
      this.q = FusionState.a;
      this.r = null;
      if (client != null && client.screen instanceof com.eric.revill.d.e.j) {
         client.setScreen(null);
      }
   }

   private void a(Minecraft client, boolean enabled) {
      if (this.j != enabled) {
         this.j = enabled;
         if (client != null && client.levelRenderer != null && client.player != null && client.level != null) {
            int radius = (Integer)client.options.renderDistance().get() * 16;
            int x = client.player.getBlockX();
            int z = client.player.getBlockZ();
            client.levelRenderer.setBlocksDirty(x - radius, client.level.getMinBuildHeight(), z - radius, x + radius, client.level.getMaxBuildHeight(), z + radius);
            client.levelRenderer.needsUpdate();
         }
      }
   }

   private float f() {
      if (!this.k) {
         return 0.0F;
      } else {
         return switch (this.q) {
            case b -> Math.min(1.0F, (float)this.n / 40.0F);
            case c -> 1.0F;
            case d -> Math.max(0.0F, 1.0F - (float)this.n / 40.0F);
            default -> 0.0F;
         };
      }
   }

   private void g() {
      Minecraft client = Minecraft.getInstance();
      if (client != null && client.player != null) {
         this.r = SimpleSoundInstance.forUI(SoundEvent.createVariableRangeEvent(b), 1.0F);
         client.getSoundManager().play(this.r);
      }
   }

   private void h() {
      if (this.h != null) {
         this.i();
      } else if (!this.g.isEmpty()) {
         BufferedImage firstFrame = this.g.get(0);
         NativeImage image = new NativeImage(Format.RGBA, firstFrame.getWidth(), firstFrame.getHeight(), false);

         for (int y = 0; y < firstFrame.getHeight(); y++) {
            for (int x = 0; x < firstFrame.getWidth(); x++) {
               image.setPixelRGBA(x, y, firstFrame.getRGB(x, y));
            }
         }

         this.h = new DynamicTexture(image);
         Minecraft.getInstance().getTextureManager().register(a, this.h);
         this.i();
      }
   }

   private void i() {
      if (this.h != null && !this.g.isEmpty()) {
         int frameIndex = this.m * 12 / 20 % this.g.size();
         BufferedImage frame = this.g.get(frameIndex);
         NativeImage image = this.h.getPixels();
         if (image != null) {
            for (int y = 0; y < frame.getHeight() && y < image.getHeight(); y++) {
               for (int x = 0; x < frame.getWidth() && x < image.getWidth(); x++) {
                  image.setPixelRGBA(x, y, frame.getRGB(x, y));
               }
            }

            this.h.upload();
         }
      }
   }

   private void j() {
      if (!this.s) {
         try {
            Minecraft client = Minecraft.getInstance();
            if (client == null || client.getResourceManager() == null) {
               return;
            }

            for (int index = 1; index <= 60; index++) {
               ResourceLocation frameId = ResourceLocation.fromNamespaceAndPath("revill", String.format("textures/fusion/ezgif-frame-%03d.jpg", index));
               Optional resource = client.getResourceManager().getResource(frameId);
               if (resource.isEmpty()) {
                  this.g.clear();
                  return;
               }

               try (InputStream stream = ((Resource)resource.get()).open()) {
                  BufferedImage frame = ImageIO.read(stream);
                  if (frame == null) {
                     this.g.clear();
                     return;
                  }

                  this.g.add(frame);
               }
            }

            this.s = this.g.size() == 60;
         } catch (IOException var10) {
            this.g.clear();
         }
      }
   }

   @Environment(EnvType.CLIENT)
   private static enum FusionState {
      a,
      b,
      c,
      d;

      public static FusionState[] a() {
         return values();
      }

      public static FusionState a(String name) {
         return valueOf(name);
      }
   }
}
