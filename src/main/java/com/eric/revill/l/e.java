package com.eric.revill.l;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.Level;

public final class e extends b {
   public e(Properties settings) {
      super(settings, "item.dedsafio-4-mechanics.corazon_limbo.tooltip");
   }

   public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
      if (!world.isClientSide && user instanceof ServerPlayer player) {
         ((com.eric.revill.q.a)player).dedsafio4$setRedHearts(0);
         g.a(player);
         player.setHealth(player.getMaxHealth());
      }

      return super.finishUsingItem(stack, world, user);
   }
}
