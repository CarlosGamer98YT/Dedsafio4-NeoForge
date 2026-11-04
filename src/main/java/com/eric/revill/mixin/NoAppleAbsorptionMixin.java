package com.eric.revill.mixin;

import com.eric.revill.g.a;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Player.class})
public abstract class NoAppleAbsorptionMixin {
   @Inject(
      method = {"eat(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/food/FoodProperties;)Lnet/minecraft/world/item/ItemStack;"},
      at = {@At("RETURN")}
   )
   private void dedsafio4$removeAppleAbsorption(Level world, ItemStack stack, FoodProperties foodProperties, CallbackInfoReturnable<ItemStack> cir) {
      if (a.a("dia7_sin_absorcion") && (stack.is(Items.GOLDEN_APPLE) || stack.is(Items.ENCHANTED_GOLDEN_APPLE))) {
         ((Player)(Object)this).removeEffect(MobEffects.ABSORPTION);
      }
   }
}
