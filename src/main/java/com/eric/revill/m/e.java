package com.eric.revill.m;

/**
 * Canales de red para el envío, recepción y reclamo de cartas del Buzón.
 */

import java.util.ArrayList;
import java.util.List;
import com.eric.revill.fabric.fabric.api.networking.v1.PacketByteBufs;
import com.eric.revill.fabric.fabric.api.networking.v1.ServerPlayConnectionEvents;
import com.eric.revill.fabric.fabric.api.networking.v1.ServerPlayNetworking;
import com.eric.revill.fabric.fabric.api.networking.v1.ServerPlayConnectionEvents.Join;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public final class e {
   public static final ResourceLocation a = ResourceLocation.fromNamespaceAndPath("dedsafio-4-mechanics", "mail_compose_open");
   public static final ResourceLocation b = ResourceLocation.fromNamespaceAndPath("dedsafio-4-mechanics", "mail_return_inbox");
   public static final ResourceLocation c = ResourceLocation.fromNamespaceAndPath("dedsafio-4-mechanics", "mail_send");
   public static final ResourceLocation d = ResourceLocation.fromNamespaceAndPath("dedsafio-4-mechanics", "mail_mark_read");
   public static final ResourceLocation e = ResourceLocation.fromNamespaceAndPath("dedsafio-4-mechanics", "mail_claim_items");
   public static final ResourceLocation f = ResourceLocation.fromNamespaceAndPath("dedsafio-4-mechanics", "mail_delete");
   public static final ResourceLocation g = ResourceLocation.fromNamespaceAndPath("dedsafio-4-mechanics", "mail_inbox_sync");
   public static final ResourceLocation h = ResourceLocation.fromNamespaceAndPath("dedsafio-4-mechanics", "mail_unread_count_sync");

   private e() {
   }

   public static void a() {
      ServerPlayConnectionEvents.JOIN.register((Join)(handler, sender, server) -> a(handler.getPlayer()));
      ServerPlayNetworking.registerGlobalReceiver(
         a, (server, player, handler, buf, responseSender) -> server.execute(() -> player.openMenu(com.eric.revill.m.b.a()))
      );
      ServerPlayNetworking.registerGlobalReceiver(b, (server, player, handler, buf, responseSender) -> server.execute(() -> {
            if (player.containerMenu instanceof b) {
               player.openMenu(com.eric.revill.m.c.a());
            }
         }));
      ServerPlayNetworking.registerGlobalReceiver(c, (server, player, handler, buf, responseSender) -> {
         String recipientName = buf.readUtf(64);
         String message = buf.readUtf(512);
         server.execute(() -> a(player, recipientName, message));
      });
      ServerPlayNetworking.registerGlobalReceiver(d, (server, player, handler, buf, responseSender) -> {
         int syncId = buf.readVarInt();
         int index = buf.readVarInt();
         server.execute(() -> a(player, syncId, index, false));
      });
      ServerPlayNetworking.registerGlobalReceiver(e, (server, player, handler, buf, responseSender) -> {
         int syncId = buf.readVarInt();
         int index = buf.readVarInt();
         server.execute(() -> a(player, syncId, index, true));
      });
      ServerPlayNetworking.registerGlobalReceiver(f, (server, player, handler, buf, responseSender) -> {
         int syncId = buf.readVarInt();
         int index = buf.readVarInt();
         server.execute(() -> a(player, syncId, index));
      });
   }

   private static HolderLookup.Provider getRegistries() {
      MinecraftServer server = net.neoforged.neoforge.server.ServerLifecycleHooks.getCurrentServer();
      if (server != null) {
         return server.registryAccess();
      }
      try {
         if (net.minecraft.client.Minecraft.getInstance().level != null) {
            return net.minecraft.client.Minecraft.getInstance().level.registryAccess();
         }
      } catch (Throwable ignored) {
      }
      return null;
   }

   private static void writeItem(FriendlyByteBuf buf, ItemStack stack) {
      HolderLookup.Provider registries = getRegistries();
      if (stack == null || stack.isEmpty() || registries == null) {
         buf.writeBoolean(false);
      } else {
         buf.writeBoolean(true);
         buf.writeNbt((CompoundTag)stack.save(registries));
      }
   }

   private static ItemStack readItem(FriendlyByteBuf buf) {
      if (!buf.readBoolean()) {
         return ItemStack.EMPTY;
      }
      CompoundTag tag = buf.readNbt();
      HolderLookup.Provider registries = getRegistries();
      return tag != null && registries != null ? ItemStack.parseOptional(registries, tag) : ItemStack.EMPTY;
   }

   public static void a(FriendlyByteBuf buf, List<a> mails) {
      int count = Math.min(mails.size(), 100);
      buf.writeVarInt(count);

      for (int index = 0; index < count; index++) {
         a mail = mails.get(index);
         buf.writeUtf(mail.b(), 64);
         buf.writeUtf(mail.d(), 512);
         buf.writeBoolean(mail.f());
         int itemCount = Math.min(mail.e().size(), 21);
         buf.writeVarInt(itemCount);

         for (int itemIndex = 0; itemIndex < itemCount; itemIndex++) {
            writeItem(buf, mail.e().get(itemIndex));
         }
      }
   }

   public static List<c.a> a(FriendlyByteBuf buf) {
      int count = Math.min(buf.readVarInt(), 100);
      ArrayList<c.a> mails = new ArrayList<>(count);

      for (int index = 0; index < count; index++) {
         String sender = buf.readUtf(64);
         String message = buf.readUtf(512);
         boolean read = buf.readBoolean();
         int itemCount = Math.min(buf.readVarInt(), 21);
         ArrayList<ItemStack> items = new ArrayList<>(itemCount);

         for (int itemIndex = 0; itemIndex < itemCount; itemIndex++) {
            items.add(readItem(buf));
         }

         mails.add(new c.a(sender, message, read, items));
      }

      return mails;
   }

   public static void a(ServerPlayer player, c handler) {
      FriendlyByteBuf buf = PacketByteBufs.create();
      buf.writeVarInt(handler.containerId);
      a(buf, com.eric.revill.m.d.a.d(player.getUUID()));
      ServerPlayNetworking.send(player, g, buf);
   }

   private static void a(ServerPlayer sender, String recipientName, String message) {
      if (sender.containerMenu instanceof b composeHandler) {
         ServerPlayer recipient = sender.getServer().getPlayerList().getPlayerByName(recipientName.trim());
         if (recipient != null && !message.isBlank()) {
            ArrayList<ItemStack> attachments = new ArrayList<>();

            for (int slot = 0; slot < composeHandler.c().getContainerSize(); slot++) {
               ItemStack stack = composeHandler.c().getItem(slot);
               if (!stack.isEmpty()) {
                  attachments.add(stack.copy());
               }
            }

            com.eric.revill.m.d.a.a(recipient.getUUID(), sender.getUUID(), sender.getName().getString(), "Correo", message, attachments);
            composeHandler.d();
            com.eric.revill.m.d.a.a(recipient, sender.getName().getString(), message);
            a(recipient);
            sender.displayClientMessage(Component.literal("Correo enviado."), true);
            sender.openMenu(com.eric.revill.m.c.a());
         } else {
            sender.displayClientMessage(Component.literal(recipient == null ? "Destinatario no conectado." : "El mensaje no puede estar vacío."), true);
         }
      }
   }

   private static void a(ServerPlayer player, int syncId, int index, boolean claim) {
      if (player.containerMenu instanceof c inbox && inbox.containerId == syncId) {
         if (claim) {
            com.eric.revill.m.d.a.a(player.getUUID(), index, player);
         } else {
            com.eric.revill.m.d.a.a(player.getUUID(), index);
         }

         a(player);
         a(player, inbox);
         return;
      }
   }

   private static void a(ServerPlayer player, int syncId, int index) {
      if (player.containerMenu instanceof c inbox && inbox.containerId == syncId) {
         com.eric.revill.m.d.a.b(player.getUUID(), index);
         a(player);
         player.openMenu(com.eric.revill.m.c.a());
         return;
      }
   }

   public static void a(ServerPlayer player) {
      FriendlyByteBuf buf = PacketByteBufs.create();
      buf.writeVarInt(com.eric.revill.m.d.a.b(player.getUUID()));
      ServerPlayNetworking.send(player, h, buf);
   }
}
