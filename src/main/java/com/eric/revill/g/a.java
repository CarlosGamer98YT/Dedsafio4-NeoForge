package com.eric.revill.g;

import com.eric.revill.RevillMod;
import com.eric.revill.l.g;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import com.eric.revill.fabric.fabric.api.networking.v1.PacketByteBufs;
import com.eric.revill.fabric.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec3;

public final class a {
   private static final Set<UUID> a = new HashSet<>();
   private static final Set<UUID> b = new HashSet<>();
   private static final int c = 280;
   private static int d;
   private static String e;
   private static String f;
   private static String g;
   private static String h;
   private static boolean i;
   private static boolean j;
   private static boolean k;
   private static final Map<String, Boolean> l = new HashMap<>();
   private static final List<a.DifficultyChange> m = b();
   private static final String[] n = new String[]{"roja", "red", "verde", "green", "naranja", "orange", "rosada", "rosa", "pink"};

   private a() {
   }

   public static List<a.DifficultyChange> a() {
      return new ArrayList<>(m);
   }

   public static List<a.DifficultyChange> a(int day) {
      ArrayList<a.DifficultyChange> result = new ArrayList<>();

      for (a.DifficultyChange change : m) {
         if (change.b() == day) {
            result.add(change);
         }
      }

      return result;
   }

   public static boolean a(String id) {
      return l.getOrDefault(id, false);
   }

   public static void a(String id, boolean enabled) {
      if (f(id) != null) {
         l.put(id, enabled);
      }
   }

