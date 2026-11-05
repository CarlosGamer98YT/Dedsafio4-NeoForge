package com.eric.revill.m;

/**
 * Menú contenedor (ScreenHandler) para ver y reclamar cartas recibidas en el Buzón.
 */

import com.eric.revill.ForgeRegistration;
import java.util.ArrayList;
import java.util.List;
import com.eric.revill.fabric.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import com.eric.revill.fabric.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

public class c extends AbstractContainerMenu {
   public static final MenuType<c> a = ForgeRegistration.register(
      Registries.MENU, ResourceLocation.fromNamespaceAndPath("dedsafio-4-mechanics", "mail_inbox"), new ExtendedScreenHandlerType(c::new)
   );
   private final List<c.a> b;

   public c(int syncId, Inventory playerInventory) {
      super(a, syncId);
      this.b = new ArrayList<>();
   }

   public c(int syncId, Inventory playerInventory, FriendlyByteBuf buf) {
      super(a, syncId);
      this.b = e.a(buf);
   }

   public static ExtendedScreenHandlerFactory a() {
      return new ExtendedScreenHandlerFactory() {
         public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
            e.a(buf, d.a.d(player.getUUID()));
         }

         public Component getDisplayName() {
            return Component.literal("Buzón de correo");
         }

         public AbstractContainerMenu createMenu(int syncId, Inventory inventory, Player player) {
            return new c(syncId, inventory);
         }
      };
   }

   public static MenuType<c> b() {
      return a;
   }

   public List<c.a> c() {
      return this.b;
   }

   public void a(List<c.a> updatedMails) {
      this.b.clear();
      this.b.addAll(updatedMails);
   }

   public boolean stillValid(Player player) {
      return true;
   }

   public boolean clickMenuButton(Player player, int id) {
      return false;
   }

   public ItemStack quickMoveStack(Player player, int slot) {
      return ItemStack.EMPTY;
   }

   public static final class a {
      private final String a;
      private final String b;
      private final boolean c;
      private final List<ItemStack> d;

      public a(String sender, String message, boolean read, List<ItemStack> items) {
         this.a = sender;
         this.b = message;
         this.c = read;
         this.d = items;
      }

      public String a() {
         return this.a;
      }

      public String b() {
         return this.b;
      }

      public boolean c() {
         return this.c;
      }

      public List<ItemStack> d() {
         return this.d;
      }
   }
}
