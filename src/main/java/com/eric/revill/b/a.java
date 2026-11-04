package com.eric.revill.b;

import net.minecraft.core.BlockPos;

public class a {
   private String a = "";
   private BlockPos b;
   private BlockPos c;

   public boolean a() {
      return this.a != null && !this.a.isEmpty() && this.b != null && this.c != null;
   }

   public boolean a(BlockPos pos) {
      if (this.a() && pos != null) {
         int minX = Math.min(this.b.getX(), this.c.getX());
         int maxX = Math.max(this.b.getX(), this.c.getX());
         int minY = Math.min(this.b.getY(), this.c.getY());
         int maxY = Math.max(this.b.getY(), this.c.getY());
         int minZ = Math.min(this.b.getZ(), this.c.getZ());
         int maxZ = Math.max(this.b.getZ(), this.c.getZ());
         return pos.getX() >= minX
            && pos.getX() <= maxX
            && pos.getY() >= minY
            && pos.getY() <= maxY
            && pos.getZ() >= minZ
            && pos.getZ() <= maxZ;
      } else {
         return false;
      }
   }

   public String b() {
      return this.a;
   }

   public void a(String dimension) {
      this.a = dimension;
   }

   public BlockPos c() {
      return this.b;
   }

   public void b(BlockPos firstCorner) {
      this.b = firstCorner;
   }

   public BlockPos d() {
      return this.c;
   }

   public void c(BlockPos secondCorner) {
      this.c = secondCorner;
   }
}
