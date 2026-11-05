package com.eric.revill.mixin;

/**
 * Inyección en la entidad de perla de ender {@link net.minecraft.world.entity.projectile.ThrownEnderpearl}.
 * 
 * Aplica restricciones de teletransporte o efectos adversos en días donde el uso de ender pearls
 * está condicionado por las reglas del desafío.
 */

import com.eric.revill.g.a;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ThrownEnderpearl.class})
public abstract class EnderPearlEntityMixin {
   @Inject(
      method = {"onHit"},
      at = {@At("TAIL")}
   )
   private void dedsafio4$enchancedEnderPearlDamage(HitResult hitResult, CallbackInfo ci) {
      ThrownEnderpearl pearl = (ThrownEnderpearl)(Object)this;
      Level world = pearl.level();
      if (!world.isClientSide) {
         if (world instanceof ServerLevel sw) {
            Entity owner = pearl.getOwner();
            if (owner instanceof LivingEntity livingOwner) {
               float damageAmount;
               if (a.a("dia3_ender_pearls_damage10")) {
                  damageAmount = 10.0F;
                  damageAmount = 20.0F;
               } else {
                  damageAmount = 5.0F;
               }

               boolean ignoreArmor = a.a("dia1_ender_pearls_ignoran_armor") || a.a("dia3_ender_pearls_damage10");
               if (ignoreArmor || a.a("dia3_ender_pearls_damage10")) {
                  if (ignoreArmor && owner instanceof ServerPlayer player) {
                     float extra = damageAmount - 5.0F;
                     if (extra > 0.0F) {
                        player.hurt(sw.damageSources().magic(), extra);
                     } else if (a.a("dia1_ender_pearls_ignoran_armor")) {
                        player.hurt(sw.damageSources().magic(), 5.0F);
                     }
                  }
               }
            }
         }
      }
   }
}
