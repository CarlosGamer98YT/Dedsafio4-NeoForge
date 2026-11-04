package com.eric.revill.mixin;

import com.eric.revill.d.e.q;
import com.eric.revill.r.a;
import com.eric.revill.fabric.api.EnvType;
import com.eric.revill.fabric.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance.Attenuation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin({MouseHandler.class})
public abstract class UiSoundScreenMixin {
   @Inject(
      method = {"onPress(JIII)V"},
      at = {@At("TAIL")}
   )
   private void revill$playUiSound(long window, int button, int action, int modifiers, CallbackInfo callback) {
      Minecraft client = Minecraft.getInstance();
      if (action == 1 && client.screen != null && q.a(client.screen)) {
         client.getSoundManager()
            .play(new SimpleSoundInstance(a.a, SoundSource.MASTER, 1.0F, 1.0F, RandomSource.create(), false, 0, Attenuation.NONE, 0.0, 0.0, 0.0, true));
      }
   }
}
