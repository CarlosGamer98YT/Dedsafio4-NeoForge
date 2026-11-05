package com.eric.revill.c;

import com.eric.revill.fabric.fabric.api.networking.v1.ServerPlayConnectionEvents;
import com.eric.revill.fabric.fabric.api.networking.v1.ServerPlayConnectionEvents.Disconnect;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

/**
 * Notificador de desconexión de jugadores para DEDSAFIO 4.
 * 
 * Cuando un jugador se desconecta del servidor, envía un mensaje con formato
 * especial e icono de búnker al resto de jugadores conectados.
 */
public final class b {
   private b() {
   }

   /**
    * Registra el callback de desconexión del jugador en el servidor.
    */
   public static void a() {
      ServerPlayConnectionEvents.DISCONNECT.register((Disconnect)(handler, server) -> {
         ServerPlayer disconnected = handler.getPlayer();
         Component message = a.a(Component.literal(disconnected.getName().getString() + " se ha desconectado.").withStyle(ChatFormatting.GRAY), a.Icon.b);

         for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            if (!player.getUUID().equals(disconnected.getUUID())) {
               player.displayClientMessage(message, false);
            }
         }
      });
   }
}
