package com.eric.revill.m;

import com.eric.revill.ForgeRegistration;
import com.eric.revill.fabric.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import com.eric.revill.fabric.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public final class b extends AbstractContainerMenu {
   public static final MenuType<b> a = ForgeRegistration.register(
      Registries.MENU,
      ResourceLocation.fromNamespaceAndPath("dedsafio-4-mechanics", "mail_compose"),
      new ExtendedScreenHandlerType((syncId, inventory, buf) -> new b(syncId, inventory, new SimpleContainer(21)))
   );
   private final Inventory b;
   private final SimpleContainer c;

   public b(int syncId, Inventory playerInventory, SimpleContainer attachments) {
      super(a, syncId);
      this.b = playerInventory;
      this.c = attachments;
      checkContainerSize(attachments, 21);

      for (int slot = 0; slot < 21; slot++) {
         this.addSlot(new Slot(attachments, slot, 0, 0));
      }

      for (int row = 0; row < 3; row++) {
         for (int column = 0; column < 9; column++) {
            this.addSlot(new Slot(playerInventory, column + row * 9 + 9, 0, 0));
         }
      }

      for (int column = 0; column < 9; column++) {
         this.addSlot(new Slot(playerInventory, column, 0, 0));
      }
   }

   public static ExtendedScreenHandlerFactory a() {
      return new ExtendedScreenHandlerFactory() {
         public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
         }

         public Component getDisplayName() {
            return Component.literal("Enviar correo");
         }

         public AbstractContainerMenu createMenu(int syncId, Inventory inventory, Player player) {
            return new b(syncId, inventory, new SimpleContainer(21));
         }
      };
   }

   public static MenuType<b> b() {
      return a;
   }

   public SimpleContainer c() {
      return this.c;
   }

   public void d() {
      for (int index = 0; index < this.c.getContainerSize(); index++) {
         this.c.setItem(index, ItemStack.EMPTY);
      }
   }

   public void a(int[][] positions) {
      if (positions.length == this.slots.size()) {
         for (int index = 0; index < positions.length; index++) {
            Slot old = (Slot)this.slots.get(index);
            Slot replacement = index < 21
               ? new Slot(this.c, index, positions[index][0], positions[index][1])
               : new Slot(this.b, old.getContainerSlot(), positions[index][0], positions[index][1]);
            replacement.index = old.index;
            this.slots.set(index, replacement);
         }
      }
   }

   public ItemStack quickMoveStack(Player player, int slot) {
      if (slot >= 0 && slot < this.slots.size()) {
         Slot slot2 = (Slot)this.slots.get(slot);
         if (!slot2.hasItem()) {
            return ItemStack.EMPTY;
         } else {
            ItemStack original = slot2.getItem();
            ItemStack result = original.copy();
            if (slot < 21 ? this.moveItemStackTo(original, 21, this.slots.size(), true) : this.moveItemStackTo(original, 0, 21, false)) {
               if (original.isEmpty()) {
                  slot2.setByPlayer(ItemStack.EMPTY);
               } else {
                  slot2.setChanged();
               }

               return result;
            } else {
               return ItemStack.EMPTY;
            }
         }
      } else {
         return ItemStack.EMPTY;
      }
   }

   public boolean stillValid(Player player) {
      return true;
   }

   public void removed(Player player) {
      super.removed(player);
      if (!player.level().isClientSide) {
         for (int index = 0; index < this.c.getContainerSize(); index++) {
            ItemStack stack = this.c.removeItemNoUpdate(index);
            if (!stack.isEmpty() && !this.b.add(stack)) {
               player.drop(stack, false);
            }
         }

         this.c.stopOpen(player);
      }
   }
}
