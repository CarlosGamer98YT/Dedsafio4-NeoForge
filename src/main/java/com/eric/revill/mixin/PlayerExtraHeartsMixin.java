package com.eric.revill.mixin;

import com.eric.revill.q.a;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({Player.class})
public abstract class PlayerExtraHeartsMixin implements a {
   @Unique
   private int dedsafio4$redHearts;

   @Override
   public int dedsafio4$getRedHearts() {
      return this.dedsafio4$redHearts;
   }

   @Override
   public void dedsafio4$setRedHearts(int hearts) {
      this.dedsafio4$redHearts = Math.max(0, hearts);
   }

   @Inject(
      method = {"readAdditionalSaveData"},
      at = {@At("TAIL")}
   )
   private void dedsafio4$readExtraHearts(CompoundTag nbt, CallbackInfo ci) {
      this.dedsafio4$redHearts = Math.max(0, nbt.getInt("Dedsafio4RedHearts"));
   }

   @Inject(
      method = {"addAdditionalSaveData"},
      at = {@At("TAIL")}
   )
   private void dedsafio4$writeExtraHearts(CompoundTag nbt, CallbackInfo ci) {
      nbt.putInt("Dedsafio4RedHearts", this.dedsafio4$redHearts);
   }
}
