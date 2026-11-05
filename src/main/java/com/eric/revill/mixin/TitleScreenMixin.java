package com.eric.revill.mixin;

/**
 * Inyección en el menú principal de Minecraft {@link net.minecraft.client.gui.screens.TitleScreen}.
 * 
 * Renderiza el fondo oficial temático de DEDSAFIO 4 y muestra el botón de la Guía de Administrador
 * y créditos de EricReck.
 */

import com.eric.revill.d.b.l;
import com.eric.revill.d.b.m;
import com.eric.revill.fabric.api.EnvType;
import com.eric.revill.fabric.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin({TitleScreen.class})
public abstract class TitleScreenMixin {
   @Inject(
      method = {"renderPanorama"},
      at = {@At("TAIL")}
   )
   private void dedsafio4$renderCustomTitleBackground(GuiGraphics context, float delta, CallbackInfo ci) {
      l.a(context);
   }

   @Inject(
      method = {"render(Lnet/minecraft/client/gui/GuiGraphics;IIF)V"},
      at = {@At("TAIL")}
   )
   private void dedsafio4$renderTitleCredit(GuiGraphics context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
      m.a(context);
   }

   @Inject(
      method = {"mouseClicked(DDI)Z"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void dedsafio4$openAuthorProfile(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
      TitleScreen screen = (TitleScreen)(Object)this;
      if (button == 0 && m.a(mouseX, mouseY, screen.width, screen.height)) {
         cir.setReturnValue(true);
      }
   }
}
