package com.eric.revill.d.a;

/**
 * Gestor de efectos de audio y reproducción de sonidos en el cliente.
 * 
 * Controla la reproducción de efectos sonoros personalizados como el tintineo de monedas
 * en el cajero automático, la alarma de tensión durante el Momento Revill, el giro de la ruleta
 * y los efectos de confirmación en las interfaces de usuario.
 */

import com.eric.revill.d.e.i;
import com.eric.revill.fabric.api.EnvType;
import com.eric.revill.fabric.api.Environment;
import com.eric.revill.fabric.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import com.eric.revill.fabric.fabric.api.client.networking.v1.ClientPlayNetworking;
import com.eric.revill.fabric.fabric.api.client.networking.v1.ClientPlayConnectionEvents.Disconnect;
import com.eric.revill.fabric.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.FriendlyByteBuf;

@Environment(EnvType.CLIENT)
public final class a {
   private a() {
   }

   public static void a() {
      ClientPlayNetworking.registerGlobalReceiver(
         com.eric.revill.h.d.a, (client, handler, buf, responseSender) -> client.execute(() -> client.setScreen(new com.eric.revill.d.e.c()))
      );
      ClientPlayNetworking.registerGlobalReceiver(
         com.eric.revill.h.d.e, (client, handler, buf, responseSender) -> client.execute(() -> client.setScreen(new i()))
      );
      ClientPlayNetworking.registerGlobalReceiver(
         com.eric.revill.h.d.c, (client, handler, buf, responseSender) -> client.execute(() -> client.setScreen(new com.eric.revill.d.e.d()))
      );
      ClientPlayNetworking.registerGlobalReceiver(com.eric.revill.h.d.h, (client, handler, buf, responseSender) -> {
         int blue = buf.readVarInt();
         int red = buf.readVarInt();
         int green = buf.readVarInt();
         client.execute(() -> com.eric.revill.d.b.d.a(blue, red, green));
      });
      ClientPlayConnectionEvents.DISCONNECT.register((Disconnect)(handler, client) -> com.eric.revill.d.b.d.a(0, 0, 0));
   }

   public static void a(int blue, int red, int green) {
      FriendlyByteBuf buf = PacketByteBufs.create();
      buf.writeVarInt(blue);
      buf.writeVarInt(red);
      buf.writeVarInt(green);
      ClientPlayNetworking.send(com.eric.revill.h.d.g, buf);
   }
}
