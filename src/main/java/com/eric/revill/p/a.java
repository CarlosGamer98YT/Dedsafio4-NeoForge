package com.eric.revill.p;

/**
 * Emisor y difusor de paquetes de red para eventos, ruletas y animaciones visuales.
 */

import com.eric.revill.fabric.fabric.api.networking.v1.PacketByteBufs;
import com.eric.revill.fabric.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public final class a {
   public static final ResourceLocation a = ResourceLocation.fromNamespaceAndPath("revill", "event_state");
   public static final ResourceLocation b = ResourceLocation.fromNamespaceAndPath("revill", "announcement");
   public static final ResourceLocation c = ResourceLocation.fromNamespaceAndPath("revill", "stop_event");
   public static final ResourceLocation d = ResourceLocation.fromNamespaceAndPath("revill", "roulette");
   public static final ResourceLocation e = ResourceLocation.fromNamespaceAndPath("revill", "roulette_announcement");
   public static final ResourceLocation f = ResourceLocation.fromNamespaceAndPath("revill", "nutria_animation");
   public static final ResourceLocation g = ResourceLocation.fromNamespaceAndPath("revill", "nutria_animation_complete");
   public static final ResourceLocation h = ResourceLocation.fromNamespaceAndPath("revill", "revill_intro_death");
   public static final ResourceLocation i = ResourceLocation.fromNamespaceAndPath("revill", "revill_intro_complete");
   public static final ResourceLocation j = ResourceLocation.fromNamespaceAndPath("revill", "dedsafio4_revill_visual_action");
   public static final ResourceLocation k = ResourceLocation.fromNamespaceAndPath("revill", "custom_totem_pop");

   private a() {
   }

   public static void a(MinecraftServer server, boolean active, int remainingSeconds, boolean skyActive, boolean announcementActive) {
      if (server != null) {
         for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            FriendlyByteBuf buf = PacketByteBufs.create();
            buf.writeBoolean(active);
            buf.writeInt(remainingSeconds);
            buf.writeBoolean(skyActive);
            buf.writeBoolean(announcementActive);
            ServerPlayNetworking.send(player, a, buf);
         }
      }
   }

   public static void a(MinecraftServer server, int durationSeconds, double flashSpeed) {
      if (server != null) {
         for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            FriendlyByteBuf buf = PacketByteBufs.create();
            buf.writeInt(durationSeconds);
            buf.writeDouble(flashSpeed);
            ServerPlayNetworking.send(player, b, buf);
         }
      }
   }

   public static void a(MinecraftServer server) {
      if (server != null) {
         for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            FriendlyByteBuf buf = PacketByteBufs.create();
            buf.writeBoolean(false);
            ServerPlayNetworking.send(player, c, buf);
         }
      }
   }

   public static void a(MinecraftServer server, String color, String result) {
      if (server != null) {
         for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            FriendlyByteBuf buf = PacketByteBufs.create();
            buf.writeUtf(color);
            buf.writeUtf(result);
            ServerPlayNetworking.send(player, d, buf);
         }
      }
   }

   public static void b(MinecraftServer server, String color, String message) {
      if (server != null) {
         for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            FriendlyByteBuf buf = PacketByteBufs.create();
            buf.writeUtf(color == null ? "naranja" : color, 32);
            buf.writeUtf(message, 32767);
            ServerPlayNetworking.send(player, e, buf);
         }
      }
   }

   public static void a(MinecraftServer server, String message) {
      b(server, "naranja", message);
   }

   public static void b(MinecraftServer server) {
      if (server != null) {
         for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            ServerPlayNetworking.send(player, f, PacketByteBufs.empty());
         }
      }
   }

   public static void c(MinecraftServer server) {
      if (server != null) {
         for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            ServerPlayNetworking.send(player, h, PacketByteBufs.empty());
         }
      }
   }

   public static void a(MinecraftServer server, int action) {
      if (server != null) {
         for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            FriendlyByteBuf buf = PacketByteBufs.create();
            buf.writeVarInt(action);
            ServerPlayNetworking.send(player, j, buf);
         }
      }
   }
}
