package com.eric.revill.mixin;

/**
 * Inyección en la gestión de conexión de jugadores {@link net.minecraft.server.players.PlayerList}.
 * 
 * Emite notificaciones informativas de desconexión incluyendo las últimas coordenadas conocidas
 * del jugador en el servidor para evitar pérdidas o muertes desatendidas.
 */

import com.eric.revill.c.a;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin({PlayerList.class})
public abstract class PlayerManagerJoinMessageMixin {
   @ModifyArg(
      method = {"placeNewPlayer"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/server/players/PlayerList;broadcastSystemMessage(Lnet/minecraft/network/chat/Component;Z)V"
      ),
      index = 0
   )
   private Component dedsafio4$formatJoinMessage(Component message) {
      return a.a(message.copy().withStyle(ChatFormatting.GREEN), a.Icon.a);
   }
}
