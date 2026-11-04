package com.eric.revill.mixin;

import com.eric.revill.g.a;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SculkShriekerBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({Warden.class})
public abstract class WardenEntityMixin {
   @Unique
   private boolean dedsafio4$sculkSpawned;

   @Inject(
      method = {"tick"},
      at = {@At("TAIL")}
   )
   private void dedsafio4$onWardenTick(CallbackInfo ci) {
      if (a.a("dia1_wardens_sculk") && !this.dedsafio4$sculkSpawned) {
         Warden warden = (Warden)(Object)this;
         if (!warden.isAlive()) {
            Level world = warden.level();
            if (!world.isClientSide) {
               if (world instanceof ServerLevel sw) {
                  this.dedsafio4$sculkSpawned = true;
                  BlockPos pos = warden.blockPosition();
                  if (sw.getBlockState(pos).canBeReplaced()) {
                     sw.setBlockAndUpdate(pos, (BlockState)Blocks.SCULK_SHRIEKER.defaultBlockState().setValue(SculkShriekerBlock.CAN_SUMMON, true));
                  }
               }
            }
         }
      }
   }
}
