package com.eric.revill.c;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;

public final class a {
   private static final ResourceLocation a = ResourceLocation.fromNamespaceAndPath("dedsafio-4-mechanics", "icons");

   private a() {
   }

   public static Component a(Component message, a.Icon icon) {
      return Component.empty()
         .append(Component.literal(String.valueOf(icon.f)).setStyle(Style.EMPTY.withFont(a)))
         .append(Component.literal(" "))
         .append(message);
   }

   public static enum Icon {
      a('\ue000'),
      b('\ue001'),
      c('\ue002'),
      d('\ue003'),
      e('\ue004');

      private final char f;

      public static a.Icon[] a() {
         return values();
      }

      public static a.Icon a(String name) {
         return valueOf(name);
      }

      private Icon(char glyph) {
         this.f = glyph;
      }
   }
}
