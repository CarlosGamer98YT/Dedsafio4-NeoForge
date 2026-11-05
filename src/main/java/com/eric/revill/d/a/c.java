package com.eric.revill.d.a;

/**
 * Controlador de animaciones y transiciones de interfaz en cliente.
 * 
 * Calcula curvas de interpolación suave (fade-in, fade-out, traslaciones elásticas)
 * empleadas en el despliegue de ventanas modales, diálogos y notificaciones emergentes.
 */

import com.eric.revill.d.e.p;
import com.eric.revill.o.h;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.UUID;
import com.eric.revill.fabric.api.EnvType;
import com.eric.revill.fabric.api.Environment;
import com.eric.revill.fabric.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import com.eric.revill.fabric.fabric.api.client.networking.v1.ClientPlayNetworking;
import com.eric.revill.fabric.fabric.api.client.networking.v1.ClientPlayConnectionEvents.Disconnect;
import com.eric.revill.fabric.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public final class c {
   private static JsonObject a = new JsonObject();

   private c() {
   }

   public static void a() {
      ClientPlayNetworking.registerGlobalReceiver(h.b, (client, handler, buf, responseSender) -> {
         String json = buf.readUtf(32767);
         client.execute(() -> {
            try {
               a = JsonParser.parseString(json).getAsJsonObject();
               if (client.screen instanceof p screen) {
                  screen.a();
               }
            } catch (RuntimeException var4x) {
               if (client.player != null) {
                  client.player.displayClientMessage(Component.literal("No se pudieron leer los datos de misiones recibidos."), false);
               }
            }
         });
      });
      ClientPlayConnectionEvents.DISCONNECT.register((Disconnect)(handler, client) -> a = new JsonObject());
   }

   public static JsonObject b() {
      return a.deepCopy();
   }

   public static void c() {
      ClientPlayNetworking.send(h.a, PacketByteBufs.empty());
   }

   public static void a(JsonObject mission) {
      a(h.c, mission.toString(), 8192);
   }

   public static void a(String id) {
      a(h.d, id, 64);
   }

   public static void a(UUID playerId) {
      a(h.e, playerId.toString(), 36);
   }

   public static void a(UUID playerId, String missionId) {
      FriendlyByteBuf buf = PacketByteBufs.create();
      buf.writeUtf(playerId.toString(), 36);
      buf.writeUtf(missionId, 64);
      ClientPlayNetworking.send(h.f, buf);
   }

   private static void a(ResourceLocation channel, String value, int maxLength) {
      FriendlyByteBuf buf = PacketByteBufs.create();
      buf.writeUtf(value, maxLength);
      ClientPlayNetworking.send(channel, buf);
   }
}
