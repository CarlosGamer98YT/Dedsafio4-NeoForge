package com.eric.revill.n;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Display.BlockDisplay;
import net.minecraft.world.level.Level.ExplosionInteraction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.phys.Vec3;

public class b {
   private final List<a> a = new ArrayList<>();
   private int b = 0;

   public void a(ServerLevel world, int timeRemaining, com.eric.revill.f.a config) {
      if (world != null && config.n) {
         if (this.b == 0 || world.getServer().getTickCount() >= this.b) {
            int interval = this.a(timeRemaining, config);
            if (interval > 0 && this.a.size() < config.o) {
               this.a(world, config);
            }

            this.b = world.getServer().getTickCount() + interval;
         }

         Iterator<a> iterator = this.a.iterator();

         while (iterator.hasNext()) {
            a meteor = iterator.next();
            if (!meteor.e()) {
               if (meteor.f()) {
                  this.a(world, meteor, config);
               }

               iterator.remove();
            } else {
               meteor.a(world);
               if (meteor.e()) {
                  Vec3 position = meteor.b();
                  world.sendParticles(ParticleTypes.FLAME, position.x, position.y, position.z, 6, 0.15, 0.15, 0.15, 0.02);
                  world.sendParticles(ParticleTypes.SMOKE, position.x, position.y, position.z, 3, 0.12, 0.12, 0.12, 0.01);
               }
            }
         }
      }
   }

   public int a(int timeRemaining, com.eric.revill.f.a config) {
      if (timeRemaining >= 600) {
         return 80;
      } else if (timeRemaining >= 480) {
         return 60;
      } else if (timeRemaining >= 300) {
         return 45;
      } else if (timeRemaining >= 120) {
         return 25;
      } else if (timeRemaining >= 60) {
         return 15;
      } else {
         return timeRemaining >= 30 ? 10 : 8;
      }
   }

   public void a(ServerLevel world, com.eric.revill.f.a config) {
      if (this.a.size() < config.o) {
         List players = world.players();
         if (!players.isEmpty()) {
            ServerPlayer anchor = (ServerPlayer)players.get(world.random.nextInt(players.size()));
            double targetX = anchor.getX() + (world.random.nextDouble() * 192.0 - 96.0);
            double targetZ = anchor.getZ() + (world.random.nextDouble() * 192.0 - 96.0);
            BlockPos surfacePos = world.getHeightmapPos(Types.MOTION_BLOCKING, new BlockPos((int)targetX, 0, (int)targetZ));
            double targetY = (double)surfacePos.getY();
            int minHeight = Math.max(40, config.p);
            int maxHeight = Math.max(minHeight, config.q);
            double startY = targetY + (double)minHeight + (double)world.random.nextInt(maxHeight - minHeight + 1);
            double startX = targetX + world.random.nextDouble() * 80.0 - 40.0;
            double startZ = targetZ + world.random.nextDouble() * 80.0 - 40.0;
            Vec3 start = new Vec3(startX, startY, startZ);
            Vec3 target = new Vec3(targetX, targetY, targetZ);
            double speed = 1.6 + world.random.nextDouble() * 0.8;
            BlockDisplay display = this.a(world, start);
            a meteor = new a(start, target, speed, display);
            this.a.add(meteor);
         }
      }
   }

   public void a() {
      for (a meteor : this.a) {
         meteor.a();
      }

      this.a.clear();
      this.b = 0;
   }

   public int b() {
      return this.a.size();
   }

