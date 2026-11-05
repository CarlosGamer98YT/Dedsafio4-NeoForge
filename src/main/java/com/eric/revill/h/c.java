package com.eric.revill.h;

/**
 * Enumerador de los tipos de moneda de DEDSAFIO 4.
 * 
 * Define los 3 tipos de Dedita:
 * - AZUL: Valor base 1.
 * - VERDE: Valor 100 (100 Dedita Azul equivalen a 1 Dedita Verde).
 * - ROJA: Valor 10000.
 */

import com.eric.revill.l.g;
import java.util.Locale;
import java.util.function.Supplier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public enum c {
   a("azul", g.c, 1),
   b("roja", g.d, 10000),
   c("verde", g.f, 100);

   private final String d;
   private final Supplier<Item> e;
   private final int f;

   public static c[] a() {
      return values();
   }

   public static c a(String name) {
      return valueOf(name);
   }

   private c(String key, Supplier<Item> item, int valueInBlue) {
      this.d = key;
      this.e = item;
      this.f = valueInBlue;
   }

   public String b() {
      return this.d;
   }

   public Item c() {
      return this.e.get();
   }

   public int d() {
      return this.f;
   }

   public static c a(ItemStack stack) {
      if (stack != null && !stack.isEmpty()) {
         for (c type : a()) {
            if (stack.is(type.e.get())) {
               return type;
            }
         }

         return null;
      } else {
         return null;
      }
   }

   public static c b(String value) {
      if (value == null) {
         return null;
      } else {
         String normalized = value.toLowerCase(Locale.ROOT);

         for (c type : a()) {
            if (type.d.equals(normalized)) {
               return type;
            }
         }

         return null;
      }
   }
}
