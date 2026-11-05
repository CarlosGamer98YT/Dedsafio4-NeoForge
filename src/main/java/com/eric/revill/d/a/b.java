package com.eric.revill.d.a;

/**
 * Almacén y estado en cliente de la información de Gremios / Hermandades.
 * 
 * Mantiene en caché local el estado del jugador respecto a su gremio (si pertenece a uno,
 * nombre del gremio, color distintivo, lista de miembros y solicitudes de unión pendientes)
 * para alimentar los menús y el renderizado en pantalla sin consultar constantemente al servidor.
 */

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import com.eric.revill.fabric.api.EnvType;
import com.eric.revill.fabric.api.Environment;
import com.eric.revill.fabric.fabric.api.client.networking.v1.ClientPlayNetworking;
import com.eric.revill.fabric.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

@Environment(EnvType.CLIENT)
public final class b {
   private static CompoundTag a = new CompoundTag();
   private static final Map<UUID, b.a> b = new HashMap<>();

   private b() {
   }

   public static void a() {
      ClientPlayNetworking.registerGlobalReceiver(com.eric.revill.k.b.c, (client, handler, buf, responseSender) -> {
         int count = Math.min(buf.readVarInt(), 1000);
         HashMap<UUID, b.a> received = new HashMap<>();

         for (int index = 0; index < count; index++) {
            UUID playerId = UUID.fromString(buf.readUtf(36));
            if (buf.readBoolean()) {
               received.put(playerId, new b.a(buf.readUtf(32), buf.readInt()));
            }
         }

         client.execute(() -> {
            b.clear();
            b.putAll(received);
         });
      });
      ClientPlayNetworking.registerGlobalReceiver(com.eric.revill.k.b.b, (client, handler, buf, responseSender) -> {
         CompoundTag received = buf.readNbt();
         client.execute(() -> a = received == null ? new CompoundTag() : received);
      });
   }

   public static CompoundTag b() {
      return a.copy();
   }

   public static void c() {
      if (ClientPlayNetworking.canSend(com.eric.revill.k.b.a)) {
         ClientPlayNetworking.send(com.eric.revill.k.b.a, PacketByteBufs.empty());
      }
   }

   public static void a(int action, CompoundTag data) {
      if (ClientPlayNetworking.canSend(com.eric.revill.k.b.d)) {
         FriendlyByteBuf buf = PacketByteBufs.create();
         buf.writeVarInt(action);
         buf.writeNbt(data);
         ClientPlayNetworking.send(com.eric.revill.k.b.d, buf);
      }
   }

   public static String a(String playerName) {
      if (!a.getBoolean("hasGuild")) {
         return null;
      } else {
         CompoundTag guild = a.getCompound("guild");

         for (int i2 = 0; i2 < guild.getList("members", 10).size(); i2++) {
            CompoundTag member = guild.getList("members", 10).getCompound(i2);
            if (member.getString("name").equals(playerName)) {
               return guild.getString("name");
            }
         }

         return null;
      }
   }

   public static b.a a(UUID playerId) {
      return b.get(playerId);
   }

   public static int d() {
      return a.getBoolean("hasGuild") ? a.getCompound("guild").getInt("color") : ChatFormatting.WHITE.getColor();
   }

   public static void e() {
      a = new CompoundTag();
      b.clear();
   }

   @Environment(EnvType.CLIENT)
   public static record a(String a, int b) {
   }
}
