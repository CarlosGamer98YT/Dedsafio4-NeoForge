package com.eric.revill.k;

/**
 * Canales de red para el sistema de Gremios / Clanes.
 * 
 * Maneja las peticiones de creación, invitaciones y sincronización de gremios entre cliente y servidor.
 */

import com.eric.revill.fabric.fabric.api.networking.v1.PacketByteBufs;
import com.eric.revill.fabric.fabric.api.networking.v1.ServerPlayConnectionEvents;
import com.eric.revill.fabric.fabric.api.networking.v1.ServerPlayNetworking;
import com.eric.revill.fabric.fabric.api.networking.v1.ServerPlayConnectionEvents.Disconnect;
import com.eric.revill.fabric.fabric.api.networking.v1.ServerPlayConnectionEvents.Join;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public final class b {
   public static final ResourceLocation a = a("guild_request");
   public static final ResourceLocation b = a("guild_sync");
   public static final ResourceLocation c = a("guild_nametags_sync");
   public static final ResourceLocation d = a("guild_action");

   private b() {
   }

   private static ResourceLocation a(String path) {
      return ResourceLocation.fromNamespaceAndPath("dedsafio-4-mechanics", path);
   }

   public static void a() {
      ServerPlayConnectionEvents.JOIN.register((Join)(handler, sender, server) -> com.eric.revill.k.a.a(handler.getPlayer()));
      ServerPlayConnectionEvents.DISCONNECT.register((Disconnect)(handler, server) -> server.execute(() -> com.eric.revill.k.a.b(server)));
      ServerPlayNetworking.registerGlobalReceiver(a, (server, player, handler, buf, responseSender) -> server.execute(() -> com.eric.revill.k.a.b(player)));
      ServerPlayNetworking.registerGlobalReceiver(d, (server, player, handler, buf, responseSender) -> {
         if (buf.readableBytes() > 16384) {
            player.displayClientMessage(Component.literal("La acción de hermandad excede el tamaño permitido."), false);
         } else {
            try {
               int action = buf.readVarInt();
               CompoundTag data = buf.readNbt();
               if (data == null || buf.isReadable()) {
                  player.displayClientMessage(Component.literal("La solicitud de hermandad no es válida."), false);
                  return;
               }

               server.execute(() -> com.eric.revill.k.a.a(player, action, data));
            } catch (RuntimeException var7) {
               player.displayClientMessage(Component.literal("La solicitud de hermandad no es válida."), false);
            }
         }
      });
   }

   static void a(ServerPlayer player, CompoundTag snapshot) {
      FriendlyByteBuf buf = PacketByteBufs.create();
      buf.writeNbt(snapshot);
      ServerPlayNetworking.send(player, b, buf);
   }
}
