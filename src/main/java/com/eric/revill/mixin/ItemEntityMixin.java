package com.eric.revill.mixin;

/**
 * Inyección en las entidades de ítems en el suelo {@link net.minecraft.world.entity.item.ItemEntity}.
 * 
 * Intercepta la recolección o destrucción de ítems especiales de DEDSAFIO 4 (monedas, cartas de buzón)
 * para garantizar su seguridad y contabilización correcta en misiones.
 */

import com.eric.revill.o.g;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ItemEntity.class})
public abstract class ItemEntityMixin {
   @Unique
   private int dedsafio$pickupStartCount;
   @Unique
   private Item dedsafio$pickupItem;

   @Inject(
      method = {"playerTouch"},
      at = {@At("HEAD")}
   )
   private void dedsafio$recordPickupStart(Player player, CallbackInfo callbackInfo) {
      ItemEntity itemEntity = (ItemEntity)(Object)this;
      this.dedsafio$pickupStartCount = itemEntity.getItem().getCount();
      this.dedsafio$pickupItem = itemEntity.getItem().getItem();
   }

   @Inject(
      method = {"playerTouch"},
      at = {@At("TAIL")}
   )
   private void dedsafio$recordCollectedItems(Player player, CallbackInfo callbackInfo) {
      ItemEntity itemEntity = (ItemEntity)(Object)this;
      if (!itemEntity.level().isClientSide) {
         int collected = this.dedsafio$pickupStartCount - itemEntity.getItem().getCount();
         if (collected > 0) {
            g.a(player, this.dedsafio$pickupItem, collected);
         }
      }
   }

   @Inject(
      method = {"tick"},
      at = {@At("TAIL")}
   )
   private void dedsafio4$spawnGreenBeam(CallbackInfo callbackInfo) {
      ItemEntity itemEntity = (ItemEntity)(Object)this;
      if (itemEntity.level() instanceof ServerLevel serverWorld) {
         if (itemEntity.onGround()) {
            ItemStack stack = itemEntity.getItem();
            if (stack.is(com.eric.revill.l.g.c.get())
               || stack.is(com.eric.revill.l.g.d.get())
               || stack.is(com.eric.revill.l.g.e.get())
               || stack.is(com.eric.revill.l.g.f.get())
               || stack.is(Items.TOTEM_OF_UNDYING)) {
               if ((serverWorld.getGameTime() & 7L) == 0L) {
                  for (int segment = 0; segment < 5; segment++) {
                     serverWorld.sendParticles(
                        ParticleTypes.HAPPY_VILLAGER,
                        itemEntity.getX(),
                        itemEntity.getY() + 0.12 + (double)segment * 0.16,
                        itemEntity.getZ(),
                        1,
                        0.015,
                        0.01,
                        0.015,
                        0.005
                     );
                  }
               }
            }
         }
      }
   }
}
