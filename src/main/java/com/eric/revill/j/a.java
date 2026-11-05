package com.eric.revill.j;

/**
 * Gestor principal del "MOMENTO REVILL".
 * 
 * Controla el ciclo de vida completo del evento:
 * - Inicio del evento con cuenta atrás configurable (por defecto 10 minutos).
 * - Selección de objetivo: llegar al búnker, subir a Y=250 o descender a Y=-1.
 * - Activación de cielo rojo apocalíptico y música de tensión.
 * - Generación de meteoritos cayendo del cielo.
 * - Verificación final: quienes no hayan cumplido el objetivo son eliminados y pasan a modo espectador.
 */

import com.eric.revill.RevillMod;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.DimensionDataStorage;

public class a {
   /** Instancia singleton del gestor del Momento Revill */
   public static final a a = new a();
   /** Configuración deserializada desde revill.json */
   private final com.eric.revill.f.a b = RevillMod.c;
   /** Gestor y generador de meteoritos en el Overworld */
   private final com.eric.revill.n.b c = new com.eric.revill.n.b();
   /** Conjunto de jugadores monitoreados durante el evento */
   private final Set<ServerPlayer> d = new HashSet<>();
   /** Indica si el Momento Revill está actualmente activo en el servidor */
   private boolean e;
   /** Segundos restantes de la cuenta atrás del evento */
   private int f = 600;
   /** Estado del cielo rojo apocalíptico */
   private boolean g;
   /** Estado de reproducción de música de tensión en los clientes */
   private boolean h;
   /** Bandera que indica si el evento ya ha finalizado */
   private boolean i;
   /** Contador interno de ticks del servidor (20 ticks = 1 segundo) */
   private int j;
   /** Objetivo actual en curso (BÚNKER, SUBIR A 250, BAJAR A -1) */
   private c k = com.eric.revill.j.c.a;
   /** Objetivo preconfigurado para el próximo evento */
   private c l = com.eric.revill.j.c.a;
   /** Registro de UUIDs de jugadores que ya han alcanzado el objetivo a salvo */
   private final Set<UUID> m = new HashSet<>();

   /** Obtiene la instancia de configuración revill.json */
   public com.eric.revill.f.a a() {
      return this.b;
   }

   /** Inicia la preparación del evento con el objetivo por defecto */
   public void a(MinecraftServer server) {
      this.a(server, com.eric.revill.j.c.a.b(), null);
   }

   /** Inicia la preparación del evento con ruleta y mensaje personalizado */
   public void a(MinecraftServer server, String rouletteMessage) {
      this.a(server, com.eric.revill.j.c.a.b(), rouletteMessage);
   }

   /** Inicia la preparación del evento configurando objetivo y mensaje adicional */
   public void a(MinecraftServer server, String taskId, String additionalMessage) {
      this.a(server, taskId, additionalMessage, "roja", this.b.d, this.b.revillMusicEnabled);
   }

   /**
    * Configura los parámetros del Momento Revill y lanza la ruleta visual a todos los jugadores.
    * 
    * @param server Servidor de Minecraft
    * @param taskId Identificador del objetivo (bunker, subir, bajar)
    * @param message Mensaje que se mostrará en pantalla
    * @param color Color del título o ruleta
    * @param durationSeconds Duración en segundos del evento
    * @param musicEnabled Si debe sonar la música ambiental de tensión
    */
   public void a(MinecraftServer server, String taskId, String message, String color, int durationSeconds, boolean musicEnabled) {
      if (server != null && !this.e) {
         this.l = com.eric.revill.j.c.d(taskId);
         this.b.d = Math.max(1, Math.min(86400, durationSeconds));
         this.b.revillMusicEnabled = musicEnabled;
         if (this.b.revillTaskMessages == null) {
            this.b.revillTaskMessages = new HashMap<>();
         }

         if (message != null && !message.isBlank()) {
            this.b.revillTaskMessages.put(this.l.b(), message.trim());
         } else {
            this.b.revillTaskMessages.remove(this.l.b());
         }

         this.b.b();
         com.eric.revill.g.a.a(server, color, this.l, message);
      }
   }

   /** Obtiene el objetivo configurado para el evento */
   public c b() {
      return this.l;
   }

