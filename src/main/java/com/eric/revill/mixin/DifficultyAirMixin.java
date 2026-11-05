package com.eric.revill.mixin;

/**
 * Inyección en la respiración de entidades vivas {@link net.minecraft.world.entity.LivingEntity}.
 * 
 * Implementa el reto de dificultad de asfixia: reduce drásticamente el tiempo de respiración bajo el agua
 * o aplica daño de ahogamiento acelerado según las reglas del día activo.
 */

import com.eric.revill.g.a;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({LivingEntity.class})
public abstract class DifficultyAirMixin {
   @Inject(
      method = {"decreaseAirSupply"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private void dedsafio4$consumeAirFaster(int air, CallbackInfoReturnable<Integer> cir) {
      if (a.a("dia7_ahogamiento_aumentado") && (Integer)cir.getReturnValue() < air) {
         cir.setReturnValue(air - 5);
      }
   }
}
