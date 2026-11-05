package com.eric.revill.d.a;

/**
 * Temporizador y gestor de ticks de cliente para animaciones.
 * 
 * Proporciona un contador de tiempo continuo sincronizado con el renderizado de Minecraft
 * para coordinar efectos animados independientes de los ticks del servidor.
 */

import com.eric.revill.RevillClient;
import com.eric.revill.RevillMod;
import com.eric.revill.d.b.h;
import com.eric.revill.l.g;
import com.eric.revill.m.e;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import com.eric.revill.fabric.api.EnvType;
import com.eric.revill.fabric.api.Environment;
import com.eric.revill.fabric.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

@Environment(EnvType.CLIENT)
public final class d {
   private d() {
   }

   public static void a() {
      ClientPlayNetworking.registerGlobalReceiver(com.eric.revill.g.b.l, (client, handler, buf, responseSender) -> {
         UUID playerId = UUID.fromString(buf.readUtf(36));
         String message = buf.readUtf();
         client.execute(() -> com.eric.revill.d.d.a.a.a(playerId, message));
      });
      ClientPlayNetworking.registerGlobalReceiver(com.eric.revill.g.b.m, (client, handler, buf, responseSender) -> {
         UUID playerId = UUID.fromString(buf.readUtf(36));
         client.execute(() -> com.eric.revill.d.d.a.a.a(playerId));
      });
      ClientPlayNetworking.registerGlobalReceiver(com.eric.revill.p.a.a, (client, handler, buf, responseSender) -> {
         boolean active = buf.readBoolean();
         int remainingSeconds = buf.readInt();
         boolean skyActive = buf.readBoolean();
         boolean announcementActive = buf.readBoolean();
         client.execute(() -> {
            if (active) {
               com.eric.revill.d.c.a.a();
            } else {
               com.eric.revill.d.c.a.b();
            }

            if (active && skyActive) {
               RevillClient.a.a(true);
               RevillClient.e.a();
            } else {
               RevillClient.e.a(RevillClient.a);
            }

            if (!active || !announcementActive) {
               RevillClient.b.b();
            } else if (!RevillClient.b.c()) {
               RevillClient.b.a(true);
            }

            RevillClient.c.a(active, remainingSeconds);
         });
      });
      ClientPlayNetworking.registerGlobalReceiver(com.eric.revill.p.a.b, (client, handler, buf, responseSender) -> {
         int durationSeconds = buf.readInt();
         double flashSpeed = buf.readDouble();
         client.execute(() -> {
            RevillClient.b.a(durationSeconds);
            RevillClient.b.a(flashSpeed);
            RevillClient.b.a();
         });
      });
      ClientPlayNetworking.registerGlobalReceiver(com.eric.revill.p.a.c, (client, handler, buf, responseSender) -> {
         buf.readBoolean();
         client.execute(() -> {
            com.eric.revill.d.c.a.b();
            RevillClient.e.a(RevillClient.a);
            RevillClient.b.b();
            RevillClient.c.a(false, 0);
            RevillClient.c.a();
         });
      });
      ClientPlayNetworking.registerGlobalReceiver(com.eric.revill.p.a.d, (client, handler, buf, responseSender) -> {
         String color = buf.readUtf();
         String result = buf.readUtf();
         client.execute(() -> RevillClient.c.a(color, result));
      });
      ClientPlayNetworking.registerGlobalReceiver(com.eric.revill.p.a.j, (client, handler, buf, responseSender) -> {
         int action = buf.readVarInt();
         client.execute(() -> {
            switch (action) {
               case 0:
                  RevillClient.a.a(true);
                  break;
               case 1:
                  RevillClient.a.a(true);
                  RevillClient.e.a();
                  break;
               case 2:
                  RevillClient.e.b();
                  break;
               case 3:
                  RevillClient.a.a(false);
                  break;
               default:
                  RevillMod.b.warn("Acción visual REVILL desconocida recibida: {}", action);
            }
         });
      });
      ClientPlayNetworking.registerGlobalReceiver(com.eric.revill.p.a.e, (client, handler, buf, responseSender) -> {
         String color = buf.readUtf(32);
         String message = buf.readUtf(32767);
         client.execute(() -> RevillClient.c.b(message, color));
      });
      ClientPlayNetworking.registerGlobalReceiver(com.eric.revill.p.a.f, (client, handler, buf, responseSender) -> client.execute(() -> RevillClient.c.b()));
      ClientPlayNetworking.registerGlobalReceiver(com.eric.revill.p.a.h, (client, handler, buf, responseSender) -> client.execute(() -> RevillClient.c.d()));
      ClientPlayNetworking.registerGlobalReceiver(com.eric.revill.g.b.d, (client, handler, buf, responseSender) -> {
         int count = Math.min(buf.readVarInt(), 256);
         HashMap<String, Boolean> states = new HashMap<>();

         for (int index = 0; index < count; index++) {
            states.put(buf.readUtf(64), buf.readBoolean());
         }

         client.execute(() -> states.forEach(com.eric.revill.g.a::a));
      });
      ClientPlayNetworking.registerGlobalReceiver(e.g, (client, handler, buf, responseSender) -> {
         int syncId = buf.readVarInt();
         List<com.eric.revill.m.c.a> mails = e.a(buf);
         client.execute(() -> {
            if (client.player != null) {
               AbstractContainerMenu patt6523$temp = client.player.containerMenu;
               if (client.player.containerMenu instanceof com.eric.revill.m.c) {
                  com.eric.revill.m.c inbox = (com.eric.revill.m.c)patt6523$temp;
                  if (inbox.containerId == syncId) {
                     inbox.a(mails);
                  }
               }
            }
         });
      });
      ClientPlayNetworking.registerGlobalReceiver(e.h, (client, handler, buf, responseSender) -> {
         int unreadCount = buf.readVarInt();
         client.execute(() -> h.a(unreadCount));
      });
      ClientPlayNetworking.registerGlobalReceiver(com.eric.revill.g.b.j, (client, handler, buf, responseSender) -> {
         String playerName = buf.readUtf();
         String reason = buf.readUtf();
         boolean self = buf.readBoolean();
         client.execute(() -> RevillClient.c.a(playerName, reason, self));
      });
      ClientPlayNetworking.registerGlobalReceiver(com.eric.revill.p.a.k, (client, handler, buf, responseSender) -> {
         String itemId = buf.readUtf(128);
         client.execute(() -> {
            ResourceLocation id = ResourceLocation.tryParse(itemId);
            if (id != null) {
               Item item = (Item)BuiltInRegistries.ITEM.get(id);
               if (item != Items.AIR && g.a(new ItemStack(item))) {
                  RevillClient.setCustomTotemPop(new ItemStack(item));
               }
            }
         });
      });
      ClientPlayNetworking.registerGlobalReceiver(com.eric.revill.g.b.n, (client, handler, buf, responseSender) -> {
         int duration = buf.readVarInt();
         boolean musicEnabled = buf.readBoolean();
         int count = Math.min(buf.readVarInt(), 3);
         HashMap<String, String> messages = new HashMap<>();

         for (int index = 0; index < count; index++) {
            messages.put(buf.readUtf(64), buf.readUtf(240));
         }

         client.execute(() -> {
            Screen screen = client.screen;
            if (screen instanceof com.eric.revill.d.e.h) {
               ((com.eric.revill.d.e.h)screen).applyRevillSettings(duration, musicEnabled, messages);
            } else {
               RevillMod.c.d = Math.max(1, Math.min(86400, duration));
               RevillMod.c.revillMusicEnabled = musicEnabled;
               if (RevillMod.c.revillTaskMessages == null) {
                  RevillMod.c.revillTaskMessages = new HashMap<>();
               }

               RevillMod.c.revillTaskMessages.clear();
               RevillMod.c.revillTaskMessages.putAll(messages);
            }
         });
      });
      ClientPlayNetworking.registerGlobalReceiver(com.eric.revill.g.b.k, (client, handler, buf, responseSender) -> {
         boolean enabled = buf.readBoolean();
         client.execute(() -> RevillClient.d.a(enabled));
      });
      ClientPlayNetworking.registerGlobalReceiver(com.eric.revill.t.b.a, (client, handler, buf, responseSender) -> {
         List<com.eric.revill.t.a> waypoints = com.eric.revill.t.b.a(buf);
         client.execute(() -> com.eric.revill.d.b.c.a(waypoints));
      });
   }
}
