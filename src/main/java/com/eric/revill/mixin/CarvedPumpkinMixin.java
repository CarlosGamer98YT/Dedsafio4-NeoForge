package com.eric.revill.mixin;

/**
 * Inyección en el bloque de calabaza tallada {@link net.minecraft.world.level.block.CarvedPumpkinBlock}.
 * 
 * Aplica restricciones o efectos de visión en retos específicos donde el uso de calabazas
 * es obligatorio o está penalizado.
 */

import com.eric.revill.g.a;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.CarvedPumpkinBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({CarvedPumpkinBlock.class})
public abstract class CarvedPumpkinMixin {
   @Inject(
      method = {"canSpawnGolem(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;)Z"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private void dedsafio4$noGolemDispense(LevelReader world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
      if (a.a("dia1_sin_golems")) {
         cir.setReturnValue(false);
      }
   }

   @Inject(
      method = {"trySpawnGolem(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void dedsafio4$preventGolemSpawn(Level world, BlockPos pos, CallbackInfo ci) {
      if (a.a("dia1_sin_golems")) {
         Player closest;
         if (!world.isClientSide && (closest = world.getNearestPlayer((double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), 8.0, false)) != null) {
            closest.displayClientMessage(Component.literal("§cLos Gólems ya no pueden ser invocados."), true);
         }

         ci.cancel();
      }
   }
}
