package com.eric.revill.mixin;

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
