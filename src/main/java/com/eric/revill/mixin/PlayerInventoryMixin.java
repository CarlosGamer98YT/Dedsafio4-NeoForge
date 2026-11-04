package com.eric.revill.mixin;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({Inventory.class})
public abstract class PlayerInventoryMixin implements Container {
   public int getMaxStackSize() {
      return 99;
   }
}
