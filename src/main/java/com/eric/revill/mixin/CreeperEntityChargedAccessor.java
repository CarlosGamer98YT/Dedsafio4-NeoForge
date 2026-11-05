package com.eric.revill.mixin;

/**
 * Accesor Mixin para acceder al campo privado {@code DATA_IS_POWERED} de {@link net.minecraft.world.entity.monster.Creeper}.
 * 
 * Permite al gestor de retos de DEDSAFIO 4 transformar creepers ordinarios en creepers
 * electrificados cargados al momento de spawnear durante días de dificultad alta.
 */

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
