package com.eric.revill.mixin;

import com.eric.revill.RevillClient;
import com.eric.revill.fabric.api.EnvType;
import com.eric.revill.fabric.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin({ClientLevel.class})
public abstract class ClientWorldMixin {
   @Inject(
      method = {"getSkyColor(Lnet/minecraft/world/phys/Vec3;F)Lnet/minecraft/world/phys/Vec3;"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private void dedsafio4$tintSkyRed(Vec3 cameraPosition, float tickDelta, CallbackInfoReturnable<Vec3> callbackInfo) {
      if (RevillClient.d.c()) {
         callbackInfo.setReturnValue(Vec3.ZERO);
      } else {
         Vec3 vanilla = (Vec3)callbackInfo.getReturnValue();
         if (vanilla != null) {
            double t = (double)RevillClient.a.c();
            double r = vanilla.x + (0.92 - vanilla.x) * t;
            double g2 = vanilla.y + (0.018 - vanilla.y) * t;
            double b2 = vanilla.z + (0.025 - vanilla.z) * t;
            callbackInfo.setReturnValue(new Vec3(r, g2, b2));
         }
      }
   }

   @Inject(
      method = {"getCloudColor(F)Lnet/minecraft/world/phys/Vec3;"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private void dedsafio4$tintCloudsRed(float tickDelta, CallbackInfoReturnable<Vec3> callbackInfo) {
      if (RevillClient.d.c()) {
         callbackInfo.setReturnValue(Vec3.ZERO);
      } else {
         Vec3 vanilla;
         if (RevillClient.a.b() && (vanilla = (Vec3)callbackInfo.getReturnValue()) != null) {
            double t = (double)RevillClient.a.c();
            double r = vanilla.x + (0.92 - vanilla.x) * t;
            double g2 = vanilla.y + (0.018 - vanilla.y) * t;
            double b2 = vanilla.z + (0.025 - vanilla.z) * t;
            callbackInfo.setReturnValue(new Vec3(r, g2, b2));
         }
      }
   }
}