   private void a(ServerLevel world, a meteor, com.eric.revill.f.a config) {
      Vec3 pos = meteor.b();
      int ix = (int)Math.floor(pos.x);
      int iy = (int)Math.floor(pos.y);
      int iz = (int)Math.floor(pos.z);
      meteor.a();
      world.sendParticles(ParticleTypes.EXPLOSION_EMITTER, pos.x, pos.y, pos.z, 1, 0.0, 0.0, 0.0, 0.0);
      world.sendParticles(ParticleTypes.LARGE_SMOKE, pos.x, pos.y, pos.z, 60, 2.0, 1.5, 2.0, 0.08);
      world.sendParticles(ParticleTypes.FLAME, pos.x, pos.y, pos.z, 120, 3.0, 1.5, 3.0, 0.15);
      world.sendParticles(ParticleTypes.ASH, pos.x, pos.y + 1.0, pos.z, 160, 4.0, 2.5, 4.0, 0.05);
      world.playSound(null, pos.x, pos.y, pos.z, SoundEvents.GENERIC_EXPLODE, SoundSource.HOSTILE, 3.0F, 0.7F);
      int craterRadius = Math.max(12, config.v);
      int craterDepth = Math.max(5, craterRadius / 2);

      for (int dx = -craterRadius; dx <= craterRadius; dx++) {
         for (int dz = -craterRadius; dz <= craterRadius; dz++) {
            double dist2d = Math.sqrt((double)(dx * dx + dz * dz));
            if (!(dist2d > (double)craterRadius)) {
               int depth = (int)Math.max(1.0, (double)craterDepth * (1.0 - dist2d / (double)craterRadius));

               for (int dy = -depth; dy <= 1; dy++) {
                  BlockPos bpos = new BlockPos(ix + dx, iy + dy, iz + dz);
                  BlockState state = world.getBlockState(bpos);
                  if (!state.isAir() && !state.is(Blocks.BEDROCK)) {
                     world.setBlock(bpos, Blocks.AIR.defaultBlockState(), 3);
                  }
               }
            }
         }
      }

      int fireRadius = craterRadius + 2;

      for (int dx = -fireRadius; dx <= fireRadius; dx++) {
         for (int dzx = -fireRadius; dzx <= fireRadius; dzx++) {
            double dist2d = Math.sqrt((double)(dx * dx + dzx * dzx));
            if (!(dist2d < (double)(craterRadius - 1)) && !(dist2d > (double)fireRadius) && !(world.random.nextFloat() > 0.4F)) {
               for (int dyx = 2; dyx >= -2; dyx--) {
                  BlockPos below = new BlockPos(ix + dx, iy + dyx - 1, iz + dzx);
                  BlockPos firePos = new BlockPos(ix + dx, iy + dyx, iz + dzx);
                  BlockState belowState = world.getBlockState(below);
                  if (!belowState.isAir() && world.getBlockState(firePos).isAir()) {
                     world.setBlock(firePos, Blocks.FIRE.defaultBlockState(), 3);
                     break;
                  }
               }
            }
         }
      }

      if (config.r) {
         ExplosionInteraction sourceType = config.u ? ExplosionInteraction.TNT : ExplosionInteraction.NONE;
         world.explode(null, pos.x, pos.y, pos.z, (float)Math.max(6.0, config.s), false, sourceType);
      }
   }

   private BlockDisplay a(ServerLevel world, Vec3 position) {
      BlockDisplay display = new BlockDisplay(EntityType.BLOCK_DISPLAY, world);
      CompoundTag blockState = new CompoundTag();
      blockState.putString("Name", "minecraft:magma_block");
      CompoundTag transformation = new CompoundTag();
      transformation.put("translation", this.a(-2.0F, -2.0F, -2.0F));
      transformation.put("left_rotation", this.a(0.0F, 0.0F, 0.0F, 1.0F));
      transformation.put("scale", this.a(5.0F, 5.0F, 5.0F));
      transformation.put("right_rotation", this.a(0.0F, 0.0F, 0.0F, 1.0F));
      CompoundTag data = new CompoundTag();
      data.put("block_state", blockState);
      data.put("transformation", transformation);
      data.putFloat("view_range", 4.0F);
      data.putFloat("width", 5.0F);
      data.putFloat("height", 5.0F);
      display.load(data);
      display.setPos(position.x, position.y, position.z);
      display.setNoGravity(true);
      display.setSilent(true);
      display.setInvulnerable(true);
      world.addFreshEntity(display);
      return display;
   }

   private ListTag a(float... values) {
      ListTag list = new ListTag();

      for (float value : values) {
         list.add(FloatTag.valueOf(value));
      }

      return list;
   }
}
