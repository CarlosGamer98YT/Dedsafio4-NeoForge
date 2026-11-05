package com.eric.revill.d.c;

/**
 * Gestor de shaders y efectos de posprocesado de pantalla en cliente.
 * 
 * Permite cargar y alternar pipelines de renderizado personalizados para simular:
 * - Efecto de pantalla roja al morir o recibir daño letal.
 * - Distorsión de grieta dimensional durante eventos especiales.
 * - Filtros de visión temática de DEDSAFIO 4.
 */

import com.eric.revill.fabric.api.EnvType;
import com.eric.revill.fabric.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractSoundInstance;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.TickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance.Attenuation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;

@Environment(EnvType.CLIENT)
public final class a {
   private static final ResourceLocation a = ResourceLocation.fromNamespaceAndPath("revill", "revill");
   private static final ResourceLocation b = ResourceLocation.fromNamespaceAndPath("revill", "grieta");
   private static final ResourceLocation c = ResourceLocation.fromNamespaceAndPath("revill", "nutria");
   private static final ResourceLocation d = ResourceLocation.fromNamespaceAndPath("revill", "muerte");
   private static a.RevillLoopSound e;
   private static SimpleSoundInstance f;
   private static SimpleSoundInstance g;
   private static a.RiftLoopSound h;

   private a() {
   }

   public static void a() {
      Minecraft client = Minecraft.getInstance();
      if (e != null && client.getSoundManager().isActive(e)) {
         e.b();
      } else {
         e = new a.RevillLoopSound();
         client.getSoundManager().play(e);
      }
   }

   public static void b() {
      if (e != null) {
         e.a();
      }
   }

   public static void c() {
      f = a(c);
   }

   public static boolean d() {
      return a(f);
   }

   public static void e() {
      g = a(d);
   }

   public static boolean f() {
      return a(g);
   }

   private static SimpleSoundInstance a(ResourceLocation soundId) {
      SimpleSoundInstance sound = new SimpleSoundInstance(
         soundId, SoundSource.MASTER, 1.0F, 1.0F, RandomSource.create(), false, 0, Attenuation.NONE, 0.0, 0.0, 0.0, true
      );
      Minecraft.getInstance().getSoundManager().play(sound);
      return sound;
   }

   private static boolean a(SimpleSoundInstance sound) {
      return sound != null && Minecraft.getInstance().getSoundManager().isActive(sound);
   }

   public static void g() {
      if (h == null || h.isStopped() || h.b()) {
         h = new a.RiftLoopSound();
         Minecraft.getInstance().getSoundManager().queueTickingSound(h);
      }
   }

   public static void h() {
      if (h != null) {
         h.a();
      }
   }

   @Environment(EnvType.CLIENT)
   private static final class RevillLoopSound extends AbstractSoundInstance implements TickableSoundInstance {
      private static final int a = 30;
      private int b;
      private int c = -1;
      private boolean d;

      private RevillLoopSound() {
         super(com.eric.revill.d.c.a.a, SoundSource.MASTER, RandomSource.create());
         this.volume = 1.0F;
         this.pitch = 1.0F;
         this.looping = true;
         this.delay = 0;
         this.attenuation = Attenuation.NONE;
         this.relative = true;
      }

      public void tick() {
         this.b++;
         if (this.c >= 0) {
            float fadeOut = 1.0F - (float)(this.b - this.c) / 30.0F;
            this.volume = Math.max(0.0F, fadeOut);
            this.d = fadeOut <= 0.0F;
         }
      }

      private void a() {
         if (this.c < 0) {
            this.c = this.b;
         }
      }

      private void b() {
         this.c = -1;
         this.volume = 1.0F;
      }

      public boolean isStopped() {
         return this.d;
      }
   }

   @Environment(EnvType.CLIENT)
   private static final class RiftLoopSound extends AbstractSoundInstance implements TickableSoundInstance {
      private static final int a = 12;
      private int b;
      private int c = -1;
      private boolean d;

      private RiftLoopSound() {
         super(com.eric.revill.d.c.a.b, SoundSource.MASTER, RandomSource.create());
         this.volume = 0.01F;
         this.pitch = 1.0F;
         this.looping = true;
         this.delay = 0;
         this.attenuation = Attenuation.NONE;
         this.relative = true;
      }

      public void tick() {
         this.b++;
         float fadeIn = Math.min(1.0F, (float)this.b / 12.0F);
         if (this.c < 0) {
            this.volume = fadeIn;
         } else {
            float fadeOut = 1.0F - (float)(this.b - this.c) / 12.0F;
            this.volume = Math.max(0.0F, Math.min(fadeIn, fadeOut));
            this.d = fadeOut <= 0.0F;
         }
      }

      private void a() {
         if (this.c < 0) {
            this.c = this.b;
         }
      }

      private boolean b() {
         return this.c >= 0;
      }

      public boolean isStopped() {
         return this.d;
      }
   }
}