   /**
    * Comienza formalmente el Momento Revill en el servidor.
    * Activa el cielo rojo, crea el waypoint del búnker si corresponde, inicia la cuenta atrás
    * y notifica a todos los clientes conectados.
    */
   public void b(MinecraftServer server) {
      if (server != null && !this.e) {
         this.e = true;
         this.i = false;
         this.j = 0;
         this.f = this.b.d;
         this.g = true;
         this.h = this.b.h;
         this.l = this.k = this.l;
         this.d.clear();
         this.m.clear();

         for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            this.d.add(player);
         }

         if (this.k == com.eric.revill.j.c.a && com.eric.revill.b.b.c()) {
            BlockPos first = com.eric.revill.b.b.a().c();
            BlockPos second = com.eric.revill.b.b.a().d();
            int centerX = (first.getX() + second.getX()) / 2;
            int centerY = (first.getY() + second.getY()) / 2;
            int centerZ = (first.getZ() + second.getZ()) / 2;
            com.eric.revill.t.b.a(server, new BlockPos(centerX, centerY, centerZ), com.eric.revill.b.b.a().b());
         }

         this.g(server);
         com.eric.revill.p.a.a(server, true, this.f, true, true);
         this.e(server);
      }
   }

   /**
    * Detiene o cancela el Momento Revill de forma inmediata.
    * 
    * @param server Servidor de Minecraft
    * @param cancelled True si fue cancelado manualmente por un comando de admin
    */
   public void a(MinecraftServer server, boolean cancelled) {
      boolean cancelledRoulette = com.eric.revill.g.a.c(server);
      if (this.e || this.i || cancelledRoulette) {
         this.e = false;
         this.i = true;
         this.g = false;
         this.h = false;
         this.m.clear();
         this.c.a();
         if (server != null) {
            com.eric.revill.t.b.a(server, "bunker");
            com.eric.revill.p.a.a(server, false, 0, false, false);
            if (cancelled) {
               this.b(server, "El Evento Rojo ha sido cancelado.");
            }
         }

         this.e(server);
      }
   }

   /**
    * Tick periódico ejecutado en cada ciclo del servidor.
    * Actualiza la cuenta atrás cada 20 ticks, comprueba jugadores a salvo y genera meteoritos.
    */
   public void c(MinecraftServer server) {
      if (server != null && this.e) {
         this.j++;
         if (this.j % 20 == 0) {
            this.f = Math.max(0, this.f - 1);
            this.a(server, this.f);
            com.eric.revill.p.a.a(server, true, this.f, this.g, this.h);
            if (this.f <= 0) {
               this.h(server);
               return;
            }
         }

         for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            if (!player.isSpectator() && this.k.a(player) && this.m.add(player.getUUID())) {
               this.b(server, this.k.c(player.getGameProfile().getName()));
            }
         }

         if (server.overworld() != null) {
            this.c.a(server.overworld(), this.f, this.b);
         }
      }
   }

   /** Imprime el estado detallado del evento al emisor de un comando */
   public void a(CommandSourceStack source) {
      source.sendSuccess(
         () -> Component.literal(
               "Evento activo: "
                  + (this.e ? "sí" : "no")
                  + "\nTiempo restante: "
                  + this.a(this.f)
                  + "\nMeteoritos activos: "
                  + this.c.b()
                  + "\nBúnker configurado: "
                  + (com.eric.revill.b.b.c() ? "sí" : "no")
                  + "\nCielo rojo: "
                  + (this.g ? "activo" : "inactivo")
            ),
         false
      );
   }

   /** Carga el estado guardado del evento desde el almacenamiento del mundo (SavedData) */
   public void d(MinecraftServer server) {
      if (server != null) {
         DimensionDataStorage manager = server.overworld().getDataStorage();
         b state = (b)manager.computeIfAbsent(com.eric.revill.j.b.FACTORY, "revill_event");
         this.e = state.a();
         this.f = state.b();
         this.g = state.c();
         this.h = state.d();
         this.l = this.k = com.eric.revill.j.c.d(state.e());
         com.eric.revill.b.b.a(state);
         if (this.e) {
            com.eric.revill.p.a.a(server, true, this.f, this.g, this.h);
         }
      }
   }

   /** Guarda el estado actual del evento en el almacenamiento persistente del mundo */
   public void e(MinecraftServer server) {
      if (server != null && server.overworld() != null) {
         DimensionDataStorage manager = server.overworld().getDataStorage();
         b state = (b)manager.computeIfAbsent(com.eric.revill.j.b.FACTORY, "revill_event");
         state.a(this.e);
         state.a(this.f);
         state.b(this.g);
         state.c(this.h);
         state.a(this.k.b());
         com.eric.revill.b.b.a(state);
         state.b(server.overworld().dimension().location().toString());
         state.setDirty();
      }
   }

   public void f(MinecraftServer server) {
      this.e(server);
   }

   /** Inicia la reproducción de la música de tensión en todos los clientes */
   private void g(MinecraftServer server) {
      if (server != null && this.b.h) {
         this.h = true;
         com.eric.revill.p.a.a(server, this.b.i, this.b.l);
      }
   }

   /** Emite avisos sonoros y en el chat en hitos de tiempo clave (10m, 5m, 2m, 1m, 30s, 10s) */
   private void a(MinecraftServer server, int seconds) {
      if (seconds == 600 || seconds == 300 || seconds == 120 || seconds == 60 || seconds == 30 || seconds == 10) {
         String text = switch (seconds) {
            case 10 -> "10 segundos.";
            case 30 -> "Quedan 30 segundos.";
            case 60 -> "Queda 1 minuto.";
            case 120 -> "Quedan 2 minutos.";
            case 300 -> "Quedan 5 minutos.";
            case 600 -> "Quedan 10 minutos.";
            default -> "";
         };
         if (!text.isEmpty()) {
            this.b(server, text);
         }
      }
   }

   /**
    * Conclusión del evento al agotarse el tiempo:
    * Todos los jugadores que no hayan alcanzado el objetivo son eliminados (player.kill()).
    */
   private void h(MinecraftServer server) {
      this.e = false;
      this.i = true;
      this.g = false;
      this.h = false;
      this.c.a();
      if (server != null) {
         for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            if (!this.k.a(player) && !this.m.contains(player.getUUID())) {
               player.kill();
            }
         }

         this.b(server, "Final del Momento Revill. Objetivo: " + this.k.d() + ". Quienes no lo completaron han sido eliminados.");
         com.eric.revill.p.a.a(server, false, 0, false, false);
      }

      this.e(server);
   }

   /** Envía un mensaje con el icono estilizado de DEDSAFIO 4 a todos los jugadores */
   private void b(MinecraftServer server, String message) {
      if (server != null) {
         Component text = com.eric.revill.c.a.a(Component.literal(message), com.eric.revill.c.a.Icon.c);

         for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            player.displayClientMessage(text, false);
         }
      }
   }

   /** Formatea segundos en formato MM:SS */
   private String a(int seconds) {
      int totalMinutes = seconds / 60;
      int remainingSeconds = seconds % 60;
      return String.format("%02d:%02d", totalMinutes, remainingSeconds);
   }
}
