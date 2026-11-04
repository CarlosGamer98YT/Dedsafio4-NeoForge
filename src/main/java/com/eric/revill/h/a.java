package com.eric.revill.h;

import com.eric.revill.ForgeRegistration;
import com.eric.revill.fabric.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import com.eric.revill.fabric.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public final class a extends AbstractContainerMenu {
   public static final int a = 0;
   public static final MenuType<a> b = ForgeRegistration.register(
      Registries.MENU,
      ResourceLocation.fromNamespaceAndPath("dedsafio-4-mechanics", "atm_deposit"),
      new ExtendedScreenHandlerType((syncId, inventory, buf) -> new a(syncId, inventory, new SimpleContainer(9)))
   );
   private final Inventory c;
   private final SimpleContainer d;

   public static MenuType<a> a() {
      return b;
   }

   public a(int syncId, Inventory playerInventory, SimpleContainer depositInventory) {
      super(b, syncId);
      this.c = playerInventory;
      this.d = depositInventory;
      checkContainerSize(depositInventory, 9);

      for (int row = 0; row < 3; row++) {
         for (int column = 0; column < 3; column++) {
            int index = row * 3 + column;
            this.addSlot(new a.DepositSlot(depositInventory, index, 0, 0));
         }
      }

      for (int var8 = 0; var8 < 3; var8++) {
         for (int column = 0; column < 9; column++) {
            this.addSlot(new Slot(playerInventory, column + var8 * 9 + 9, 0, 0));
         }
      }

      for (int column2 = 0; column2 < 9; column2++) {
         this.addSlot(new Slot(playerInventory, column2, 0, 0));
      }
   }

   public static ExtendedScreenHandlerFactory b() {
      return new ExtendedScreenHandlerFactory() {
         public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
         }

         public Component getDisplayName() {
            return Component.literal("Cajero Automático");
         }

         public AbstractContainerMenu createMenu(int syncId, Inventory inventory, Player player) {
            return new a(syncId, inventory, new SimpleContainer(9));
         }
      };
   }

   public boolean clickMenuButton(Player player, int id) {
      if (id == 0 && player instanceof ServerPlayer serverPlayer) {
         int[] deposited = this.c();

         for (int slot = 0; slot < this.d.getContainerSize(); slot++) {
            ItemStack stack = this.d.getItem(slot);
            if (com.eric.revill.h.c.a(stack) != null) {
               this.d.setItem(slot, ItemStack.EMPTY);
            }
         }

         for (c type : com.eric.revill.h.c.a()) {
            com.eric.revill.h.b.a(serverPlayer.getServer(), serverPlayer.getUUID(), type, deposited[type.ordinal()]);
         }

         serverPlayer.displayClientMessage(Component.literal("Deditas ingresadas en la cuenta."), true);
         serverPlayer.closeContainer();
         return true;
      } else {
         return false;
      }
   }

   public int[] c() {
      int[] deposited = new int[com.eric.revill.h.c.a().length];

      for (int slot = 0; slot < this.d.getContainerSize(); slot++) {
         ItemStack stack = this.d.getItem(slot);
         c type = com.eric.revill.h.c.a(stack);
         if (type != null) {
            int n2 = type.ordinal();
            deposited[n2] += stack.getCount();
         }
      }

      long totalValueInBlue = 0L;

      for (c type : com.eric.revill.h.c.a()) {
         totalValueInBlue += (long)deposited[type.ordinal()] * (long)type.d();
      }

      deposited[com.eric.revill.h.c.b.ordinal()] = (int)(totalValueInBlue / (long)com.eric.revill.h.c.b.d());
      long var9;
      deposited[com.eric.revill.h.c.c.ordinal()] = (int)((var9 = totalValueInBlue % (long)com.eric.revill.h.c.b.d()) / (long)com.eric.revill.h.c.c.d());
      deposited[com.eric.revill.h.c.a.ordinal()] = (int)(var9 % (long)com.eric.revill.h.c.c.d());
      return deposited;
   }

   public ItemStack quickMoveStack(Player player, int slot) {
      if (slot >= 0 && slot < this.slots.size()) {
         Slot slot2 = (Slot)this.slots.get(slot);
         if (!slot2.hasItem()) {
            return ItemStack.EMPTY;
         } else {
            ItemStack original = slot2.getItem();
            ItemStack result = original.copy();
            if (slot < 9
               ? this.moveItemStackTo(original, 9, this.slots.size(), true)
               : com.eric.revill.h.c.a(original) != null && this.moveItemStackTo(original, 0, 9, false)) {
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
         for (int index = 0; index < this.d.getContainerSize(); index++) {
            ItemStack stack = this.d.removeItemNoUpdate(index);
            if (!stack.isEmpty() && !this.c.add(stack)) {
               player.drop(stack, false);
            }
         }

         this.d.stopOpen(player);
      }
   }

   public void a(int[][] positions) {
      int expected = 45;
      if (positions.length == expected && this.slots.size() == expected) {
         for (int index = 0; index < expected; index++) {
            Slot old = (Slot)this.slots.get(index);
            Slot replacement = (Slot)(index < 9
               ? new a.DepositSlot(this.d, index, positions[index][0], positions[index][1])
               : new Slot(this.c, old.getContainerSlot(), positions[index][0], positions[index][1]));
            replacement.index = old.index;
            this.slots.set(index, replacement);
         }
      }
   }

   private static final class DepositSlot extends Slot {
      private DepositSlot(Container inventory, int index, int x, int y) {
         super(inventory, index, x, y);
      }

      public boolean mayPlace(ItemStack stack) {
         return com.eric.revill.h.c.a(stack) != null;
      }

      public int getMaxStackSize() {
         return 99;
      }

      public int getMaxStackSize(ItemStack stack) {
         return com.eric.revill.h.c.a(stack) == null ? super.getMaxStackSize(stack) : stack.getMaxStackSize();
      }
   }
}
