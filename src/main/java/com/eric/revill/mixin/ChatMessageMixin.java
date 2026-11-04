package com.eric.revill.mixin;

import com.eric.revill.s.a;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.network.chat.ChatType.Bound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({PlayerList.class})
public abstract class ChatMessageMixin {
   @Inject(
      method = {"broadcastChatMessage(Lnet/minecraft/network/chat/PlayerChatMessage;Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/network/chat/ChatType$Bound;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void eon(PlayerChatMessage message, ServerPlayer sender, Bound params, CallbackInfo ci) {
      if (sender != null) {
         if (a.a(sender.getUUID())) {
            ci.cancel();
            String text = message.signedContent();
            MinecraftServer server = sender.getServer();
            if (server != null && text != null && !text.isBlank()) {
               server.execute(() -> a.a(server, sender.getUUID(), text));
            }
         }
      }
   }
}
