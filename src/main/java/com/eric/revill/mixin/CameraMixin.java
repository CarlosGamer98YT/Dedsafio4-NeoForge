package com.eric.revill.mixin;

/**
 * Inyección en la cámara del jugador {@link net.minecraft.client.Camera}.
 * 
 * Permite bloquear, rotar o suavizar la perspectiva de la cámara durante cinemáticas especiales
 * y durante la transición a modo espectador tras ser eliminado en un reto.
 */

import com.eric.revill.RevillClient;
import com.eric.revill.fabric.api.EnvType;
import com.eric.revill.fabric.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin({Camera.class})
public abstract class CameraMixin {
   @Shadow
   protected abstract void setRotation(float var1, float var2);

   @Inject(
      method = {"setup(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/world/entity/Entity;ZZF)V"},
      at = {@At("TAIL")}
   )
   private void dedsafio4$shakeCameraDuringRift(
      BlockGetter area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci
   ) {
      float strength = RevillClient.e.c();
      if (!(strength <= 0.0F)) {
         Camera camera = (Camera)(Object)this;
         float time = (float)((double)System.nanoTime() / 1.0E9);
         float yaw = (float)(Math.sin((double)(time * 19.0F)) + Math.sin((double)(time * 31.0F)) * 0.4F) * strength * 0.8F;
         float pitch = (float)(Math.sin((double)(time * 23.0F)) + Math.sin((double)(time * 37.0F)) * 0.35F) * strength * 0.55F;
         this.setRotation(camera.getYRot() + yaw, camera.getXRot() + pitch);
      }
   }
}
