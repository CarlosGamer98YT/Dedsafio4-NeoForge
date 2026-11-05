package com.eric.revill.h;

/**
 * Canales y manejadores de red para el Cajero Automático (ATM).
 * 
 * Comunica las acciones de apertura de cajero, depósito de monedas físicas,
 * retiro de saldo en billetes/monedas y sincronización de balances hacia el HUD del cliente.
 */

import com.eric.revill.fabric.fabric.api.networking.v1.PacketByteBufs;
import com.eric.revill.fabric.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public final class d {
   public static final ResourceLocation a = ResourceLocation.fromNamespaceAndPath("dedsafio-4-mechanics", "atm_open");
   public static final ResourceLocation b = ResourceLocation.fromNamespaceAndPath("dedsafio-4-mechanics", "atm_withdraw_open");
   public static final ResourceLocation c = ResourceLocation.fromNamespaceAndPath("dedsafio-4-mechanics", "atm_withdraw_ui");
   public static final ResourceLocation d = ResourceLocation.fromNamespaceAndPath("dedsafio-4-mechanics", "atm_deposit_open");
   public static final ResourceLocation e = ResourceLocation.fromNamespaceAndPath("dedsafio-4-mechanics", "coins_menu_open");
   public static final ResourceLocation f = ResourceLocation.fromNamespaceAndPath("dedsafio-4-mechanics", "coins_action");
   public static final ResourceLocation g = ResourceLocation.fromNamespaceAndPath("dedsafio-4-mechanics", "atm_withdraw");
   public static final ResourceLocation h = ResourceLocation.fromNamespaceAndPath("dedsafio-4-mechanics", "currency_sync");

   private d() {
   }

   public static void a() {
      ServerPlayNetworking.registerGlobalReceiver(
         d, (server, player, handler, buf, responseSender) -> server.execute(() -> player.openMenu(com.eric.revill.h.a.b()))
      );
      ServerPlayNetworking.registerGlobalReceiver(b, (server, player, handler, buf, responseSender) -> server.execute(() -> {
            a(player, com.eric.revill.h.b.a(server, player.getUUID()));
            ServerPlayNetworking.send(player, c, PacketByteBufs.empty());
         }));
      ServerPlayNetworking.registerGlobalReceiver(f, (server, player, handler, buf, responseSender) -> {
         int action = buf.readVarInt();
         int currencyIndex = buf.readVarInt();
         int amount = buf.readVarInt();
         server.execute(() -> a(player, action, currencyIndex, amount));
      });
      ServerPlayNetworking.registerGlobalReceiver(g, (server, player, handler, buf, responseSender) -> {
         int blue = buf.readVarInt();
         int red = buf.readVarInt();
         int green = buf.readVarInt();
         server.execute(() -> b(player, blue, red, green));
      });
   }

   public static void a(ServerPlayer player) {
      ServerPlayNetworking.send(player, a, PacketByteBufs.empty());
   }

   public static void b(ServerPlayer player) {
      ServerPlayNetworking.send(player, e, PacketByteBufs.empty());
   }

   public static void a(ServerPlayer player, int[] balances) {
      FriendlyByteBuf buf = PacketByteBufs.create();

      for (int balance : balances) {
         buf.writeVarInt(balance);
      }

      ServerPlayNetworking.send(player, h, buf);
   }

   private static void a(ServerPlayer player, int action, int currencyIndex, int amount) {
      c[] types = com.eric.revill.h.c.a();
      if (currencyIndex < 0 || currencyIndex >= types.length) {
         currencyIndex = 0;
      }

      c type = types[currencyIndex];
      switch (action) {
         case 0:
            com.eric.revill.h.b.a(player.getServer(), player.getUUID(), type, Math.max(1, amount));
            player.displayClientMessage(Component.literal("Añadidas " + Math.max(1, amount) + " deditas " + type.b() + " a tu cuenta."), false);
            break;
         case 1:
            int removal = Math.max(1, amount);
            if (!com.eric.revill.h.b.b(player.getServer(), player.getUUID(), type, removal)) {
               player.displayClientMessage(Component.literal("No tienes suficientes deditas " + type.b() + " para quitar."), false);
               return;
            }

            player.displayClientMessage(Component.literal("Quitadas " + removal + " deditas " + type.b() + " de tu cuenta."), false);
            break;
         case 2:
            int[] balances = com.eric.revill.h.b.a(player.getServer(), player.getUUID());
            player.displayClientMessage(Component.literal("Tu saldo: Azul " + balances[0] + " | Roja " + balances[1] + " | Verde " + balances[2]), false);
            break;
         default:
            player.displayClientMessage(Component.literal("Acción no válida."), false);
      }

      com.eric.revill.h.b.a(player);
   }

   private static void b(ServerPlayer player, int blue, int red, int green) {
      int[] requested = new int[]{blue, red, green};
      c[] types = com.eric.revill.h.c.a();

      for (int index = 0; index < requested.length; index++) {
         int amount = requested[index];
         if (amount < 0 || amount > com.eric.revill.h.b.a(player.getServer(), player.getUUID(), types[index])) {
            player.displayClientMessage(Component.literal("No tienes suficientes deditas en la cuenta."), true);
            com.eric.revill.h.b.a(player);
            return;
         }
      }

      if (!a(player, requested, types)) {
         player.displayClientMessage(Component.literal("No tienes espacio suficiente en el inventario."), true);
      } else if (!com.eric.revill.h.b.a(player.getServer(), player.getUUID(), requested)) {
         player.displayClientMessage(Component.literal("No tienes suficientes deditas en la cuenta."), true);
         com.eric.revill.h.b.a(player);
      } else {
         for (int var11 = 0; var11 < requested.length; var11++) {
            int amount = requested[var11];
            if (amount != 0) {
               c type = types[var11];

               while (amount > 0) {
                  int stackSize = Math.min(amount, type.c().getDefaultMaxStackSize());
                  player.getInventory().add(new ItemStack(type.c(), stackSize));
                  amount -= stackSize;
               }
            }
         }

         com.eric.revill.h.b.a(player);
      }
   }

   private static boolean a(ServerPlayer player, int[] amounts, c[] types) {
      int emptySlots = 0;

      for (int slot = 0; slot < 36; slot++) {
         ItemStack stack = player.getInventory().getItem(slot);
         if (stack.isEmpty()) {
            emptySlots++;
         }
      }

      int requiredEmptySlots = 0;

      for (int index = 0; index < amounts.length; index++) {
         int remaining = amounts[index];
         Item item = types[index].c();

         for (int slotx = 0; slotx < 36 && remaining > 0; slotx++) {
            ItemStack stack = player.getInventory().getItem(slotx);
            if (stack.is(item)) {
               remaining -= stack.getMaxStackSize() - stack.getCount();
            }
         }

         remaining = Math.max(0, remaining);
         requiredEmptySlots += (remaining + item.getDefaultMaxStackSize() - 1) / item.getDefaultMaxStackSize();
      }

      return requiredEmptySlots <= emptySlots;
   }
}
