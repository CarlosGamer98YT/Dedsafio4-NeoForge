package com.eric.revill.b;

import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

/**
 * Gestor global de la zona del Búnker en el servidor.
 * 
 * Permite definir las coordenadas del búnker con `/dedsafio4 bunker set`,
 * consultar su estado (`/dedsafio4 bunker show`), borrarlo (`/dedsafio4 bunker clear`)
 * y comprobar si un jugador se encuentra a salvo dentro durante el evento REVILL.
 */
public final class b {
   /** Zona vacía por defecto. */
   private static final a a = new a();

   /** Zona actual configurada del búnker. */
   private static a b = new a();

   private b() {
   }

   /**
    * Configura los límites del búnker en el Overworld a partir de dos esquinas.
    * 
    * @param server Instancia del servidor de Minecraft.
    * @param firstCorner Coordenadas de la primera esquina.
    * @param secondCorner Coordenadas de la segunda esquina opuesta.
    */
   public static void a(MinecraftServer server, BlockPos firstCorner, BlockPos secondCorner) {
      if (server != null) {
         ServerLevel world = server.overworld();
         b = new a();
         b.a(world.dimension().location().toString());
         b.b(firstCorner);
         b.c(secondCorner);
      }
   }

   /**
    * Obtiene la zona configurada actual del búnker.
    */
   public static a a() {
      return b == null ? a : b;
   }

   /**
    * Elimina la configuración del búnker actual.
    */
   public static void b() {
      b = a;
   }

   /**
    * Comprueba si el búnker está actualmente configurado.
    */
   public static boolean c() {
      return a().a();
   }

   /**
    * Verifica si el jugador especificado se encuentra físicamente dentro del búnker.
    * 
    * @param player Jugador a evaluar.
    * @return true si el jugador está a salvo dentro del búnker.
    */
   public static boolean a(ServerPlayer player) {
      return player == null ? false : a().a(player.blockPosition());
   }

   /**
    * Guarda los datos del búnker en el estado persistente del mundo (SavedData).
    */
   public static void a(com.eric.revill.j.b state) {
      if (state != null) {
         state.a(a());
      }
   }
}
