package com.eric.revill.l;

import com.eric.revill.fabric.fabric.api.networking.v1.PacketByteBufs;
import com.eric.revill.fabric.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;

public final class AdminGuideItem extends Item {
   public AdminGuideItem(Properties settings) {
      super(settings);
   }

   public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
      ItemStack stack = user.getItemInHand(hand);
      if (!world.isClientSide && user instanceof ServerPlayer player) {
         if (!player.hasPermissions(4)) {
            player.displayClientMessage(Component.literal("Necesitas ser operador de nivel 4 para abrir esta guía."), false);
            return InteractionResultHolder.fail(stack);
         }

         ServerPlayNetworking.send(player, com.eric.revill.g.b.o, PacketByteBufs.empty());
      }

      return InteractionResultHolder.sidedSuccess(stack, world.isClientSide);
   }

   public static void grantOnLogin(ServerPlayer player) {
      if (player.hasPermissions(4)) {
         for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++) {
            if (player.getInventory().getItem(slot).is(g.t.get())) {
               return;
            }
         }

         ItemStack guide = new ItemStack((ItemLike)g.t.get());
         if (!player.getInventory().add(guide) && !guide.isEmpty()) {
            player.drop(guide, false);
         }
      }
   }
}
