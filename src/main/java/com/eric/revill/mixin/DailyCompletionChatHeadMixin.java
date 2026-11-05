package com.eric.revill.mixin;

/**
 * Inyección en el componente de chat del cliente {@link net.minecraft.client.gui.components.ChatComponent}.
 * 
 * Dibuja la cabeza del jugador (avatar facial) al lado de los mensajes de chat
 * y de los avisos de misiones diarias completadas.
 */

import com.mojang.blaze3d.systems.RenderSystem;
import com.eric.revill.fabric.api.EnvType;
import com.eric.revill.fabric.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Environment(EnvType.CLIENT)
@Mixin({ChatComponent.class})
public abstract class DailyCompletionChatHeadMixin {
   @Redirect(
      method = {"render(Lnet/minecraft/client/gui/GuiGraphics;IIIZ)V"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/util/FormattedCharSequence;III)I"
      )
   )
   private int dedsafio4$drawDailyCompletionFace(GuiGraphics context, Font textRenderer, FormattedCharSequence text, int x, int y, int color) {
      int result = context.drawString(textRenderer, text, x, y, color);
      String message = getPlainText(text);
      ResourceLocation skin = getDailyCompletionSkin(message);
      if (skin != null) {
         float alpha = (float)(color >>> 24) / 255.0F;
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
         context.blit(skin, x, y, 8.0F, 8.0F, 8, 8, 64, 64);
         context.blit(skin, x, y, 40.0F, 8.0F, 8, 8, 64, 64);
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      }

      return result;
   }

   private static String getPlainText(FormattedCharSequence text) {
      StringBuilder result = new StringBuilder();
      text.accept((index, style, codePoint) -> {
         result.appendCodePoint(codePoint);
         return true;
      });
      return result.toString();
   }

   private static ResourceLocation getDailyCompletionSkin(String message) {
      String prefix = "☺ ";
      String suffix = " ha completado la misión diaria.";
      if (!message.startsWith(prefix)) {
         return null;
      } else {
         int nameEnd = message.indexOf(suffix, prefix.length());
         if (nameEnd <= prefix.length()) {
            return null;
         } else {
            String playerName = message.substring(prefix.length(), nameEnd);
            Minecraft client = Minecraft.getInstance();
            if (client.getConnection() == null) {
               return null;
            } else {
               for (PlayerInfo entry : client.getConnection().getOnlinePlayers()) {
                  if (entry.getProfile().getName().equals(playerName)) {
                     return entry.getSkin().texture();
                  }
               }

               return null;
            }
         }
      }
   }
}
