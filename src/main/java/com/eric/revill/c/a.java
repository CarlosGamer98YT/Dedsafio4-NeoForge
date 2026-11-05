package com.eric.revill.c;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;

/**
 * Formateador de texto con iconos personalizados para el chat y mensajes de DEDSAFIO 4.
 * 
 * Utiliza la fuente personalizada {@code dedsafio-4-mechanics:icons} para inyectar
 * glifos de estrella, búnker, hogar, bandera y advertencia en los mensajes.
 */
public final class a {
   /** Identificador de la fuente personalizada con glifos de Dedsafio 4. */
   private static final ResourceLocation a = ResourceLocation.fromNamespaceAndPath("dedsafio-4-mechanics", "icons");

   private a() {
   }

   /**
    * Antepone un icono gráfico personalizado al mensaje de chat indicado.
    * 
    * @param message Mensaje base a mostrar.
    * @param icon Icono a anteponer.
    * @return Componente con el icono y el texto formateados.
    */
   public static Component a(Component message, a.Icon icon) {
      return Component.empty()
         .append(Component.literal(String.valueOf(icon.f)).setStyle(Style.EMPTY.withFont(a)))
         .append(Component.literal(" "))
         .append(message);
   }

   /**
    * Enumerador de iconos disponibles en la fuente personalizada.
    */
   public static enum Icon {
      /** Estrella (glifo \ue000). */
      a('\ue000'),
      /** Búnker / Seguridad (glifo \ue001). */
      b('\ue001'),
      /** Casa / Hogar (glifo \ue002). */
      c('\ue002'),
      /** Bandera (glifo \ue003). */
      d('\ue003'),
      /** Advertencia / Peligro (glifo \ue004). */
      e('\ue004');

      private final char f;

      public static a.Icon[] a() {
         return values();
      }

      public static a.Icon a(String name) {
         return valueOf(name);
      }

      private Icon(char glyph) {
         this.f = glyph;
      }
   }
}
