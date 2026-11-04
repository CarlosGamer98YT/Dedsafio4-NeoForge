package com.eric.revill.mixin;

import com.eric.revill.g.a;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BasePressurePlateBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({BasePressurePlateBlock.class})
public abstract class PressurePlateDamageMixin extends Block {
   protected PressurePlateDamageMixin(Properties settings) {
      super(settings);
   }

   @Inject(
      method = {"entityInside"},
      at = {@At("HEAD")}
   )
   private void dedsafio4$damageOnActivation(BlockState state, Level world, BlockPos pos, Entity entity, CallbackInfo ci) {
      if (!world.isClientSide
         && a.a("dia7_dano_placa_presion")
         && entity instanceof Player player
         && state.hasProperty(BlockStateProperties.POWERED)
         && !(Boolean)state.getValue(BlockStateProperties.POWERED)) {
         player.hurt(world.damageSources().generic(), 1.0E12F);
      }
   }
}
