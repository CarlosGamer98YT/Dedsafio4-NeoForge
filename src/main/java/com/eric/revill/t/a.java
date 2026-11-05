package com.eric.revill.t;

/**
 * Modelo de datos de un Waypoint.
 * 
 * Contiene ID único, nombre, posición de bloque, icono gráfico, color hexadecimal y dimensión.
 */

import net.minecraft.core.BlockPos;

public class a {
   private final String a;
   private final String b;
   private final BlockPos c;
   private final String d;
   private final int e;
   private final String f;

   public a(String id, String name, BlockPos pos, String icon, int color, String dimension) {
      this.a = id;
      this.b = name;
      this.c = pos;
      this.d = icon;
      this.e = color;
      this.f = dimension;
   }

   public String a() {
      return this.a;
   }

   public String b() {
      return this.b;
   }

   public BlockPos c() {
      return this.c;
   }

   public String d() {
      return this.d;
   }

   public int e() {
      return this.e;
   }

   public String f() {
      return this.f;
   }
}
