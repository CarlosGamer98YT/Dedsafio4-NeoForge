package com.eric.revill.o;

/**
 * BlockEntity del Pedestal de Desafíos Diarios.
 */

import java.util.Arrays;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public final class b extends BlockEntity implements Container {
   private static final int a = 21;
   private int b = 1;
   private final ItemStack[] c = new ItemStack[21];

   public b(BlockPos pos, BlockState state) {
      super(e.c, pos, state);
      Arrays.fill(this.c, ItemStack.EMPTY);
   }

   public int a() {
      return this.b;
   }

   public List<ItemStack> b() {
      return Arrays.stream(this.c).filter(stack -> !stack.isEmpty()).<ItemStack>map(ItemStack::copy).toList();
   }

   public void a(int day) {
      this.b = day;
      this.setChanged();
      if (this.level != null && !this.level.isClientSide) {
         this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
      }
   }

   @Override
   protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
      super.saveAdditional(nbt, registries);
      nbt.putInt("day", this.b);
      ListTag items = new ListTag();

      for (int slot = 0; slot < this.c.length; slot++) {
         if (!this.c[slot].isEmpty()) {
            CompoundTag item = (CompoundTag)this.c[slot].save(registries, new CompoundTag());
            item.putByte("Slot", (byte)slot);
            items.add(item);
         }
      }

      nbt.put("rewards", items);
   }

   @Override
   protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
      super.loadAdditional(nbt, registries);
      this.b = Math.max(1, nbt.getInt("day"));
      Arrays.fill(this.c, ItemStack.EMPTY);
      if (nbt.contains("rewards", 10)) {
         ListTag items = nbt.getList("rewards", 10);

         for (int index = 0; index < items.size(); index++) {
            CompoundTag item = items.getCompound(index);
            int slot = item.getByte("Slot") & 255;
            if (slot < this.c.length) {
               this.c[slot] = ItemStack.parseOptional(registries, item);
            }
         }
      } else if (nbt.contains("reward", 10)) {
         this.c[0] = ItemStack.parseOptional(registries, nbt.getCompound("reward"));
      }
   }

   @Override
   public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
      CompoundTag nbt = new CompoundTag();
      this.saveAdditional(nbt, registries);
      return nbt;
   }

   public ClientboundBlockEntityDataPacket c() {
      return ClientboundBlockEntityDataPacket.create(this);
   }

   public int getContainerSize() {
      return this.c.length;
   }

   public boolean isEmpty() {
      for (ItemStack stack : this.c) {
         if (!stack.isEmpty()) {
            return false;
         }
      }

      return true;
   }

   public ItemStack getItem(int slot) {
      return slot >= 0 && slot < this.c.length ? this.c[slot] : ItemStack.EMPTY;
   }

   public ItemStack removeItem(int slot, int amount) {
      if (slot >= 0 && slot < this.c.length) {
         ItemStack result = ContainerHelper.removeItem(Arrays.asList(this.c), slot, amount);
         if (!result.isEmpty()) {
            this.setChanged();
         }

         return result;
      } else {
         return ItemStack.EMPTY;
      }
   }

   public ItemStack removeItemNoUpdate(int slot) {
      if (slot >= 0 && slot < this.c.length) {
         ItemStack result = this.c[slot];
         this.c[slot] = ItemStack.EMPTY;
         if (!result.isEmpty()) {
            this.setChanged();
         }

         return result;
      } else {
         return ItemStack.EMPTY;
      }
   }

   public void setItem(int slot, ItemStack stack) {
      if (slot >= 0 && slot < this.c.length) {
         this.c[slot] = stack;
         if (stack.getCount() > this.getMaxStackSize()) {
            stack.setCount(this.getMaxStackSize());
         }

         this.setChanged();
      }
   }

   public void setChanged() {
      super.setChanged();
      if (this.level != null && !this.level.isClientSide) {
         this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
      }
   }

   public boolean stillValid(Player player) {
      return !this.isRemoved() && player.distanceToSqr(this.worldPosition.getCenter()) <= 64.0;
   }

   public void clearContent() {
      Arrays.fill(this.c, ItemStack.EMPTY);
      this.setChanged();
   }

   public Packet getUpdatePacket() {
      return this.c();
   }
}
