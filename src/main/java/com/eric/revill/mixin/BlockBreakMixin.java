package com.eric.revill.mixin;

/**
 * Inyección en la clase base {@link net.minecraft.world.level.block.Block} para capturar la rotura de bloques.
 * 
 * Rastrea la destrucción de bloques por parte de los jugadores para registrar el progreso
 * de las misiones diarias (por ejemplo, minar diamantes, obsidiana o madera).
 */

import com.eric.revill.g.a;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.CaveSpider;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({Block.class})
public abstract class BlockBreakMixin {
   @Inject(
      method = {"destroy"},
      at = {@At("TAIL")}
   )
   private void dedsafio4$cobwebCaveSpider(LevelAccessor world, BlockPos pos, BlockState state, CallbackInfo ci) {
      if (a.a("dia4_aranas_cueva")) {
         if (world instanceof ServerLevel sw) {
            if (state.is(Blocks.COBWEB)) {
               if (!(sw.getRandom().nextFloat() >= 0.25F)) {
                  CaveSpider spider = (CaveSpider)EntityType.CAVE_SPIDER.create(sw);
                  if (spider != null) {
                     spider.moveTo(
                        (double)pos.getX() + 0.5, (double)pos.getY(), (double)pos.getZ() + 0.5, sw.getRandom().nextFloat() * 360.0F, 0.0F
                     );
                     spider.finalizeSpawn(sw, sw.getCurrentDifficultyAt(pos), MobSpawnType.SPAWNER, null);
                     sw.addFreshEntityWithPassengers(spider);
                  }
               }
            }
         }
      }
   }
}
