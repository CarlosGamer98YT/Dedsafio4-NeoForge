package com.eric.revill.o;

/**
 * Canales de red para la interacción y entrega de misiones en el Pedestal.
 */

import com.eric.revill.fabric.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;

public final class c {
   public static final ResourceLocation a = a("daily_pedestal_save_day");

   private c() {
   }

   private static ResourceLocation a(String path) {
      return ResourceLocation.fromNamespaceAndPath("dedsafio-4-mechanics", path);
   }

   public static void a() {
      ServerPlayNetworking.registerGlobalReceiver(a, (server, player, handler, buf, responseSender) -> {
         BlockPos pos = buf.readBlockPos();
         int day = buf.readVarInt();
         server.execute(() -> a(player, pos, day));
      });
   }

   private static void a(ServerPlayer player, BlockPos pos, int day) {
      BlockEntity blockEntity;
      if (player.isCreative() && player.hasPermissions(4) && !(player.distanceToSqr(pos.getCenter()) > 64.0) && (blockEntity = player.level().getBlockEntity(pos)) instanceof b) {
         b pedestal = (b)blockEntity;
         AbstractContainerMenu currentHandler = player.containerMenu;
         if (currentHandler instanceof d && ((d)currentHandler).a().equals(pos)) {
            if (day >= 1 && day <= 99) {
               pedestal.a(day);
               player.displayClientMessage(Component.translatable("block.dedsafio-4-mechanics.daily_pedestal.saved", new Object[]{day}), false);
               return;
            }

            player.displayClientMessage(Component.translatable("block.dedsafio-4-mechanics.daily_pedestal.invalid_config"), false);
            return;
         }
      }

      player.displayClientMessage(Component.translatable("block.dedsafio-4-mechanics.daily_pedestal.edit_denied"), false);
   }
}
