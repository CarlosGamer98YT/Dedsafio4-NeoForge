package com.eric.revill.d.b;

/**
 * Renderizador de iconos y distintivos de Hermandad / Gremio.
 * 
 * Dibuja los estandartes y placas de nombre personalizadas con el color del clan
 * sobre las cabezas de los jugadores en tercera persona o en la lista de jugadores (Tab).
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.eric.revill.fabric.api.EnvType;
import com.eric.revill.fabric.api.Environment;

@Environment(EnvType.CLIENT)
public class c {
   private static final List<com.eric.revill.t.a> a = new ArrayList<>();

   private c() {
   }

   public static void a(List<com.eric.revill.t.a> waypoints) {
      a.clear();
      if (waypoints != null) {
         a.addAll(waypoints);
      }
   }

   public static void b(List<com.eric.revill.t.a> guildWaypoints) {
      a.removeIf(waypoint -> waypoint.a().startsWith("guild_"));
      if (guildWaypoints != null) {
         a.addAll(guildWaypoints);
      }
   }

   public static void a() {
      a.clear();
   }

   public static List<com.eric.revill.t.a> b() {
      return Collections.unmodifiableList(a);
   }
}
