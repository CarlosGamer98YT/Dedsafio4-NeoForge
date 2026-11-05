package com.eric.revill.l;

/**
 * Ítem Corazón Dorado.
 * 
 * Al consumirse, otorga corazones adicionales de absorción temporal durante 3 minutos (3600 ticks).
 */

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.Level;

public final class c extends b {
   private static final int a = 3600;
   private static final int b = 4;

   public c(Properties settings) {
      super(settings, "item.dedsafio-4-mechanics.corazon_dorado.tooltip");
   }

   public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
      if (!world.isClientSide) {
         user.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 3600, 4));
      }

      return super.finishUsingItem(stack, world, user);
   }
}
