package com.eric.revill.j;

import net.minecraft.server.level.ServerPlayer;

public enum c {
   a(
      "bunker",
      "Ir al búnker",
      "el búnker",
      "REVILL ha liberado un gas tóxico en el exterior. Reuníos en el búnker configurado antes de que termine la cuenta atrás y permaneced dentro hasta el final. CONSEJO: seguid el waypoint del búnker y preparad una ruta segura. ADVERTENCIA: quien termine fuera del búnker será eliminado."
   ),
   b(
      "subir_250",
      "Subir hasta Y=250",
      "la capa Y=250",
      "REVILL ha alterado la gravedad y el peligro aumenta a ras de suelo. Subid hasta la capa Y=250 antes de que termine la cuenta atrás y permaneced a esa altura hasta el final. CONSEJO: llevad bloques y comida para construir una plataforma segura. ADVERTENCIA: quien no alcance Y=250 será eliminado."
   ),
   c(
      "bajar_-250",
      "Bajar hasta Y=-1",
      "la capa Y=-1",
      "REVILL ha abierto una grieta bajo el mundo. Descended hasta la capa Y=-1 antes de que termine la cuenta atrás y alcanzad esa altura para completar la tarea. CONSEJO: preparaos para descender con cuidado y vigilad vuestra posición."
   );

   private final String d;
   private final String e;
   private final String f;
   private final String g;

   public static c[] a() {
      return values();
   }

   public static c a(String name) {
      return valueOf(name);
   }

   private c(String id, String label, String shortTitle, String description) {
      this.d = id;
      this.e = label;
      this.f = shortTitle;
      this.g = description;
   }

   public String b() {
      return this.d;
   }

   public String c() {
      return this.e;
   }

   public String d() {
      return this.f;
   }

   public String e() {
      return this.g;
   }

   public String b(String additionalMessage) {
      return additionalMessage != null && !additionalMessage.isBlank() ? additionalMessage.trim() : this.g;
   }

   public boolean a(ServerPlayer player) {
      return switch (this) {
         case a -> com.eric.revill.b.b.a(player);
         case b -> player.getY() >= 250.0;
         case c -> player.getY() <= -1.0;
      };
   }

   public String c(String playerName) {
      return switch (this) {
         case a -> playerName + " ha llegado al búnker.";
         case b -> playerName + " ha alcanzado la capa Y=250.";
         case c -> playerName + " ha alcanzado la capa Y=-1.";
      };
   }

   public static c d(String id) {
      if (id != null) {
         for (c task : a()) {
            if (task.d.equals(id)) {
               return task;
            }
         }
      }

      return a;
   }
}
