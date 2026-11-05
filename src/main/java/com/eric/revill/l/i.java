package com.eric.revill.l;

/**
 * Utilidades complementarias para el manejo de ítems e inventarios.
 */

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public final class i {
   private i() {
   }

   public static void a(ServerPlayer player, ItemStack totem) {
      MutableComponent message = player.getName()
         .copy()
         .withStyle(ChatFormatting.BOLD)
         .append(Component.literal(" ha activado ").withStyle(ChatFormatting.YELLOW))
         .append(totem.getHoverName().copy().withStyle(ChatFormatting.YELLOW))
         .append(Component.literal(".").withStyle(ChatFormatting.YELLOW));

      for (ServerPlayer viewer : player.getServer().getPlayerList().getPlayers()) {
         viewer.displayClientMessage(message, false);
      }
   }
}
