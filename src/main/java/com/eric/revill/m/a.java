package com.eric.revill.m;

/**
 * Estructura de datos de un paquete o mensaje de correo del Buzón.
 * 
 * Contiene el UUID del remitente, nombre, mensaje de texto y la lista de ítems adjuntos.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class a {
   private final UUID a;
   private final String b;
   private final String c;
   private final String d;
   private final List<ItemStack> e;
   private boolean f;

   public a(UUID senderId, String senderName, String subject, String message, List<ItemStack> items) {
      this.a = senderId;
      this.b = senderName;
      this.c = subject;
      this.d = message;
      this.e = new ArrayList<>();

      for (ItemStack stack : items) {
         if (!stack.isEmpty()) {
            this.e.add(stack.copy());
         }
      }
   }

   public UUID a() {
      return this.a;
   }

   public String b() {
      return this.b;
   }

   public String c() {
      return this.c;
   }

   public String d() {
      return this.d;
   }

   public List<ItemStack> e() {
      return this.e;
   }

   public boolean f() {
      return this.f;
   }

   public void a(boolean read) {
      this.f = read;
   }

   public Component g() {
      return Component.literal(this.b + " — " + this.c);
   }
}
