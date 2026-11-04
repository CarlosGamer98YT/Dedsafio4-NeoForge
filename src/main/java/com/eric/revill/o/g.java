package com.eric.revill.o;

import com.eric.revill.RevillMod;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.saveddata.SavedData;
import net.neoforged.fml.loading.FMLPaths;

public final class g {
   public static final int a = 100;
   private static final int b = 65536;
   private static final int c = 1000000;
   private static final String d = "dedsafio4_missions";
   private static final Gson e = new GsonBuilder().setPrettyPrinting().create();
   private static final Path f = FMLPaths.CONFIGDIR.get().resolve("dedsafio4-missions.json");
   private static final Map<String, f> g = new LinkedHashMap<>();

   private g() {
   }

   public static synchronized List<f> a() {
      return List.copyOf(g.values());
   }

   public static synchronized void a(MinecraftServer server) {
      try {
         if (!Files.exists(f)) {
            b(b());
         }

         long size;
         if ((size = Files.size(f)) > 0L && size <= 65536L) {
            String json = Files.readString(f, StandardCharsets.UTF_8);
            JsonObject root = JsonParser.parseString(json).getAsJsonObject();
            if (!root.keySet().equals(Set.of("missions"))) {
               throw new IllegalArgumentException("El archivo debe contener únicamente la propiedad 'missions'.");
            } else {
               JsonArray entries = root.getAsJsonArray("missions");
               if (entries != null && entries.size() <= 100) {
                  ArrayList<f> definitions = new ArrayList<>();

                  for (int index = 0; index < entries.size(); index++) {
                     try {
                        definitions.add(a(entries.get(index).getAsJsonObject()));
                     } catch (RuntimeException var9) {
                        throw new IllegalArgumentException("Misión en índice " + index + ": " + var9.getMessage(), var9);
                     }
                  }

                  b(server, definitions);
                  b(server);
               } else {
                  throw new IllegalArgumentException("La lista debe contener como máximo 100 misiones.");
               }
            }
         } else {
            throw new IllegalArgumentException("El archivo de misiones debe ocupar entre 1 y 65536 bytes.");
         }
      } catch (RuntimeException | IOException var10) {
         throw new IllegalStateException("No se pudieron cargar las misiones desde " + f + ": " + var10.getMessage(), var10);
      }
   }

   public static void a(MinecraftServer server, ServerPlayer player) {
      g.MissionState state = b(server);
      g.b progress = state.a.computeIfAbsent(player.getUUID(), ignored -> new g.b());
      progress.a = player.getName().getString();
      state.setDirty();
      a(player);
   }

   public static void a(ServerPlayer player, LivingEntity victim) {
      if (player != null && player.getServer() != null) {
         a(player, "KILL", BuiltInRegistries.ENTITY_TYPE.getKey(victim.getType()).toString(), 1);
      }
   }

   public static void a(Player player, Block block) {
      if (player instanceof ServerPlayer serverPlayer) {
         a(serverPlayer, "BREAK", BuiltInRegistries.BLOCK.getKey(block).toString(), 1);
      }
   }

   public static void a(Player player, Item item, int count) {
      if (player instanceof ServerPlayer serverPlayer && count > 0) {
         a(serverPlayer, "COLLECT", BuiltInRegistries.ITEM.getKey(item).toString(), count);
      }
   }

   public static int a(MinecraftServer server, UUID playerId, String missionId) {
      g.b progress = b(server).a.get(playerId);
      return progress == null ? 0 : progress.b.getOrDefault(missionId, 0);
   }

   public static boolean b(MinecraftServer server, UUID playerId, String missionId) {
      g.b progress = b(server).a.get(playerId);
      return progress != null && progress.c.contains(missionId);
   }

   public static boolean a(MinecraftServer server, UUID playerId) {
      g.b progress = b(server).a.get(playerId);
      return progress == null ? false : g.values().stream().filter(m -> m.i()).anyMatch(mission -> progress.c.contains(mission.a()));
   }

