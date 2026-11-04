package com.eric.revill.l;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.Level;

public final class d extends b {
   public d(Properties settings) {
      super(settings, "item.dedsafio-4-mechanics.corazon_rojo.tooltip");
   }

   public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
      if (!world.isClientSide && user instanceof ServerPlayer player) {
         com.eric.revill.q.a holder = (com.eric.revill.q.a)player;
         holder.dedsafio4$setRedHearts(holder.dedsafio4$getRedHearts() + 1);
         g.a(player);
         player.setHealth(Math.min(player.getMaxHealth(), player.getHealth() + 2.0F));
      }

      return super.finishUsingItem(stack, world, user);
   }
}
