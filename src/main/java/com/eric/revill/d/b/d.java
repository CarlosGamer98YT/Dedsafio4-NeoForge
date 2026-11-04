package com.eric.revill.d.b;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.Locale;
import com.eric.revill.fabric.api.EnvType;
import com.eric.revill.fabric.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

@Environment(EnvType.CLIENT)
public final class d {
   private static final int[] a = new int[3];
   private static final double[] b = new double[3];
   private static int c;
   private static boolean d;

   private d() {
   }

   public static void a(int blue, int red, int green) {
      int[] next = new int[]{Math.max(0, blue), Math.max(0, red), Math.max(0, green)};

      for (int index = 0; index < a.length; index++) {
         if (next[index] != a[index]) {
            d = next[index] > a[index];
            c = 24;
            a[index] = next[index];
         }
      }
   }

   public static int[] a() {
      return (int[])a.clone();
   }

   public static void b() {
      for (int index = 0; index < b.length; index++) {
         b[index] = b[index] + ((double)a[index] - b[index]) * 0.28;
         if (Math.abs((double)a[index] - b[index]) < 0.5) {
            b[index] = (double)a[index];
         }
      }

      if (c > 0) {
         c--;
      }
   }

   public static void a(GuiGraphics context) {
      Minecraft client = Minecraft.getInstance();
      if (client != null && client.player != null && !client.options.hideGui) {
         com.eric.revill.h.c[] visibleTypes = c();
         if (visibleTypes.length != 0) {
            int padding = 5;
            int itemSize = 16;
            int gap = 8;
            int panelWidth = a(client, visibleTypes);
            int maxTextWidth = b(client, visibleTypes);
            int panelHeight = padding * 2 + itemSize;
            int left = context.guiWidth() - panelWidth - 10;
            int top = 10;
            context.fill(left + 4, top, left + panelWidth - 4, top + panelHeight, -921561829);
            context.fill(left + 2, top + 2, left + panelWidth - 2, top + panelHeight - 2, -921561829);
            context.fill(left, top + 4, left + panelWidth, top + panelHeight - 4, -921561829);

            for (int i2 = 0; i2 < visibleTypes.length; i2++) {
               com.eric.revill.h.c type = visibleTypes[i2];
               int index = type.ordinal();
               int x = left + padding + i2 * (itemSize + gap + maxTextWidth);
               int textWidth = client.font.width(a(b[index]));
               int iconX = x + textWidth + 4;
               int color = c > 0 ? (d ? -9443958 : -37006) : -920587;
               context.drawString(client.font, Component.literal(a(b[index])), x, top + padding + 4, color);
               RenderSystem.setShaderColor(1.35F, 1.35F, 1.35F, 1.0F);

               try {
                  context.renderItem(new ItemStack(type.c()), iconX, top + padding);
               } finally {
                  RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
               }
            }
         }
      }
   }

   public static int a(Minecraft client) {
      return a(client, c());
   }

   private static int a(Minecraft client, com.eric.revill.h.c[] visibleTypes) {
      return 10 + visibleTypes.length * (24 + b(client, visibleTypes));
   }

   private static int b(Minecraft client, com.eric.revill.h.c[] visibleTypes) {
      int maxWidth = 6;

      for (com.eric.revill.h.c type : visibleTypes) {
         maxWidth = Math.max(maxWidth, client.font.width(a((double)a[type.ordinal()])));
      }

      return maxWidth;
   }

   private static com.eric.revill.h.c[] c() {
      com.eric.revill.h.c[] ordered = new com.eric.revill.h.c[]{com.eric.revill.h.c.b, com.eric.revill.h.c.c, com.eric.revill.h.c.a};
      ArrayList<com.eric.revill.h.c> visible = new ArrayList<>();

      for (com.eric.revill.h.c type : ordered) {
         if (type == com.eric.revill.h.c.a || a[type.ordinal()] > 0) {
            visible.add(type);
         }
      }

      if (visible.isEmpty()) {
         visible.add(com.eric.revill.h.c.a);
      }

      return visible.toArray(new com.eric.revill.h.c[0]);
   }

   private static String a(double value) {
      return String.format(Locale.ROOT, "%,d", Math.round(value));
   }
}
