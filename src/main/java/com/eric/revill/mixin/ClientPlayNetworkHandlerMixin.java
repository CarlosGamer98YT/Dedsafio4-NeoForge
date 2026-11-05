package com.eric.revill.mixin;

/**
 * Inyección en el manejador de paquetes de red del cliente {@link net.minecraft.client.multiplayer.ClientPacketListener}.
 * 
 * Procesa la recepción de paquetes de estado del mod, apertura forzada de pantallas de cinemática
 * y sincronización de balances bancarios de Dedita.
 */

import com.eric.revill.RevillClient;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ClientPacketListener.class})
public abstract class ClientPlayNetworkHandlerMixin {
   @Redirect(
      method = {"handleEntityEvent"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/GameRenderer;displayItemActivation(Lnet/minecraft/world/item/ItemStack;)V"
      )
   )
   private void dedsafio4$showUsedTotem(GameRenderer gameRenderer, ItemStack vanillaTotem) {
      ItemStack customTotem = RevillClient.consumeCustomTotemPop();
      gameRenderer.displayItemActivation(customTotem.isEmpty() ? vanillaTotem : customTotem);
   }
}
