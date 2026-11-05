package com.eric.revill.mixin;

/**
 * Inyección en la percepción de entidades vivas {@link net.minecraft.world.entity.LivingEntity}.
 * 
 * Permite que los monstruos que se han vuelto invisibles por las reglas de dificultad
 * mantengan su hostilidad y detección hacia los jugadores sin perder el objetivo.
 */

import com.eric.revill.g.a;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({LivingEntity.class})
public abstract class InvisibleMobDetectionMixin {
   @Inject(
      method = {"hasLineOfSight"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private void dedsafio4$detectInvisibleTargets(Entity target, CallbackInfoReturnable<Boolean> cir) {
      if ((Object)this instanceof Mob && a.a("dia7_mobs_detectan_invisibles") && target.isInvisible()) {
         cir.setReturnValue(true);
      }
   }
}
