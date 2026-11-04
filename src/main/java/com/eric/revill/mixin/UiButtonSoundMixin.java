package com.eric.revill.mixin;

import com.eric.revill.d.e.q;
import com.eric.revill.fabric.api.EnvType;
import com.eric.revill.fabric.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.sounds.SoundManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin({AbstractWidget.class})
public abstract class UiButtonSoundMixin {
   @Inject(
      method = {"playDownSound(Lnet/minecraft/client/sounds/SoundManager;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void revill$replaceUiButtonSound(SoundManager soundManager, CallbackInfo callback) {
      Minecraft client = Minecraft.getInstance();
      if (client.screen != null && q.a(client.screen)) {
         callback.cancel();
      }
   }
}
