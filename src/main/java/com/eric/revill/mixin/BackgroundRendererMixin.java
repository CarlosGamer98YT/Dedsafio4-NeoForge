package com.eric.revill.mixin;

/**
 * Inyección en el renderizador de niebla {@link net.minecraft.client.renderer.FogRenderer}.
 * 
 * Modifica el color, densidad y distancia de la niebla durante el "MOMENTO REVILL"
 * para teñir la atmósfera de un rojo carmesí apocalíptico y reducir la visibilidad.
 */

import com.eric.revill.RevillClient;
import com.mojang.blaze3d.systems.RenderSystem;
import com.eric.revill.fabric.api.EnvType;
import com.eric.revill.fabric.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.FogRenderer.FogMode;
import net.minecraft.world.level.material.FogType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin({FogRenderer.class})
public abstract class BackgroundRendererMixin {
   @Inject(
      method = {"setupFog(Lnet/minecraft/client/Camera;Lnet/minecraft/client/renderer/FogRenderer$FogMode;FZF)V"},
      at = {@At("RETURN")}
   )
   private static void dedsafio4$applyRedFog(Camera camera, FogMode fogType, float viewDistance, boolean thickFog, float tickDelta, CallbackInfo ci) {
      if (RevillClient.d.c()) {
         float[] fog = RenderSystem.getShaderFogColor();
         RenderSystem.setShaderFogColor(0.0F, 0.0F, 0.0F, fog[3]);
      } else if (RevillClient.a.b()) {
         if (camera.getFluidInCamera() == FogType.NONE) {
            float t = RevillClient.a.c();
            float[] fog = RenderSystem.getShaderFogColor();
            float r = fog[0] + (0.92F - fog[0]) * t;
            float g2 = fog[1] + (0.018F - fog[1]) * t;
            float b2 = fog[2] + (0.025F - fog[2]) * t;
            RenderSystem.setShaderFogColor(r, g2, b2, fog[3]);
         }
      }
   }
}
