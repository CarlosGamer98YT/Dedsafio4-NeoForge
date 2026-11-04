package com.eric.revill.o;

import com.eric.revill.ForgeRegistration;
import com.eric.revill.fabric.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import com.eric.revill.fabric.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.core.BlockPos;
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
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public final class d extends AbstractContainerMenu {
   public static final MenuType<d> a = ForgeRegistration.register(
      Registries.MENU, ResourceLocation.fromNamespaceAndPath("dedsafio-4-mechanics", "daily_pedestal"), new ExtendedScreenHandlerType(d::new)
   );
   private final Container b;
   private final BlockPos c;
   private final ContainerData d;

   private d(int syncId, Inventory playerInventory, FriendlyByteBuf buf) {
      this(syncId, playerInventory, a(playerInventory, buf));
   }

   private d(int syncId, Inventory playerInventory, d.a data) {
      this(syncId, playerInventory, data.a(), data.b(), data.c());
   }

   private static d.a a(Inventory playerInventory, FriendlyByteBuf buf) {
      BlockPos pos = buf.readBlockPos();
      if (playerInventory.player.level().getBlockEntity(pos) instanceof b pedestal) {
         SimpleContainerData day = new SimpleContainerData(1);
         day.set(0, pedestal.a());
         return new d.a(pedestal, pos, day);
      } else {
         return new d.a(new SimpleContainer(21), pos, new SimpleContainerData(1));
      }
   }

   private d(int syncId, Inventory playerInventory, Container rewards, BlockPos pos, ContainerData properties) {
      super(a, syncId);
      this.b = rewards;
      this.c = pos;
      this.d = properties;
      checkContainerSize(rewards, 21);
      this.addDataSlots(properties);

      for (int row = 0; row < 3; row++) {
         for (int column = 0; column < 7; column++) {
            this.addSlot(new Slot(rewards, row * 7 + column, 25 + column * 18, 34 + row * 18));
         }
      }

      for (int var10 = 0; var10 < 3; var10++) {
         for (int column = 0; column < 9; column++) {
            this.addSlot(new Slot(playerInventory, column + var10 * 9 + 9, 8 + column * 18, 108 + var10 * 18));
         }
      }

      for (int column2 = 0; column2 < 9; column2++) {
         this.addSlot(new Slot(playerInventory, column2, 8 + column2 * 18, 166));
      }
   }

   public static ExtendedScreenHandlerFactory a(b pedestal) {
      return new ExtendedScreenHandlerFactory() {
         public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
            buf.writeBlockPos(pedestal.getBlockPos());
         }

         public Component getDisplayName() {
            return Component.translatable("block.dedsafio-4-mechanics.daily_pedestal.title");
         }

         public AbstractContainerMenu createMenu(int syncId, Inventory inventory, Player player) {
            ContainerData day = new ContainerData() {
               public int get(int index) {
                  return index == 0 ? pedestal.a() : 0;
               }

               public void set(int index, int value) {
               }

               public int getCount() {
                  return 1;
               }
            };
            return new d(syncId, inventory, pedestal, pedestal.getBlockPos(), day);
         }
      };
   }

   public BlockPos a() {
      return this.c;
   }

   public int b() {
      return this.d.get(0);
   }

   public boolean stillValid(Player player) {
      Container inventory = this.b;
      b pedestal;
      return inventory instanceof b && (pedestal = (b)inventory).stillValid(player);
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

   private static record a(Container a, BlockPos b, ContainerData c) {
   }
}