   public static void a(MinecraftServer server, String id, boolean enabled) {
      a.DifficultyChange change = f(id);
      if (server != null && change != null) {
         l.put(id, enabled);
         if (enabled && "dia6_animales_extintos".equals(id)) {
            com.eric.revill.g.c.a(server);
         }

         for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            player.displayClientMessage(Component.literal((enabled ? "Cambio activado: " : "Cambio desactivado: ") + change.c()), true);
         }

         a(server);
      }
   }

   public static void a(MinecraftServer server) {
      for (ServerPlayer player : server.getPlayerList().getPlayers()) {
         FriendlyByteBuf buf = PacketByteBufs.create();
         a(buf);
         ServerPlayNetworking.send(player, com.eric.revill.g.b.d, buf);
      }
   }

   public static void a(ServerPlayer player) {
      if (player != null) {
         FriendlyByteBuf buf = PacketByteBufs.create();
         a(buf);
         ServerPlayNetworking.send(player, com.eric.revill.g.b.d, buf);
      }
   }

   private static void a(FriendlyByteBuf buf) {
      buf.writeVarInt(m.size());

      for (a.DifficultyChange change : m) {
         buf.writeUtf(change.a(), 64);
         buf.writeBoolean(a(change.a()));
      }
   }

   public static void b(MinecraftServer server, String id, boolean enabled) {
      a.DifficultyChange change = f(id);
      if (server != null && change != null) {
         a(server, id, enabled);
      }
   }

   public static void b(String id) {
      l.put(id, false);
   }

   public static void b(ServerPlayer player) {
      if (player != null) {
         if (!player.hasPermissions(4)) {
            player.displayClientMessage(Component.literal("Necesitas ser operador para abrir el panel de DEDSAFIO 4."), false);
         } else {
            ServerPlayNetworking.send(player, com.eric.revill.g.b.a, PacketByteBufs.empty());
            a(player.getServer());
            FriendlyByteBuf settings = PacketByteBufs.create();
            settings.writeVarInt(Math.max(1, Math.min(86400, RevillMod.c.d)));
            settings.writeBoolean(RevillMod.c.revillMusicEnabled);
            Map<String, String> messages = RevillMod.c.revillTaskMessages;
            settings.writeVarInt(messages == null ? 0 : messages.size());
            if (messages != null) {
               messages.forEach((taskId, message) -> {
                  settings.writeUtf(taskId, 64);
                  settings.writeUtf(message, 240);
               });
            }

            ServerPlayNetworking.send(player, com.eric.revill.g.b.n, settings);
         }
      }
   }

   public static void a(MinecraftServer server, String id) {
      a.DifficultyChange change = f(id);
      if (change != null) {
         h = change.a();
         String announcementText = change.d() != null && !change.d().isBlank() ? change.d() : change.f();
         b(server, "naranja", announcementText);
      }
   }

   public static void b(MinecraftServer server, String rawColor) {
      a(server, rawColor, "La ruleta ha elegido esta zona.", false);
   }

   public static void a(MinecraftServer server, String rawColor, String resultText) {
      a(server, rawColor, resultText, false);
   }

   public static void b(MinecraftServer server) {
      c(server, null);
   }

   public static void c(MinecraftServer server, String customMessage) {
      a(server, com.eric.revill.j.c.a, customMessage);
   }

   public static void a(MinecraftServer server, com.eric.revill.j.c task, String customMessage) {
      a(server, "roja", task, customMessage);
   }

   public static void a(MinecraftServer server, String color, com.eric.revill.j.c task, String customMessage) {
      com.eric.revill.j.c selectedTask = task == null ? com.eric.revill.j.c.a : task;
      a(server, color, selectedTask.b(customMessage), true);
   }

   private static void a(MinecraftServer server, String rawColor, String resultText, boolean launchRevill) {
      if (server != null) {
         String color = g(rawColor);
         if (!i(color)) {
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
               player.displayClientMessage(Component.literal("Color no válido. Usa uno de: roja, verde, naranja, rosada."), false);
            }
         } else {
            color = h(color);
            b.clear();

            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
               b.add(player.getUUID());
               a(player, "ruleta");
            }

            e = color;
            f = resultText;
            i = launchRevill;
            j = false;
            k = false;
            g = null;
            d = 280;
            com.eric.revill.p.a.a(server, color, resultText);
         }
      }
   }

   public static boolean a(ServerPlayer player, DamageSource source, float amount) {
      if (player != null && !player.isSpectator()) {
         Vec3 pos = player.position();
         ServerLevel world = player.serverLevel();
         String reason = a(source);
         com.eric.revill.l.g.b(player);
         a.add(player.getUUID());
         player.setGameMode(GameType.SPECTATOR);
         player.setHealth(player.getMaxHealth());
         player.getAbilities().flying = false;
         player.clearFire();
         player.fallDistance = 0.0F;
         player.getFoodData().setFoodLevel(20);
         if (world != null) {
            player.teleportTo(world, pos.x, pos.y, pos.z, player.getYRot(), player.getXRot());
         }

         a(player, "muerte");
         MinecraftServer server = player.getServer();
         if (server != null) {
            server.execute(() -> ServerPlayNetworking.send(player, com.eric.revill.g.b.i, PacketByteBufs.empty()));
         } else {
            ServerPlayNetworking.send(player, com.eric.revill.g.b.i, PacketByteBufs.empty());
         }

         for (ServerPlayer viewer : player.getServer().getPlayerList().getPlayers()) {
            FriendlyByteBuf buf = PacketByteBufs.create();
            buf.writeUtf(player.getGameProfile().getName());
            buf.writeUtf(reason);
            buf.writeBoolean(viewer.getUUID().equals(player.getUUID()));
            ServerPlayNetworking.send(viewer, com.eric.revill.g.b.j, buf);
         }

         return true;
      } else {
         return false;
      }
   }

   public static boolean c(MinecraftServer server) {
      if ((d <= 0 || !i) && !k) {
         return false;
      } else {
         d = 0;
         e = null;
         f = null;
         i = false;
         k = false;
         com.eric.revill.p.a.a(server);
         return true;
      }
   }

   public static void d(MinecraftServer server) {
      if (server != null) {
         if (d > 0 && --d == 0) {
            h(server);
         }

         for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            if (a.contains(player.getUUID())) {
               if (player.isDeadOrDying() || player.getHealth() <= 0.0F) {
                  c(player);
               } else if (player.gameMode.getGameModeForPlayer() != GameType.SPECTATOR) {
                  player.setGameMode(GameType.SPECTATOR);
               }
            }
         }
      }
   }

   public static void e(MinecraftServer server) {
      b.clear();
      a.clear();
      if (server != null) {
         for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            if (player.isSpectator()) {
               player.setGameMode(GameType.SURVIVAL);
            }
         }
      }
   }

   public static void c(ServerPlayer player) {
      if (player != null) {
         a.add(player.getUUID());
         player.setHealth(player.getMaxHealth());
         player.getAbilities().flying = false;
         player.setGameMode(GameType.SPECTATOR);
         player.clearFire();
         player.fallDistance = 0.0F;
         player.getFoodData().setFoodLevel(20);
      }
   }

   public static void a(ServerPlayer player, GameType mode) {
      if (player != null) {
         a.remove(player.getUUID());
         if (mode != null) {
            player.setGameMode(mode);
         }
      }
   }

   private static void a(MinecraftServer server, a.DifficultyChange change) {
      if (server != null && change != null) {
         l.put(change.a(), true);
         a(server);
         a(server);
         if ("dia6_animales_extintos".equals(change.a())) {
            com.eric.revill.g.c.a(server);
         }

         for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            player.displayClientMessage(
               Component.literal("Cambio aplicado: " + change.c()).withStyle(style -> style.withBold(true).withColor(TextColor.fromRgb(16766282))), false
            );
         }
      }
   }

   private static String a(DamageSource source) {
      if (source == null) {
         return "muerte";
      } else if (source.is(DamageTypes.DROWN)) {
         return "ahogamiento";
      } else if (source.is(DamageTypes.FELL_OUT_OF_WORLD)) {
         return "caída del mundo";
      } else {
         String name = source.getMsgId();
         if (name != null && !name.isBlank()) {
            String lower = name.toLowerCase(Locale.ROOT);
            if (lower.contains("suffocate") || lower.contains("wall") || lower.contains("asfix")) {
               return "asfixia";
            } else {
               return !lower.contains("fire") && !lower.contains("lava") && !lower.contains("burn") ? name : "fuego";
            }
         } else {
            return "muerte";
         }
      }
   }

   private static void b(MinecraftServer server, String color, String text) {
      a(server, color, text, false);
   }

   private static void h(MinecraftServer server) {
      String color = e;
      String result = f;
      boolean launchRevill = i;
      e = null;
      f = null;
      i = false;
      if (color != null && result != null) {
         String displayText = e(result);
         if ("rosada".equals(color) && !launchRevill) {
            g = displayText;
            j = true;
            com.eric.revill.p.a.b(server);
         } else {
            com.eric.revill.j.c revillTask = launchRevill ? com.eric.revill.j.a.a.b() : null;
            Component announcementMessage = a(color, displayText, launchRevill, revillTask);
            Component announcement = com.eric.revill.c.a.a(announcementMessage, b(color, launchRevill));

            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
               player.displayClientMessage(announcement, false);
            }

            String cardMessage = b(color, displayText, launchRevill, revillTask);
            com.eric.revill.p.a.b(server, color, cardMessage);
         }

         if (h != null) {
            a.DifficultyChange queuedChange = f(h);
            if (queuedChange != null) {
               a(server, queuedChange);
            }

            h = null;
         }

         if (launchRevill) {
            k = true;
            com.eric.revill.p.a.c(server);
         }
      }
   }

   public static void f(MinecraftServer server) {
      String explanation = g;
      g = null;
      if (j && server != null && explanation != null) {
         j = false;
         Component chatMessage = a("rosada", explanation, false, null);
         Component announcement = com.eric.revill.c.a.a(chatMessage, com.eric.revill.c.a.Icon.e);

         for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            player.displayClientMessage(announcement, false);
         }

         com.eric.revill.p.a.b(server, "rosada", b("rosada", explanation, false, null));
      }
   }

   public static void g(MinecraftServer server) {
      if (k && server != null) {
         k = false;
         com.eric.revill.j.a.a.b(server);
      }
   }

   private static Component a(String color, String displayText, boolean isRevill, com.eric.revill.j.c task) {
      String body = d(displayText);
      ChatFormatting formatting = c(color);
      if ("verde".equals(color)) {
         return Component.literal(body).withStyle(formatting);
      } else {
         Object title = isRevill
            ? "Momento Revill: " + (task == null ? com.eric.revill.j.c.a.d() : task.d())
            : ("rosada".equals(color) ? "Momento nutria" : "Cambio de dificultad");
         return Component.literal("⩥ || " + (String)title + " ||")
            .withStyle(new ChatFormatting[]{formatting, ChatFormatting.BOLD})
            .append(Component.literal("\n\n"))
            .append(Component.literal(body).withStyle(formatting));
      }
   }

   private static com.eric.revill.c.a.Icon b(String color, boolean isRevill) {
      if (isRevill) {
         return com.eric.revill.c.a.Icon.c;
      } else if ("verde".equals(color)) {
         return com.eric.revill.c.a.Icon.d;
      } else {
         return "rosada".equals(color) ? com.eric.revill.c.a.Icon.e : com.eric.revill.c.a.Icon.c;
      }
   }

   private static String b(String color, String displayText, boolean isRevill, com.eric.revill.j.c task) {
      String body = d(displayText);
      if ("verde".equals(color)) {
         return body;
      } else {
         Object title = isRevill
            ? "Momento Revill: " + (task == null ? com.eric.revill.j.c.a.d() : task.d())
            : ("rosada".equals(color) ? "Momento nutria" : "Cambio de dificultad");
         return "⩥ || " + (String)title + " ||\n\n" + body;
      }
   }

   private static ChatFormatting c(String color) {
      return switch (color) {
         case "roja" -> ChatFormatting.RED;
         case "verde" -> ChatFormatting.GREEN;
         case "naranja" -> ChatFormatting.GOLD;
         case "rosada" -> ChatFormatting.LIGHT_PURPLE;
         default -> ChatFormatting.YELLOW;
      };
   }

   private static String d(String displayText) {
      String body = displayText == null ? "" : displayText.trim();
      body = body.replaceFirst("(?i)^ruleta\\s+[a-záéíóúüñ]+\\s*[-–:]?\\s*", "");
      body = body.replaceAll("§[0-9A-FK-ORa-fk-or]", "");
      body = body.replaceAll("\\s+", " ").trim();
      return body.isBlank()
         ? "El evento ha comenzado. Seguid las instrucciones y preparaos para sobrevivir. ADVERTENCIA: permaneced atentos al objetivo."
         : body;
   }

   private static String e(String result) {
      return result != null && !result.isBlank() ? result.trim() : "";
   }

   private static void a(ServerPlayer player, String soundName) {
      if (!"ruleta".equals(soundName) || RevillMod.c.revillMusicEnabled) {
         ResourceLocation id = ResourceLocation.fromNamespaceAndPath("revill", soundName);
         player.playNotifySound(SoundEvent.createVariableRangeEvent(id), SoundSource.MASTER, 1.0F, 1.0F);
      }
   }

   private static a.DifficultyChange f(String id) {
      for (a.DifficultyChange change : m) {
         if (change.a().equals(id)) {
            return change;
         }
      }

      return null;
   }

   private static String g(String rawColor) {
      return rawColor == null ? "" : rawColor.trim().toLowerCase(Locale.ROOT).replace("-", "").replace("_", "");
   }

   private static String h(String color) {
      return switch (color) {
         case "red" -> "roja";
         case "green" -> "verde";
         case "orange" -> "naranja";
         case "rosa", "pink" -> "rosada";
         default -> color;
      };
   }

   private static boolean i(String color) {
      for (String key : n) {
         if (key.equals(color)) {
            return true;
         }
      }

      return false;
   }

   private static List<a.DifficultyChange> b() {
      ArrayList<a.DifficultyChange> changes = new ArrayList<>();
      changes.add(
         new a.DifficultyChange(
            "dia1_creepers_electricos",
            1,
            "Creepers Eléctricos",
            "Revill ha electrificado a los Creepers! Los Creepers Normales han sido modificados por Creepers Eléctricos. Con el equipamiento que tenemos, sólo podemos sobrevivir a su explosión utilizando un escudo.",
            "naranja",
            "Ruleta Naranja - Cambio de dificultad"
         )
      );
      changes.add(
         new a.DifficultyChange(
            "dia1_ender_pearls_ignoran_armor",
            1,
            "Ender Pearls dañinas",
            "ReviiL corrompió la composición de las Ender Pearls. A partir de ahora, su daño ignora la armadura.",
            "naranja",
            "ReviiL corrompió la composición de las Ender Pearls. A partir de ahora, su daño ignora la armadura."
         )
      );
      changes.add(
         new a.DifficultyChange(
            "dia1_wardens_sculk",
            1,
            "Wardens dejan Sculk Shrieker",
            "ReviiL corrompió el alma de los Wardens. A partir de ahora, al morir dejan un Sculk Shrieker.",
            "naranja",
            "Cambio de dificultad — Wardens ahora dejan Sculk Shrieker al morir"
         )
      );
      changes.add(
         new a.DifficultyChange(
            "dia1_respirar_bajo_agua",
            1,
            "Respirar bajo el agua",
            "Revill alteró las corrientes de agua. Las puertas y trampillas ya no pueden utilizarse para respirar bajo el agua.",
            "naranja",
            "Cambio de dificultad — Respirar bajo el agua"
         )
      );
      changes.add(
         new a.DifficultyChange(
            "dia1_sin_golems",
            1,
            "Sin Gólems",
            "Revill lanzó una maldición que impide que las calabazas cobren vida. Los Gólems de Hierro y de Nieve ya no pueden ser invocados.",
            "naranja",
            "Cambio de dificultad — Sin Gólems de Hierro ni de Nieve"
         )
      );
      changes.add(
         new a.DifficultyChange(
            "dia1_no_leche",
            1,
            "Sin leche",
            "REviil ordena a todas las vacas y, a partir de ahora, los jugadores ya no pueden obtener leche de ellas.",
            "naranja",
            "Cambio de dificultad — Ya no se puede sacar leche a las vacas"
         )
      );
      changes.add(
         new a.DifficultyChange(
            "dia1_wardens_deep_dark",
            1,
            "Wardens Deep Dark",
            "REviil altera la genética de los Wardens. A partir de ahora pueden aparecer naturalmente en el Deep Dark.",
            "naranja",
            "Cambio de dificultad — Wardens en el Deep Dark"
         )
      );
      changes.add(
         new a.DifficultyChange(
            "dia1_arboles_sin_brotes",
            1,
            "Árboles sin brotes",
            "Los árboles dejan de soltar brotes, por lo que replantar ya no es una opción.",
            "naranja",
            "Cambio de dificultad — Los árboles dejan de soltar brotes"
         )
      );
      changes.add(
         new a.DifficultyChange(
            "dia2_zombies_trepadores",
            2,
            "Zombies trepadores",
            "Cuando los zombies aparecen hay un 50% de probabilidad de que puedan escalar paredes como las Arañas y sean inmunes al sol.",
            "naranja",
            "Cambio de dificultad — Zombies trepadores"
         )
      );
      changes.add(
         new a.DifficultyChange(
            "dia2_blazes_nether",
            2,
            "Blazes en el Nether",
            "El Inframundo no descansa. Ahora los Blaze aparecen naturalmente en el Nether.",
            "naranja",
            "Cambio de dificultad — Blazes en el Nether"
         )
      );
      changes.add(
         new a.DifficultyChange(
            "dia3_ender_pearls_damage10",
            3,
            "Ender Pearls más dañinas",
            "ReviiL ha utilizado el poder de la Ruleta para corromper la composición de las Ender Pearls. Ahora su daño aumentó a 10.",
            "naranja",
            "Cambio de dificultad — Ender Pearls más dañinas"
         )
      );
      changes.add(
         new a.DifficultyChange(
            "dia3_magma_letal",
            3,
            "Bloques de Magma letales",
            "El calor de las capas bajas es insoportable. Ahora los Bloques de Magma hacen siete mil millones de cuatrillones de daño. El daño también se aplica al tocar cualquiera de las caras del bloque vertical u horizontalmente.",
            "naranja",
            "Cambio de dificultad — Bloques de Magma letales"
         )
      );
      changes.add(
         new a.DifficultyChange(
            "dia4_aranas_cueva",
            4,
            "Arañas de Cueva",
            "Al romper una Tela de Araña existe la probabilidad de que salga una Araña de Cueva.",
            "naranja",
            "Cambio de dificultad — Arañas de Cueva al romper telarañas"
         )
      );
      changes.add(
         new a.DifficultyChange(
            "dia4_zombies_mejorados",
            4,
            "Zombies mejorados",
            "ReviiL está experimentando con los Zombies. Ahora todos pueden escalar, no se queman con el sol y spaunean con una Espada de Hierro con Filo V.",
            "naranja",
            "Cambio de dificultad — Zombies mejorados"
         )
      );
      changes.add(
         new a.DifficultyChange(
            "dia5_zombies_nether",
            5,
            "Zombies en el Nether",
            "Ahora los Zombies aparecen en el Nether, sin importar la luz.",
            "naranja",
            "Cambio de dificultad — Zombies en el Nether"
         )
      );
      changes.add(
         new a.DifficultyChange(
            "dia5_bogged_noche",
            5,
            "Esqueletos Bogged",
            "A partir de este momento los Esqueletos Bogged (veneno) aparecen durante la noche.",
            "naranja",
            "Cambio de dificultad — Esqueletos Bogged durante la noche"
         )
      );
      changes.add(
         new a.DifficultyChange(
            "dia5_stray_noche",
            5,
            "Esqueletos Stray",
            "A partir de este momento los Esqueletos Stray pueden salir durante la noche en el Overworld.",
            "naranja",
            "Cambio de dificultad — Esqueletos Stray en el Overworld"
         )
      );
      changes.add(
         new a.DifficultyChange(
            "dia5_luna_mata",
            5,
            "Mirar la Luna mata",
            "Si alguien mira a la Luna directamente recibirá daño instantáneamente y morirá. Este cambio permanecerá activo hasta que alguien muera por esa razón.",
            "naranja",
            "Cambio de dificultad — Mirar la Luna causa la muerte"
         )
      );
      changes.add(
         new a.DifficultyChange(
            "dia6_wither_skeletons_nether",
            6,
            "Wither Skeleton en el Nether",
            "Los Wither Skeleton ahora aparecen naturalmente en el Nether.",
            "naranja",
            "Cambio de dificultad — Wither Skeleton en el Nether"
         )
      );
      changes.add(
         new a.DifficultyChange(
            "dia6_fuego_lava_triple",
            6,
            "Daño de Lava y Fuego",
            "La Lava y el Fuego han triplicado su daño.",
            "naranja",
            "Cambio de dificultad — Daño de Lava y Fuego"
         )
      );
      changes.add(
         new a.DifficultyChange(
            "dia6_animales_extintos",
            6,
            "Extinción de los animales",
            "ReviiL ha lanzado un gas tóxico que afecta a los animales. Los animales fueron extintos. Ningún animal volverá a aparecer.",
            "naranja",
            "Cambio de dificultad — Extinción de los animales"
         )
      );
      changes.add(
         new a.DifficultyChange(
            "dia7_aranas_cueva_overworld",
            7,
            "Arañas de cueva en el Overworld",
            "Las arañas de cueva aparecen durante la noche en el Overworld y todas las arañas tienen Velocidad I infinita.",
            "naranja",
            "Cambio de dificultad — Arañas de Cueva en el Overworld"
         )
      );
      changes.add(
         new a.DifficultyChange(
            "dia7_aranas_fuerza",
            7,
            "Arañas con Fuerza III",
            "Las arañas y las arañas de cueva tienen Fuerza III.",
            "naranja",
            "Cambio de dificultad — Arañas con Fuerza III"
         )
      );
      changes.add(
         new a.DifficultyChange(
            "dia7_dano_caida_doble",
            7,
            "Daño de caída x2",
            "El daño de caída se ha multiplicado x2.",
            "naranja",
            "Cambio de dificultad — El Daño de Caída ha sido multiplicado x2."
         )
      );
      changes.add(
         new a.DifficultyChange(
            "dia7_sin_absorcion",
            7,
            "Sin Absorción",
            "Las manzanas doradas y encantadas ya no otorgan Absorción.",
            "naranja",
            "Cambio de dificultad — Sin Absorción"
         )
      );
      changes.add(
         new a.DifficultyChange(
            "dia7_ahogamiento_aumentado",
            7,
            "Ahogamiento aumentado",
            "Pierdes la respiración 5 veces más rápido y el daño por ahogamiento se multiplica x10.",
            "naranja",
            "Cambio de dificultad — Ahogamiento aumentado"
         )
      );
      changes.add(
         new a.DifficultyChange(
            "dia7_dano_placa_presion",
            7,
            "Daño de placa de presión",
            "Interactuar con una placa de presión inflige 1 billón de daño.",
            "naranja",
            "Cambio de dificultad — Daño de Placa de Presión"
         )
      );
      changes.add(
         new a.DifficultyChange(
            "dia7_dano_boton",
            7,
            "Daño de botón",
            "Interactuar con un botón inflige 4 mil millones de millones de millones de millones de daño.",
            "naranja",
            "Cambio de dificultad — Daño de Botón"
         )
      );
      changes.add(
         new a.DifficultyChange(
            "dia7_fuego_lava_x10",
            7,
            "Daño de fuego y lava x10",
            "El daño del fuego y de la lava se multiplica x10.",
            "naranja",
            "Cambio de dificultad — Daño de Fuego y Lava multiplicado"
         )
      );
      changes.add(
         new a.DifficultyChange(
            "dia7_fuego_infinito_nether",
            7,
            "Fuego infinito en el Nether",
            "Si te quemas en el Nether, el fuego es infinito.",
            "naranja",
            "Cambio de dificultad — Fuego Infinito en el Nether"
         )
      );
      changes.add(
         new a.DifficultyChange(
            "dia7_sin_regeneracion_natural",
            7,
            "Regeneración natural desactivada",
            "La regeneración natural queda desactivada mientras este cambio esté activo.",
            "naranja",
            "Cambio de dificultad — Regeneración Natural desactivada"
         )
      );
      changes.add(
         new a.DifficultyChange(
            "dia7_aranas_telaranas",
            7,
            "Arañas dejan telarañas",
            "Las arañas y arañas de cueva dejan una telaraña al golpearte y al morir.",
            "naranja",
            "Cambio de dificultad — Arañas dejan Tela de Araña"
         )
      );
      changes.add(
         new a.DifficultyChange(
            "dia7_mobs_detectan_invisibles",
            7,
            "Criaturas detectan invisibles",
            "Las criaturas pueden detectar y atacar a entidades invisibles.",
            "naranja",
            "Cambio de dificultad — Criaturas detectan entidades invisibles"
         )
      );
      return changes;
   }

   public static record DifficultyChange(String a, int b, String c, String d, String e, String f) {
   }
}
