package com.eric.revill.mixin;

/**
 * Inyección en el comportamiento de hojas de árboles {@link net.minecraft.world.level.block.Block}.
 * 
 * Registra la rotura o caída natural de hojas para misiones diarias y modifica
 * la probabilidad de obtención de manzanas u objetos específicos.
 */

import com.eric.revill.g.a;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Block.class})
public abstract class LeavesBlockMixin {
   @Inject(
      method = {"getDrops(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;)Ljava/util/List;"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private static void dedsafio4$suppressSaplingDrops(
      BlockState state, ServerLevel world, BlockPos pos, BlockEntity blockEntity, CallbackInfoReturnable<List<ItemStack>> cir
   ) {
      if (state.getBlock() instanceof LeavesBlock) {
         if (a.a("dia1_arboles_sin_brotes")) {
            List<ItemStack> original = (List<ItemStack>)cir.getReturnValue();
            ArrayList<ItemStack> filtered = new ArrayList<>();

            for (ItemStack stack : original) {
               if (!stack.is(ItemTags.SAPLINGS)) {
                  filtered.add(stack);
               }
            }

            cir.setReturnValue(filtered);
         }
      }
   }
}
