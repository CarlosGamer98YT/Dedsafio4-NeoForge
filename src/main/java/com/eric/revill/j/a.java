package com.eric.revill.j;

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
   public static final a a = new a();
   private final com.eric.revill.f.a b = RevillMod.c;
   private final com.eric.revill.n.b c = new com.eric.revill.n.b();
   private final Set<ServerPlayer> d = new HashSet<>();
   private boolean e;
   private int f = 600;
   private boolean g;
   private boolean h;
   private boolean i;
   private int j;
   private c k = com.eric.revill.j.c.a;
   private c l = com.eric.revill.j.c.a;
   private final Set<UUID> m = new HashSet<>();

   public com.eric.revill.f.a a() {
      return this.b;
   }

   public void a(MinecraftServer server) {
      this.a(server, com.eric.revill.j.c.a.b(), null);
   }

   public void a(MinecraftServer server, String rouletteMessage) {
      this.a(server, com.eric.revill.j.c.a.b(), rouletteMessage);
   }

   public void a(MinecraftServer server, String taskId, String additionalMessage) {
      this.a(server, taskId, additionalMessage, "roja", this.b.d, this.b.revillMusicEnabled);
   }

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

   public c b() {
      return this.l;
   }

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

   private void g(MinecraftServer server) {
      if (server != null && this.b.h) {
         this.h = true;
         com.eric.revill.p.a.a(server, this.b.i, this.b.l);
      }
   }

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

   private void b(MinecraftServer server, String message) {
      if (server != null) {
         Component text = com.eric.revill.c.a.a(Component.literal(message), com.eric.revill.c.a.Icon.c);

         for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            player.displayClientMessage(text, false);
         }
      }
   }

   private String a(int seconds) {
      int totalMinutes = seconds / 60;
      int remainingSeconds = seconds % 60;
      return String.format("%02d:%02d", totalMinutes, remainingSeconds);
   }
}