   public static boolean b(MinecraftServer server, UUID playerId) {
      g.b progress = b(server).a.get(playerId);
      if (progress != null) {
         progress.b.clear();
         progress.c.clear();
         progress.d.clear();
         b(server).setDirty();
         ServerPlayer online = server.getPlayerList().getPlayer(playerId);
         if (online != null) {
            a(online);
         }

         return true;
      } else {
         return false;
      }
   }

   public static boolean c(MinecraftServer server, UUID playerId, String missionId) {
      g.b progress = b(server).a.get(playerId);
      if (progress != null && g.containsKey(missionId)) {
         boolean existed = progress.b.containsKey(missionId) || progress.c.contains(missionId);
         if (!existed) {
            return false;
         } else {
            progress.b.remove(missionId);
            progress.c.remove(missionId);
            progress.d.remove(missionId);
            b(server).setDirty();
            ServerPlayer online = server.getPlayerList().getPlayer(playerId);
            if (online != null) {
               a(online);
            }

            return true;
         }
      } else {
         return false;
      }
   }

   public static synchronized void a(MinecraftServer server, List<f> definitions) {
      List<f> validated = a(definitions);

      try {
         b(validated);
         g.clear();

         for (f definition : validated) {
            g.put(definition.a(), definition);
         }

         for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            a(player);
         }
      } catch (IOException var5) {
         throw new IllegalStateException("No se pudo guardar la configuración de misiones: " + var5.getMessage(), var5);
      }
   }

   public static void a(MinecraftServer server, String missionId) {
      ArrayList<f> updated = new ArrayList<>(a());
      if (!updated.removeIf(mission -> mission.a().equals(missionId))) {
         throw new IllegalArgumentException("No existe la misión '" + missionId + "'.");
      } else {
         a(server, updated);
      }
   }

   public static void a(ServerPlayer player) {
      h.a(player);
   }

   static JsonObject a(f mission) {
      JsonObject json = new JsonObject();
      json.addProperty("id", mission.a());
      json.addProperty("title", mission.b());
      json.addProperty("description", mission.c());
      json.addProperty("type", mission.d());
      json.addProperty("target", mission.e());
      json.addProperty("required", mission.f());
      json.addProperty("reward", mission.g());
      json.addProperty("day", mission.h());
      json.addProperty("essential", mission.i());
      if (mission.j() != null) {
         json.addProperty("icon", mission.j());
      }

      if (mission.k() != null) {
         json.addProperty("rewardItem", mission.k());
      }

      json.addProperty("rewardItemCount", mission.l());
      return json;
   }

   static f a(JsonObject json) {
      Set<String> allowed = Set.of(
         "id", "title", "description", "type", "target", "required", "reward", "day", "essential", "icon", "rewardItem", "rewardItemCount"
      );

      for (String key : json.keySet()) {
         if (!allowed.contains(key)) {
            throw new IllegalArgumentException("Campo no reconocido '" + key + "'. Las recompensas de llaves no están permitidas.");
         }
      }

      return new f(
         a(json, "id"),
         a(json, "title"),
         a(json, "description"),
         a(json, "type"),
         a(json, "target"),
         b(json, "required"),
         b(json, "reward"),
         b(json, "day"),
         c(json, "essential"),
         json.has("icon") && !json.get("icon").isJsonNull() ? a(json, "icon") : null,
         json.has("rewardItem") && !json.get("rewardItem").isJsonNull() ? a(json, "rewardItem") : null,
         json.has("rewardItemCount") ? b(json, "rewardItemCount") : 0
      );
   }

   private static String a(JsonObject json, String key) {
      if (json.has(key) && json.get(key).isJsonPrimitive() && json.getAsJsonPrimitive(key).isString()) {
         return json.get(key).getAsString();
      } else {
         throw new IllegalArgumentException("Falta el texto obligatorio '" + key + "'.");
      }
   }

   private static int b(JsonObject json, String key) {
      if (json.has(key) && json.get(key).isJsonPrimitive() && json.getAsJsonPrimitive(key).isNumber()) {
         try {
            return new BigDecimal(json.get(key).getAsString()).intValueExact();
         } catch (NumberFormatException | ArithmeticException var3) {
            throw new IllegalArgumentException("El campo '" + key + "' debe ser un entero.");
         }
      } else {
         throw new IllegalArgumentException("Falta el número obligatorio '" + key + "'.");
      }
   }

   private static boolean c(JsonObject json, String key) {
      if (json.has(key) && json.get(key).isJsonPrimitive() && json.getAsJsonPrimitive(key).isBoolean()) {
         return json.get(key).getAsBoolean();
      } else {
         throw new IllegalArgumentException("Falta el booleano obligatorio '" + key + "'.");
      }
   }

   private static List<f> a(List<f> definitions) {
      if (definitions.size() > 100) {
         throw new IllegalArgumentException("Se permiten como máximo 100 misiones.");
      } else {
         HashSet<String> ids = new HashSet<>();
         int essentials = 0;
         ArrayList<f> normalized = new ArrayList<>(definitions.size());

         for (f mission : definitions) {
            if (mission != null && mission.a() != null && mission.a().matches("[a-z0-9_.-]{1,64}")) {
               if (!ids.add(mission.a())) {
                  throw new IllegalArgumentException("Identificador de misión duplicado: " + mission.a());
               }

               a(mission.b(), 80, "título");
               a(mission.c(), 512, "descripción");
               String type = mission.d().toUpperCase(Locale.ROOT);
               if (!Set.of("KILL", "COLLECT", "BREAK").contains(type)) {
                  throw new IllegalArgumentException("Tipo inválido en " + mission.a() + ": usa KILL, COLLECT o BREAK.");
               }

               ResourceLocation target = a(mission.e(), "objetivo", mission.a());

               boolean targetExists = switch (type) {
                  case "KILL" -> BuiltInRegistries.ENTITY_TYPE.getOptional(target).isPresent();
                  case "COLLECT" -> BuiltInRegistries.ITEM.getOptional(target).isPresent();
                  default -> BuiltInRegistries.BLOCK.getOptional(target).isPresent();
               };
               if (!targetExists) {
                  throw new IllegalArgumentException("El objetivo '" + target + "' no existe para " + type + ".");
               }

               if (mission.f() >= 1 && mission.f() <= 1000000) {
                  if (mission.g() >= 1 && mission.g() <= 1000000) {
                     if (mission.h() >= 1 && mission.h() <= 99) {
                        if (mission.i()) {
                           essentials++;
                        }

                        String icon;
                        if ((icon = mission.j()) != null && !icon.isBlank()) {
                           ResourceLocation iconId = a(icon, "icono", mission.a());
                           BuiltInRegistries.ITEM
                              .getOptional(iconId)
                              .orElseThrow(() -> new IllegalArgumentException("El icono '" + iconId + "' no existe como objeto."));
                           icon = iconId.toString();
                        } else {
                           icon = null;
                        }

                        if (mission.k() != null && !mission.k().isBlank()) {
                           ResourceLocation rewardItem = a(mission.k(), "objeto de recompensa", mission.a());
                           BuiltInRegistries.ITEM
                              .getOptional(rewardItem)
                              .orElseThrow(() -> new IllegalArgumentException("El objeto de recompensa '" + rewardItem + "' no existe."));
                           if (mission.l() >= 1 && mission.l() <= 1000000) {
                              normalized.add(
                                 new f(
                                    mission.a(),
                                    mission.b(),
                                    mission.c(),
                                    type,
                                    target.toString(),
                                    mission.f(),
                                    mission.g(),
                                    mission.h(),
                                    mission.i(),
                                    icon,
                                    rewardItem.toString(),
                                    mission.l()
                                 )
                              );
                              continue;
                           }

                           throw new IllegalArgumentException("Cantidad de objeto inválida en " + mission.a() + ".");
                        }

                        if (mission.l() != 0) {
                           throw new IllegalArgumentException("La cantidad de objeto requiere rewardItem en " + mission.a() + ".");
                        }

                        normalized.add(
                           new f(
                              mission.a(), mission.b(), mission.c(), type, target.toString(), mission.f(), mission.g(), mission.h(), mission.i(), icon, null, 0
                           )
                        );
                        continue;
                     }

                     throw new IllegalArgumentException("El día debe estar entre 1 y 99 en " + mission.a() + ".");
                  }

                  throw new IllegalArgumentException("La recompensa de deditas debe ser positiva en " + mission.a() + ".");
               }

               throw new IllegalArgumentException("Cantidad requerida inválida en " + mission.a() + ".");
            }

            throw new IllegalArgumentException("Cada misión necesita un identificador válido de 1 a 64 caracteres.");
         }

         if (essentials > 1) {
            throw new IllegalArgumentException("Solo se permite una misión esencial.");
         } else {
            return List.copyOf(normalized);
         }
      }
   }

   private static void a(String value, int max, String field) {
      if (value == null || value.isBlank() || value.length() > max) {
         throw new IllegalArgumentException("El " + field + " debe tener entre 1 y " + max + " caracteres.");
      }
   }

   private static ResourceLocation a(String value, String field, String missionId) {
      ResourceLocation id = ResourceLocation.tryParse(value);
      if (id == null) {
         throw new IllegalArgumentException(field + " inválido en " + missionId + ": " + value);
      } else {
         return id;
      }
   }

   private static void b(MinecraftServer server, List<f> definitions) throws IOException {
      List<f> validated = a(definitions);
      g.clear();

      for (f definition : validated) {
         g.put(definition.a(), definition);
      }
   }

   private static void b(List<f> definitions) throws IOException {
      JsonObject root = new JsonObject();
      JsonArray array = new JsonArray();

      for (f definition : definitions) {
         array.add(a(definition));
      }

      root.add("missions", array);
      byte[] bytes = e.toJson(root).getBytes(StandardCharsets.UTF_8);
      if (bytes.length > 65536) {
         throw new IllegalArgumentException("La configuración excede el límite de 65536 bytes.");
      } else {
         Files.createDirectories(f.getParent());
         Path temp = f.resolveSibling(f.getFileName() + ".tmp");
         Files.write(temp, bytes);

         try {
            Files.move(temp, f, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
         } catch (IOException var6) {
            Files.move(temp, f, StandardCopyOption.REPLACE_EXISTING);
         }
      }
   }

   private static List<f> b() {
      return List.of(
         new f(
            "essential_day_1",
            "Primera cacería",
            "Derrota a un zombi para recibir la dedita rosa de misión.",
            "KILL",
            "minecraft:zombie",
            1,
            10,
            1,
            true,
            null,
            null,
            0
         ),
         new f(
            "collect_iron",
            "Hierro para empezar",
            "Recoge lingotes de hierro.",
            "COLLECT",
            "minecraft:iron_ingot",
            8,
            5,
            1,
            false,
            "minecraft:iron_ingot",
            "minecraft:bread",
            2
         ),
         new f("break_stone", "Picar piedra", "Rompe bloques de piedra.", "BREAK", "minecraft:stone", 32, 5, 1, false, "minecraft:stone", null, 0)
      );
   }

   private static void a(ServerPlayer player, String type, String target, int amount) {
      if (amount > 0) {
         MinecraftServer server = player.getServer();
         if (server != null) {
            g.MissionState state = b(server);
            g.b playerProgress = state.a.computeIfAbsent(player.getUUID(), ignored -> new g.b());
            playerProgress.a = player.getName().getString();
            boolean changed = false;

            for (f mission : a()) {
               if (mission.d().equals(type) && mission.e().equals(target) && !playerProgress.c.contains(mission.a())) {
                  int previous = playerProgress.b.getOrDefault(mission.a(), 0);
                  int current = Math.min(mission.f(), previous + amount);
                  playerProgress.b.put(mission.a(), current);
                  changed = true;
                  if (current >= mission.f()) {
                     playerProgress.c.add(mission.a());
                     boolean firstCompletionReward = playerProgress.d.add(mission.a());
                     state.setDirty();
                     if (firstCompletionReward) {
                        a(server, player, mission);
                     } else {
                        player.displayClientMessage(Component.literal("Misión completada: " + mission.b() + ". La recompensa ya se había recibido."), false);
                     }
                  }
               }
            }

            if (changed) {
               state.setDirty();
               a(player);
            }
         }
      }
   }

   private static void a(MinecraftServer server, ServerPlayer player, f mission) {
      if (mission.g() > 0) {
         com.eric.revill.h.b.a(server, player.getUUID(), com.eric.revill.h.c.a, mission.g());
      }

      if (mission.i()) {
         ItemStack pinkDedita = new ItemStack((ItemLike)com.eric.revill.l.g.e.get());
         pinkDedita.set(DataComponents.CUSTOM_NAME, Component.literal("Dedita de misión del día " + mission.h()));
         CustomData.update(DataComponents.CUSTOM_DATA, pinkDedita, tag -> {
            CompoundTag sub = tag.getCompound("dedsafio4");
            sub.putInt("dailyMissionDay", mission.h());
            tag.put("dedsafio4", sub);
         });
         pinkDedita.set(DataComponents.LORE, new ItemLore(List.of(
            Component.literal("Entrégala en el pedestal del día correspondiente para recibir tu recompensa por correo.")
               .withStyle(ChatFormatting.GRAY)
         )));
         a(player, pinkDedita);
      }

      if (mission.k() != null && !mission.k().isBlank()) {
         Item item = (Item)BuiltInRegistries.ITEM.get(ResourceLocation.tryParse(mission.k()));
         int remaining = mission.l();

         while (remaining > 0) {
            int size = Math.min(remaining, item.getDefaultMaxStackSize());
            a(player, new ItemStack(item, size));
            remaining -= size;
         }
      }

      player.displayClientMessage(Component.literal("Misión completada: " + mission.b() + ". Recompensa: " + mission.g() + " deditas."), false);
   }

   public static int a(ItemStack token) {
      if (token != null && token.is(com.eric.revill.l.g.e.get())) {
         CustomData customData = token.get(DataComponents.CUSTOM_DATA);
         CompoundTag metadata = customData != null && customData.contains("dedsafio4") ? customData.copyTag().getCompound("dedsafio4") : null;
         if (metadata != null && metadata.contains("dailyMissionDay")) {
            int day = metadata.getInt("dailyMissionDay");
            return day > 0 ? day : -1;
         } else {
            String displayName = token.getHoverName().getString();
            Matcher matcher = Pattern.compile("(?i)dedita de misi[oó]n del d[ií]a\\s+(\\d+)").matcher(displayName);
            if (!matcher.matches()) {
               return -1;
            } else {
               try {
                  int day = Integer.parseInt(matcher.group(1));
                  return day > 0 && day <= 99 ? day : -1;
               } catch (NumberFormatException var5) {
                  return -1;
               }
            }
         }
      } else {
         return -1;
      }
   }

   private static void a(ServerPlayer player, ItemStack stack) {
      if (!player.getInventory().add(stack) && !stack.isEmpty()) {
         player.drop(stack, false);
      }
   }

   private static MissionState b(MinecraftServer server) {
      ServerLevel world = server.overworld();
      return world.getDataStorage().computeIfAbsent(MissionState.FACTORY, "dedsafio4_missions");
   }

   static JsonObject b(ServerPlayer player) {
      MissionState state = b(player.getServer());
      g.b own = state.a.getOrDefault(player.getUUID(), new g.b());
      JsonObject root = new JsonObject();
      JsonArray missions = new JsonArray();

      for (f mission : a()) {
         JsonObject entry2 = a(mission);
         entry2.addProperty("progress", own.b.getOrDefault(mission.a(), 0));
         entry2.addProperty("completed", own.c.contains(mission.a()));
         missions.add(entry2);
      }

      root.add("missions", missions);
      root.addProperty("operator", player.hasPermissions(4));
      if (player.hasPermissions(4)) {
         JsonArray players = new JsonArray();
         state.a.entrySet().stream().sorted(Comparator.comparing(entry -> entry.getValue().a, String.CASE_INSENSITIVE_ORDER)).limit(200L).forEach(entry -> {
            JsonObject row = new JsonObject();
            row.addProperty("uuid", entry.getKey().toString());
            row.addProperty("name", entry.getValue().a);
            row.addProperty("online", player.getServer().getPlayerList().getPlayer(entry.getKey()) != null);
            JsonArray completed = new JsonArray();
            entry.getValue().c.stream().limit(100L).forEach(missionId -> {
               f missionx = g.get(missionId);
               completed.add(missionx == null ? missionId : missionx.b());
            });
            row.add("completed", completed);
            players.add(row);
         });
         root.add("players", players);
      }

      return root;
   }

   private static final class MissionState extends SavedData {
      public static final SavedData.Factory<MissionState> FACTORY = new SavedData.Factory<>(MissionState::new, MissionState::a);
      private final Map<UUID, g.b> a = new HashMap<>();

      private static MissionState a(CompoundTag nbt, net.minecraft.core.HolderLookup.Provider registries) {
         MissionState state = new MissionState();
         ListTag players = nbt.getList("players", 10);

         for (int index = 0; index < players.size(); index++) {
            CompoundTag playerNbt = players.getCompound(index);

            try {
               UUID uuid = UUID.fromString(playerNbt.getString("uuid"));
               g.b progress = new g.b();
               progress.a = playerNbt.getString("name");
               CompoundTag values = playerNbt.getCompound("progress");

               for (String missionId : values.getAllKeys()) {
                  int value = values.getInt(missionId);
                  if (value >= 0 && value <= 1000000) {
                     progress.b.put(missionId, value);
                  }
               }

               ListTag completed = playerNbt.getList("completed", 8);

               for (int item = 0; item < completed.size(); item++) {
                  progress.c.add(completed.getString(item));
               }

               ListTag rewarded = playerNbt.getList("rewarded", 8);

               for (int item = 0; item < rewarded.size(); item++) {
                  progress.d.add(rewarded.getString(item));
               }

               state.a.put(uuid, progress);
            } catch (IllegalArgumentException var11) {
               RevillMod.b.warn("Se ha omitido una entrada inválida del progreso de misiones.", var11);
            }
         }

         return state;
      }

      @Override
      public CompoundTag save(CompoundTag nbt, net.minecraft.core.HolderLookup.Provider registries) {
         ListTag players = new ListTag();
         this.a.forEach((uuid, progress) -> {
            CompoundTag playerNbt = new CompoundTag();
            playerNbt.putString("uuid", uuid.toString());
            playerNbt.putString("name", progress.a);
            CompoundTag values = new CompoundTag();
            progress.b.forEach((arg_0, arg_1) -> values.putInt(arg_0, arg_1));
            playerNbt.put("progress", values);
            ListTag completed = new ListTag();
            progress.c.forEach(missionId -> completed.add(StringTag.valueOf(missionId)));
            playerNbt.put("completed", completed);
            ListTag rewarded = new ListTag();
            progress.d.forEach(missionId -> rewarded.add(StringTag.valueOf(missionId)));
            playerNbt.put("rewarded", rewarded);
            players.add(playerNbt);
         });
         nbt.put("players", players);
         return nbt;
      }
   }

   private static final class b {
      private String a = "";
      private final Map<String, Integer> b = new HashMap<>();
      private final Set<String> c = new HashSet<>();
      private final Set<String> d = new HashSet<>();
   }
}
