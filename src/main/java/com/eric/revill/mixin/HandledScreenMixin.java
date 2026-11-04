package com.eric.revill.mixin;

import com.eric.revill.l.g;
import com.eric.revill.fabric.api.EnvType;
import com.eric.revill.fabric.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin({AbstractContainerScreen.class})
public abstract class HandledScreenMixin {
   private static final int DEDSAFIO4_GREEN = -12327313;

   @Inject(
      method = {"renderSlot"},
      at = {@At("TAIL")}
   )
   private void dedsafio4$drawGreenSlotBorder(GuiGraphics context, Slot slot, CallbackInfo callbackInfo) {
      ItemStack stack = slot.getItem();
      if (stack.is(g.c.get())
         || stack.is(g.d.get())
         || stack.is(g.e.get())
         || stack.is(g.f.get())
         || stack.is(Items.TOTEM_OF_UNDYING)) {
         int left = slot.x;
         int top = slot.y;
         int right = left + 16;
         int bottom = top + 16;
         context.fill(left, top, right, top + 1, -12327313);
         context.fill(left, bottom - 1, right, bottom, -12327313);
         context.fill(left, top + 1, left + 1, bottom - 1, -12327313);
         context.fill(right - 1, top + 1, right, bottom - 1, -12327313);
      }
   }
}
