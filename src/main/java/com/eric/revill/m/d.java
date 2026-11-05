package com.eric.revill.m;

/**
 * Almacenamiento en memoria y disco de las cartas de correo pendientes de cada jugador.
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public final class d {
   public static final d a = new d();
   private final Map<UUID, List<a>> b = new ConcurrentHashMap<>();

   private d() {
   }

   public List<a> a(UUID playerId) {
      return this.b.computeIfAbsent(playerId, ignored -> new ArrayList<>());
   }

   public int b(UUID playerId) {
      int count = 0;

      for (a mail : this.a(playerId)) {
         if (!mail.f()) {
            count++;
         }
      }

      return count;
   }

   public boolean c(UUID playerId) {
      return this.b(playerId) > 0;
   }

   public void a(UUID recipientId, UUID senderId, String senderName, String subject, String message, List<ItemStack> items) {
      a mail = new a(senderId, senderName, subject, message, items);
      this.a(recipientId).add(0, mail);
   }

   public void a(UUID playerId, int index) {
      List<a> mails = this.a(playerId);
      if (index >= 0 && index < mails.size()) {
         mails.get(index).a(true);
      }
   }

   public void b(UUID playerId, int index) {
      List<a> mails = this.a(playerId);
      if (index >= 0 && index < mails.size()) {
         mails.remove(index);
      }
   }

   public void a(UUID playerId, int index, Player player) {
      List<a> mails = this.a(playerId);
      if (index >= 0 && index < mails.size()) {
         a mail = mails.get(index);
         Iterator<ItemStack> items = mail.e().iterator();

         while (items.hasNext()) {
            ItemStack stack = items.next();
            if (stack.isEmpty()) {
               items.remove();
            } else {
               ItemStack remainder = stack.copy();
               if (!player.getInventory().add(remainder) && !remainder.isEmpty()) {
                  player.drop(remainder, false);
               }

               items.remove();
            }
         }
      }
   }

   public a c(UUID playerId, int index) {
      List<a> mails = this.a(playerId);
      return index >= 0 && index < mails.size() ? mails.get(index) : null;
   }

   public List<a> d(UUID playerId) {
      return Collections.unmodifiableList(new ArrayList<>(this.a(playerId)));
   }

   public void a(ServerPlayer recipient, String senderName, String message) {
      recipient.displayClientMessage(Component.literal("Correo de " + senderName + ": " + message), false);
   }
}
