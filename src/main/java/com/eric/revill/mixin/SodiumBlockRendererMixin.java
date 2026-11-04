package com.eric.revill.mixin;

import com.eric.revill.RevillClient;
import com.eric.revill.fabric.api.EnvType;
import com.eric.revill.fabric.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Pseudo
@Mixin(
   targets = {"me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderer"},
   remap = false
)
public abstract class SodiumBlockRendererMixin {
   @Inject(
      method = {"getVertexColors"},
      at = {@At("RETURN")}
   )
   private void dedsafio4$blackSodiumBlockTextures(
      @Coerce Object context, @Coerce Object colorProvider, @Coerce Object quad, CallbackInfoReturnable<int[]> callbackInfo
   ) {
      if (RevillClient.d.c()) {
         int[] colors = (int[])callbackInfo.getReturnValue();
         if (colors != null) {
            int index = 0;

            while (index < colors.length) {
               int n2 = index++;
               colors[n2] &= -16777216;
            }
         }
      }
   }
}
