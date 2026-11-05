package com.eric.revill.s;

/**
 * Gestor de globos de texto flotantes (bocadillos de chat) sobre la cabeza de los jugadores.
 * 
 * Divide los mensajes largos en líneas legibles y los sincroniza a los clientes cercanos.
 */

import com.eric.revill.g.b;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import com.eric.revill.fabric.fabric.api.event.lifecycle.v1.ServerTickEvents;
import com.eric.revill.fabric.fabric.api.networking.v1.PacketByteBufs;
import com.eric.revill.fabric.fabric.api.networking.v1.ServerPlayConnectionEvents;
import com.eric.revill.fabric.fabric.api.networking.v1.ServerPlayNetworking;
import com.eric.revill.fabric.fabric.api.networking.v1.ServerPlayConnectionEvents.Disconnect;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public final class a {
   private static final int a = 4;
   private static final int b = 100;
   private static final int c = 20;
   private static final int d = 15;
   private static final Set<UUID> e = new HashSet<>();
   private static final Map<UUID, a.SpeechQueue> f = new HashMap<>();

   private a() {
   }

   public static void a() {
      ServerTickEvents.END_SERVER_TICK.register(com.eric.revill.s.a::a);
      ServerPlayConnectionEvents.DISCONNECT.register((Disconnect)(handler, server) -> {
         UUID playerId = handler.getPlayer().getUUID();
         e.remove(playerId);
         f.remove(playerId);
         a(server, playerId);
      });
   }

   public static boolean a(MinecraftServer server, ServerPlayer target) {
      UUID playerId = target.getUUID();
      if (!e.add(playerId)) {
         f.remove(playerId);
         a(server, playerId);
         return false;
      } else {
         return true;
      }
   }

   public static boolean a(UUID uuid) {
      return e.contains(uuid);
   }

   public static void a(MinecraftServer server, UUID speaker, String message) {
      String formatted = a(message, 15);
      if (!formatted.isEmpty() && e.contains(speaker)) {
         a.SpeechQueue queue = f.computeIfAbsent(speaker, ignored -> new a.SpeechQueue());
         if (queue.b <= 0 && queue.c <= 0) {
            a(server, speaker, queue, formatted);
         } else {
            queue.a.add(formatted);
         }
      }
   }

   private static void a(MinecraftServer server) {
      Iterator<Entry<UUID, a.SpeechQueue>> iterator = f.entrySet().iterator();

      while (iterator.hasNext()) {
         Entry<UUID, a.SpeechQueue> entry = iterator.next();
         a.SpeechQueue queue = entry.getValue();
         if (queue.b > 0) {
            queue.b--;
            if (queue.b == 0 && !queue.a.isEmpty()) {
               queue.c = 20;
            }
         } else if (queue.c > 0) {
            queue.c--;
            String next;
            if (queue.c == 0 && (next = queue.a.poll()) != null && e.contains(entry.getKey())) {
               a(server, entry.getKey(), queue, next);
            }
         }

         if (queue.b == 0 && queue.c == 0 && queue.a.isEmpty()) {
            iterator.remove();
         }
      }
   }

   private static void a(MinecraftServer server, UUID speaker, a.SpeechQueue queue, String message) {
      b(server, speaker, message);
      int wordCount = message.split("\\s+").length;
      queue.b = wordCount * 4 + 100;
   }

   private static void b(MinecraftServer server, UUID speaker, String message) {
      for (ServerPlayer player : server.getPlayerList().getPlayers()) {
         FriendlyByteBuf buf = PacketByteBufs.create();
         buf.writeUtf(speaker.toString());
         buf.writeUtf(message);
         ServerPlayNetworking.send(player, com.eric.revill.g.b.l, buf);
      }
   }

   private static void a(MinecraftServer server, UUID speaker) {
      for (ServerPlayer player : server.getPlayerList().getPlayers()) {
         FriendlyByteBuf buf = PacketByteBufs.create();
         buf.writeUtf(speaker.toString());
         ServerPlayNetworking.send(player, com.eric.revill.g.b.m, buf);
      }
   }

   private static String a(String text, int maxWords) {
      String trimmed = text.trim();
      if (trimmed.isEmpty()) {
         return "";
      } else {
         String[] words = trimmed.split("\\s+");
         if (words.length <= maxWords) {
            return trimmed;
         } else {
            StringBuilder result = new StringBuilder();

            for (int index = 0; index < maxWords; index++) {
               if (index > 0) {
                  result.append(' ');
               }

               result.append(words[index]);
            }

            return result.toString();
         }
      }
   }

   private static final class SpeechQueue {
      private final Queue<String> a = new ArrayDeque<>();
      private int b;
      private int c;
   }
}
