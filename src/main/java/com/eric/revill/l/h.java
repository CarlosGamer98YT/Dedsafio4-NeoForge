package com.eric.revill.l;

/**
 * Ítem de la Mochila Portátil (Ender Bag).
 * 
 * Al hacer clic derecho, abre una interfaz de almacenamiento portátil de 27 casillas
 * que viaja con el jugador dentro del ítem.
 */

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.Level;

public final class h extends b {
   private final boolean a;

   public h(boolean enderBag, Properties settings) {
      super(settings, enderBag ? "item.dedsafio-4-mechanics.ender_bag.tooltip" : "item.dedsafio-4-mechanics.bolsa_primitiva.tooltip");
      this.a = enderBag;
   }

   public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
      ItemStack stack = user.getItemInHand(hand);
      if (!world.isClientSide && user instanceof ServerPlayer player) {
         player.openMenu(this.a ? com.eric.revill.a.a.a(player) : com.eric.revill.a.a.a(stack));
      }

      return InteractionResultHolder.sidedSuccess(stack, world.isClientSide);
   }
}
