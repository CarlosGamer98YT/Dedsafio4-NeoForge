package com.eric.revill.l;

import com.eric.revill.fabric.fabric.api.networking.v1.PacketByteBufs;
import com.eric.revill.fabric.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;

/**
 * Ítem de la Guía de Administración de DEDSAFIO 4.
 * 
 * Al hacer clic derecho, si el jugador tiene permisos de operador (nivel 4),
 * envía un paquete de red al cliente para abrir la interfaz gráfica {@code AdminGuideScreen},
 * la cual documenta todos los comandos y mecánicas del evento.
 * 
 * También contiene el método utilitario {@link #grantOnLogin(ServerPlayer)}
 * para entregar automáticamente el libro al operador al entrar al servidor.
 */
public final class AdminGuideItem extends Item {
   public AdminGuideItem(Properties settings) {
      super(settings);
   }

   @Override
   public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
      ItemStack stack = user.getItemInHand(hand);
      if (!world.isClientSide && user instanceof ServerPlayer player) {
         if (!player.hasPermissions(4)) {
            player.displayClientMessage(Component.literal("Necesitas ser operador de nivel 4 para abrir esta guía."), false);
            return InteractionResultHolder.fail(stack);
         }

         // Envía el paquete para abrir la pantalla AdminGuideScreen en el cliente
         ServerPlayNetworking.send(player, com.eric.revill.g.b.o, PacketByteBufs.empty());
      }

      return InteractionResultHolder.sidedSuccess(stack, world.isClientSide);
   }

   /**
    * Entrega la guía de administración en el inventario del jugador al iniciar sesión
    * si posee permisos de operador de nivel 4 y aún no tiene una en su inventario.
    * 
    * @param player Jugador que acaba de ingresar.
    */
   public static void grantOnLogin(ServerPlayer player) {
      if (player.hasPermissions(4)) {
         for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++) {
            if (player.getInventory().getItem(slot).is(g.t.get())) {
               return; // Ya la tiene
            }
         }

         ItemStack guide = new ItemStack((ItemLike)g.t.get());
         if (!player.getInventory().add(guide) && !guide.isEmpty()) {
            player.drop(guide, false); // Si el inventario está lleno, la suelta
         }
      }
   }
}
