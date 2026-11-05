package com.eric.revill.a;

/**
 * Menú contenedor (ScreenHandler) de la Mochila Portátil (Ender Bag).
 * 
 * Gestiona un inventario móvil de 27 casillas (3x9) serializado y guardado
 * directamente en los DataComponents del ItemStack de la mochila. Previene
 * anidar mochilas dentro de sí mismas mediante validación de slots.
 */

import com.eric.revill.ForgeRegistration;
import com.eric.revill.l.g;
import com.eric.revill.fabric.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import com.eric.revill.fabric.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

public final class a extends AbstractContainerMenu {
   public static final MenuType<a> a = ForgeRegistration.register(
      Registries.MENU, ResourceLocation.fromNamespaceAndPath("dedsafio-4-mechanics", "portable_bag"), new ExtendedScreenHandlerType(a::new)
   );
   private final Inventory b;
   private final Container c;
   private final boolean d;
   private final ItemStack e;

   private a(int syncId, Inventory playerInventory, FriendlyByteBuf buf) {
      this(syncId, playerInventory, a(buf), null, false);
   }

   private a(int syncId, Inventory playerInventory, Container bagInventory, ItemStack backingBag, boolean persistentBag) {
      super(a, syncId);
      this.b = playerInventory;
      this.c = bagInventory;
      this.e = backingBag;
      this.d = persistentBag;
      checkContainerSize(bagInventory, 27);
      bagInventory.startOpen(playerInventory.player);

      for (int row = 0; row < 3; row++) {
         for (int column = 0; column < 9; column++) {
            this.addSlot(new a.b(bagInventory, row * 9 + column, 8 + column * 18, 18 + row * 18));
         }
      }

      for (int var10 = 0; var10 < 3; var10++) {
         for (int column = 0; column < 9; column++) {
            this.addSlot(new a.c(playerInventory, column + var10 * 9 + 9, 8 + column * 18, 86 + var10 * 18, backingBag));
         }
      }

      for (int column2 = 0; column2 < 9; column2++) {
         this.addSlot(new a.c(playerInventory, column2, 8 + column2 * 18, 144, backingBag));
      }
   }

   private static HolderLookup.Provider getRegistries() {
      MinecraftServer server = net.neoforged.neoforge.server.ServerLifecycleHooks.getCurrentServer();
      if (server != null) {
         return server.registryAccess();
      }
      try {
         if (net.minecraft.client.Minecraft.getInstance().level != null) {
            return net.minecraft.client.Minecraft.getInstance().level.registryAccess();
         }
      } catch (Throwable ignored) {
      }
      return null;
   }

   public static ExtendedScreenHandlerFactory a(ItemStack bagStack) {
      SimpleContainer inventory = new SimpleContainer(27);
      CustomData customData = bagStack.get(DataComponents.CUSTOM_DATA);
      if (customData != null && customData.contains("BagItems")) {
         HolderLookup.Provider registries = getRegistries();
         if (registries != null) {
            a(inventory, customData.copyTag().getList("BagItems", 10), registries);
         }
      }

      return new a.PortableBagFactory(inventory, bagStack, true, Component.literal("Bolsa primitiva"));
   }

   public static ExtendedScreenHandlerFactory a(ServerPlayer player) {
      return new a.PortableBagFactory(player.getEnderChestInventory(), ItemStack.EMPTY, false, Component.literal("Ender Bag").withStyle(ChatFormatting.DARK_AQUA));
   }

   public static MenuType<a> a() {
      return a;
   }

   private static Container a(FriendlyByteBuf buf) {
      buf.readBoolean();
      SimpleContainer inventory = new SimpleContainer(27);
      CompoundTag data = buf.readNbt();
      if (data != null && data.contains("BagItems", 9)) {
         HolderLookup.Provider registries = getRegistries();
         if (registries != null) {
            a(inventory, data.getList("BagItems", 10), registries);
         }
      }

      return inventory;
   }

   private static CompoundTag a(Container inventory, HolderLookup.Provider registries) {
      CompoundTag data = new CompoundTag();
      ListTag contents = new ListTag();

      if (registries != null) {
         for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
            ItemStack stack = inventory.getItem(slot);
            if (!stack.isEmpty()) {
               CompoundTag entry = (CompoundTag)stack.save(registries);
               entry.putByte("Slot", (byte)slot);
               contents.add(entry);
            }
         }
      }

      data.put("BagItems", contents);
      return data;
   }

   private static void a(Container inventory, ListTag contents, HolderLookup.Provider registries) {
      for (int index = 0; index < contents.size(); index++) {
         CompoundTag entry = contents.getCompound(index);
         int slot = entry.getByte("Slot") & 255;
         if (slot < inventory.getContainerSize()) {
            inventory.setItem(slot, ItemStack.parseOptional(registries, entry));
         }
      }
   }

   public ItemStack quickMoveStack(Player player, int slot) {
      if (slot >= 0 && slot < this.slots.size()) {
         Slot slot2 = (Slot)this.slots.get(slot);
         if (!slot2.hasItem()) {
            return ItemStack.EMPTY;
         } else {
            ItemStack stack = slot2.getItem();
            ItemStack original = stack.copy();
            boolean moved = slot < 27 ? this.moveItemStackTo(stack, 27, this.slots.size(), true) : this.moveItemStackTo(stack, 0, 27, false);
            if (!moved) {
               return ItemStack.EMPTY;
            } else {
               if (stack.isEmpty()) {
                  slot2.setByPlayer(ItemStack.EMPTY);
               } else {
                  slot2.setChanged();
               }

               return original;
            }
         }
      } else {
         return ItemStack.EMPTY;
      }
   }

   public boolean stillValid(Player player) {
      return this.c.stillValid(player);
   }

   public void removed(Player player) {
      if (this.d && !player.level().isClientSide && !this.e.isEmpty()) {
         HolderLookup.Provider registries = player.level().registryAccess();
         CustomData.update(DataComponents.CUSTOM_DATA, this.e, tag -> tag.put("BagItems", a(this.c, registries).getList("BagItems", 10)));
      }

      super.removed(player);
      this.c.stopOpen(player);
   }

   private static boolean b(ItemStack stack) {
      return stack.is(g.m.get()) || stack.is(g.n.get());
   }

   private static final class PortableBagFactory implements ExtendedScreenHandlerFactory {
      private final Container a;
      private final ItemStack b;
      private final boolean c;
      private final Component d;

      private PortableBagFactory(Container inventory, ItemStack backingBag, boolean persistentBag, Component title) {
         this.a = inventory;
         this.b = backingBag;
         this.c = persistentBag;
         this.d = title;
      }

      public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
         buf.writeBoolean(this.c);
         buf.writeNbt(com.eric.revill.a.a.a(this.a, player.registryAccess()));
      }

      public Component getDisplayName() {
         return this.d;
      }

      public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
         return new a(syncId, playerInventory, this.a, this.b, this.c);
      }
   }

   private static final class b extends Slot {
      private b(Container inventory, int index, int x, int y) {
         super(inventory, index, x, y);
      }

      public boolean mayPlace(ItemStack stack) {
         return !com.eric.revill.a.a.b(stack);
      }
   }

   private static final class c extends Slot {
      private final ItemStack a;

      private c(Inventory inventory, int index, int x, int y, ItemStack lockedStack) {
         super(inventory, index, x, y);
         this.a = lockedStack == null ? ItemStack.EMPTY : lockedStack;
      }

      public boolean mayPickup(Player playerEntity) {
         return this.a.isEmpty() || this.getItem() != this.a;
      }
   }
}
