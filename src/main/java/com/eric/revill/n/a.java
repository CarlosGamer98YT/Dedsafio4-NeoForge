package com.eric.revill.n;

/**
 * Representación matemática de la trayectoria y velocidad de un meteorito o partícula.
 */

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Display.BlockDisplay;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class a {
   private final Vec3 a;
   private final Vec3 b;
   private Vec3 c;
   private Vec3 d;
   private final double e;
   private final BlockDisplay f;
   private boolean g = true;
   private boolean h;

   public a(Vec3 start, Vec3 target, double speed, BlockDisplay display) {
      this.a = start;
      this.b = target;
      this.c = start;
      this.e = speed;
      this.f = display;
      Vec3 direction = target.subtract(start).normalize();
      this.d = direction.scale(speed);
      this.g();
   }

   public void a(ServerLevel world) {
      if (this.g) {
         this.c = this.c.add(this.d);
         this.g();
         if (this.c.distanceTo(this.b) <= 2.5 || this.b(world)) {
            this.g = false;
            this.h = true;
         }
      }
   }

   public void a() {
      if (!this.f.isRemoved()) {
         this.f.discard();
      }
   }

   private void g() {
      this.f.setPos(this.c.x, this.c.y, this.c.z);
   }

   private boolean b(ServerLevel world) {
      int radius = 2;
      int minX = (int)Math.floor(this.c.x - (double)radius);
      int maxX = (int)Math.floor(this.c.x + (double)radius);
      int minY = (int)Math.floor(this.c.y - (double)radius);
      int maxY = (int)Math.floor(this.c.y + (double)radius);
      int minZ = (int)Math.floor(this.c.z - (double)radius);
      int maxZ = (int)Math.floor(this.c.z + (double)radius);

      for (int x = minX; x <= maxX; x++) {
         for (int y = minY; y <= maxY; y++) {
            for (int z = minZ; z <= maxZ; z++) {
               BlockPos blockPos = new BlockPos(x, y, z);
               BlockState state = world.getBlockState(blockPos);
               if (!state.getCollisionShape(world, blockPos).isEmpty()) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   public Vec3 b() {
      return this.c;
   }

   public Vec3 c() {
      return this.b;
   }

   public double d() {
      return this.e;
   }

   public boolean e() {
      return this.g;
   }

   public boolean f() {
      return this.h;
   }

   public void a(boolean exploded) {
      this.h = exploded;
   }
}
