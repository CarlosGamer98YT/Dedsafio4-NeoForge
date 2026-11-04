package com.eric.revill.mixin;

import com.eric.revill.g.a;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombieVillager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({LivingEntity.class})
public abstract class ZombieMixin {
   @Inject(
      method = {"hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"},
      at = {@At("RETURN")}
   )
   private void dedsafio4$boggedPoison(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
      if ((Boolean)cir.getReturnValue() && a.a("dia5_bogged_noche")) {
         if ((Object)this instanceof ServerPlayer player) {
            Entity entity = source.getEntity();
            Skeleton skeleton;
            if (entity instanceof Skeleton && (skeleton = (Skeleton)entity).getTags().contains("dedsafio4_bogged")) {
               player.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 0));
            }
         }
      }
   }

   @Inject(
      method = {"onClimbable()Z"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private void dedsafio4$allowClimbing(CallbackInfoReturnable<Boolean> cir) {
      if ((Object)this instanceof Zombie zombie) {
         if (!(zombie instanceof Drowned) && !(zombie instanceof ZombieVillager)) {
            if (a.a("dia4_zombies_mejorados")) {
               cir.setReturnValue(true);
            } else {
               if (a.a("dia2_zombies_trepadores") && zombie.hasEffect(MobEffects.FIRE_RESISTANCE)) {
                  cir.setReturnValue(true);
               }
            }
         }
      }
   }
}
