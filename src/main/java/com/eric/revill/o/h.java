package com.eric.revill.o;

/**
 * Canales de red para la sincronización de misiones diarias hacia el cliente.
 */

import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.UUID;
import com.eric.revill.fabric.fabric.api.networking.v1.PacketByteBufs;
import com.eric.revill.fabric.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public final class h {
   public static final ResourceLocation a = a("mission_sync_request");
   public static final ResourceLocation b = a("mission_sync");
   public static final ResourceLocation c = a("mission_upsert");
   public static final ResourceLocation d = a("mission_delete");
   public static final ResourceLocation e = a("mission_reset_player");
   public static final ResourceLocation f = a("mission_reset_single");
   private static final int g = 32767;

   private h() {
   }

   private static ResourceLocation a(String path) {
      return ResourceLocation.fromNamespaceAndPath("dedsafio-4-mechanics", path);
   }

   public static void a() {
      ServerPlayNetworking.registerGlobalReceiver(a, (server, player, handler, buf, responseSender) -> server.execute(() -> com.eric.revill.o.g.a(player)));
      ServerPlayNetworking.registerGlobalReceiver(c, (server, player, handler, buf, responseSender) -> {
         String json = a(buf, 8192);
         server.execute(() -> a(server, player, json));
      });
      ServerPlayNetworking.registerGlobalReceiver(d, (server, player, handler, buf, responseSender) -> {
         String missionId = a(buf, 64);
         server.execute(() -> b(server, player, missionId));
      });
      ServerPlayNetworking.registerGlobalReceiver(e, (server, player, handler, buf, responseSender) -> {
         String uuid = a(buf, 36);
         server.execute(() -> c(server, player, uuid));
      });
      ServerPlayNetworking.registerGlobalReceiver(f, (server, player, handler, buf, responseSender) -> {
         String uuid = a(buf, 36);
         String missionId = a(buf, 64);
         server.execute(() -> a(server, player, uuid, missionId));
      });
   }

   public static void a(ServerPlayer player) {
      FriendlyByteBuf buf = PacketByteBufs.create();
      String json = com.eric.revill.o.g.b(player).toString();
      if (json.length() > 32767) {
         player.displayClientMessage(Component.literal("No se pudo sincronizar la lista de misiones: supera el límite de datos."), false);
      } else {
         buf.writeUtf(json, 32767);
         ServerPlayNetworking.send(player, b, buf);
      }
   }

   private static String a(FriendlyByteBuf buf, int limit) {
      if ((long)buf.readableBytes() > (long)limit * 4L) {
         buf.skipBytes(buf.readableBytes());
         return "";
      } else {
         try {
            return buf.readUtf(limit);
         } catch (RuntimeException var3) {
            buf.skipBytes(buf.readableBytes());
            return "";
         }
      }
   }

   private static void a(MinecraftServer server, ServerPlayer player, String json) {
      if (b(player)) {
         try {
            f replacement = com.eric.revill.o.g.a(JsonParser.parseString(json).getAsJsonObject());
            ArrayList<f> missions = new ArrayList<>(com.eric.revill.o.g.a());
            int index = -1;

            for (int i2 = 0; i2 < missions.size(); i2++) {
               if (missions.get(i2).a().equals(replacement.a())) {
                  index = i2;
                  break;
               }
            }

            if (index < 0) {
               missions.add(replacement);
            } else {
               missions.set(index, replacement);
            }

            com.eric.revill.o.g.a(server, missions);
            player.displayClientMessage(Component.literal("Misión guardada y sincronizada."), false);
         } catch (RuntimeException var7) {
            player.displayClientMessage(Component.literal("No se pudo guardar la misión: " + var7.getMessage()), false);
         }
      }
   }

   private static void b(MinecraftServer server, ServerPlayer player, String missionId) {
      if (b(player)) {
         try {
            com.eric.revill.o.g.a(server, missionId);
            player.displayClientMessage(Component.literal("Misión eliminada."), false);
         } catch (RuntimeException var4) {
            player.displayClientMessage(Component.literal("No se pudo eliminar la misión: " + var4.getMessage()), false);
         }
      }
   }

   private static void c(MinecraftServer server, ServerPlayer player, String rawUuid) {
      if (b(player)) {
         try {
            UUID uuid = UUID.fromString(rawUuid);
            if (!com.eric.revill.o.g.b(server, uuid)) {
               player.displayClientMessage(Component.literal("No existe progreso registrado para ese jugador."), false);
               return;
            }

            com.eric.revill.o.g.a(player);
            player.displayClientMessage(Component.literal("Progreso del jugador reiniciado."), false);
         } catch (IllegalArgumentException var4) {
            player.displayClientMessage(Component.literal("El identificador de jugador no es válido."), false);
         }
      }
   }

   private static void a(MinecraftServer server, ServerPlayer player, String rawUuid, String missionId) {
      if (b(player)) {
         try {
            UUID uuid = UUID.fromString(rawUuid);
            if (!com.eric.revill.o.g.c(server, uuid, missionId)) {
               player.displayClientMessage(Component.literal("El jugador no tiene progreso en esa misión, o la misión no existe."), false);
               return;
            }

            com.eric.revill.o.g.a(player);
            player.displayClientMessage(Component.literal("Progreso de la misión reiniciado."), false);
         } catch (IllegalArgumentException var5) {
            player.displayClientMessage(Component.literal("El identificador de jugador no es válido."), false);
         }
      }
   }

   private static boolean b(ServerPlayer player) {
      if (player.hasPermissions(4)) {
         return true;
      } else {
         player.displayClientMessage(Component.literal("Necesitas ser operador de nivel 4 o superior."), false);
         return false;
      }
   }
}
