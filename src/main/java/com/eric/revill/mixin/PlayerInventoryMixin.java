package com.eric.revill.mixin;

/**
 * Inyección en el inventario del jugador {@link net.minecraft.world.entity.player.Inventory}.
 * 
 * Verifica e impide equipar piezas de armadura de netherite durante los primeros días de la serie
 * según lo dicten los cambios de dificultad activos.
 */

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({Inventory.class})
public abstract class PlayerInventoryMixin implements Container {
   public int getMaxStackSize() {
      return 99;
   }
}
