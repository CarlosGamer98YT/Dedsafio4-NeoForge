package com.eric.revill.mixin;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.monster.Creeper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({Creeper.class})
public interface CreeperEntityChargedAccessor {
   @Accessor("DATA_IS_POWERED")
   static EntityDataAccessor<Boolean> dedsafio4$getChargedData() {
      throw new AssertionError();
   }
}
