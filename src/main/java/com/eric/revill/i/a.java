package com.eric.revill.i;

/**
 * Entidad viva estática del Cajero Automático (ATM).
 * 
 * Representa la máquina de cajero en el mundo con física fija (sin gravedad).
 * Al hacer clic derecho, orienta su rotación hacia el jugador y abre el menú
 * interactivo del banco para gestionar el saldo de Dedita.
 */

import com.eric.revill.h.d;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class a extends Mob {
   private boolean a;

   public a(EntityType<? extends a> entityType, Level world) {
      super(entityType, world);
      this.setPersistenceRequired();
   }

   public void tick() {
      super.tick();
      this.setNoGravity(true);
      this.noPhysics = true;
      this.setDeltaMovement(Vec3.ZERO);
      this.setXRot(0.0F);
      if (!this.level().isClientSide && !this.a) {
         Player spawner = null;
         double closestDistance = 256.0;

         for (Player player : this.level().players()) {
            double distance;
            if (!player.isSpectator() && (distance = this.distanceToSqr(player)) < closestDistance) {
               closestDistance = distance;
               spawner = player;
            }
         }

         this.a(spawner);
      }
   }

   public void a(Player player) {
      float yaw = this.getYRot();
      if (player != null) {
         double dx = player.getX() - this.getX();
         double dz = player.getZ() - this.getZ();
         if (dx != 0.0 || dz != 0.0) {
            yaw = (float)(Math.toDegrees(Math.atan2(dz, dx)) - 90.0);
         }
      }

      yaw = (float)Math.round(yaw / 90.0F) * 90.0F;
      this.setYRot(yaw);
      this.setYBodyRot(yaw);
      this.setYHeadRot(yaw);
      this.a = true;
   }

   public boolean isPushable() {
      return false;
   }

   public void push(Entity entity) {
   }

   public boolean hurt(DamageSource source, float amount) {
      Entity entity = source.getEntity();
      Player player;
      if (entity instanceof Player && (player = (Player)entity).isCreative()) {
         this.discard();
         return true;
      } else {
         return false;
      }
   }

   public boolean removeWhenFarAway(double distanceSquared) {
      return false;
   }

   public InteractionResult mobInteract(Player player, InteractionHand hand) {
      if (this.level().isClientSide) {
         return InteractionResult.SUCCESS;
      } else {
         if (player instanceof ServerPlayer serverPlayer) {
            d.a(serverPlayer);
         }

         return InteractionResult.CONSUME;
      }
   }

   public void addAdditionalSaveData(CompoundTag nbt) {
      nbt.putBoolean("SpawnFacingInitialized", this.a);
   }

   public void readAdditionalSaveData(CompoundTag nbt) {
      this.a = nbt.getBoolean("SpawnFacingInitialized");
   }
}
