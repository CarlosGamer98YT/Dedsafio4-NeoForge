package com.eric.revill.t;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.eric.revill.fabric.fabric.api.networking.v1.PacketByteBufs;
import com.eric.revill.fabric.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.phys.AABB;

public final class b {
   private static final String e = "dedsafio4_waypoints";
   private static final String f = "dedsafio4_waypoint_";
   public static final ResourceLocation a = ResourceLocation.fromNamespaceAndPath("revill", "sync_waypoints");
   public static final ResourceLocation b = ResourceLocation.fromNamespaceAndPath("revill", "add_waypoint");
   public static final ResourceLocation c = ResourceLocation.fromNamespaceAndPath("revill", "remove_waypoint");
   public static final ResourceLocation d = ResourceLocation.fromNamespaceAndPath("revill", "edit_waypoint");
   private static final Map<String, com.eric.revill.t.a> g = new LinkedHashMap<>();
   private static final Map<String, com.eric.revill.t.a> h = new LinkedHashMap<>();
   private static final Set<b.a> i = new HashSet<>();
   private static WaypointState j;

   private b() {
   }

   public static void a(MinecraftServer server) {
      h.clear();
      j = server.overworld().getDataStorage().computeIfAbsent(WaypointState.FACTORY, "dedsafio4_waypoints");
      g.clear();
      g.putAll(j.a);
      i.clear();
      i.addAll(j.b);
      if (i.isEmpty()) {
         g.values().forEach(waypoint -> i.add(new b.a(waypoint.f(), waypoint.c().getX() >> 4, waypoint.c().getZ() >> 4)));
      }

      b(server);
      c(server);
   }

   public static void a(MinecraftServer server, com.eric.revill.t.a waypoint) {
      if (waypoint != null) {
         com.eric.revill.t.a previous = g.get(waypoint.a());
         if (previous != null) {
            c(server, previous);
         }

         g.put(waypoint.a(), waypoint);
         b();
         b(server);
         c(server);
      }
   }

   public static void a(MinecraftServer server, List<com.eric.revill.t.a> waypoints) {
      if (server != null) {
         LinkedHashMap<String, com.eric.revill.t.a> replacements = new LinkedHashMap<>();

         for (com.eric.revill.t.a waypoint : waypoints) {
            replacements.put(waypoint.a(), waypoint);
         }

         boolean changed = false;

         for (com.eric.revill.t.a oldWaypoint : new ArrayList<>(h.values())) {
            com.eric.revill.t.a replacement = replacements.get(oldWaypoint.a());
            if (replacement == null || !a(oldWaypoint, replacement)) {
               c(server, oldWaypoint);
               h.remove(oldWaypoint.a());
               changed = true;
            }
         }

         for (com.eric.revill.t.a waypoint : replacements.values()) {
            if (!h.containsKey(waypoint.a())) {
               h.put(waypoint.a(), waypoint);
               changed = true;
            }
         }

         if (changed) {
            b(server);
            c(server);
         }
      }
   }

   private static boolean a(com.eric.revill.t.a first, com.eric.revill.t.a second) {
      return first.b().equals(second.b())
         && first.c().equals(second.c())
         && first.d().equals(second.d())
         && first.e() == second.e()
         && first.f().equals(second.f());
   }

   public static void a(MinecraftServer server, String idOrName) {
      if (idOrName != null && !idOrName.isBlank()) {
         com.eric.revill.t.a removed = g.remove(idOrName);
         if (removed == null) {
            String targetId = null;

            for (com.eric.revill.t.a wp : g.values()) {
               if (wp.b().equalsIgnoreCase(idOrName)) {
                  targetId = wp.a();
                  break;
               }
            }

            if (targetId != null) {
               removed = g.remove(targetId);
            }
         }

         if (removed != null) {
            c(server, removed);
            b();
            b(server);
            c(server);
         }
      }
   }

   public static void b(MinecraftServer server) {
      if (server != null) {
         HashSet<b.a> waypointChunks = new HashSet<>();

         for (com.eric.revill.t.a waypoint : c()) {
            ServerLevel world = b(server, waypoint);
            if (world != null) {
               BlockPos pos = waypoint.c();
               waypointChunks.add(new b.a(world.dimension().location().toString(), pos.getX() >> 4, pos.getZ() >> 4));
            }
         }

         for (b.a previous : new HashSet<>(i)) {
            if (!waypointChunks.contains(previous)) {
               ServerLevel world = b(server, previous.a);
               if (world != null) {
                  world.setChunkForced(previous.b, previous.c, false);
               }

               i.remove(previous);
            }
         }

         HashSet<b.a> ownedChunks = new HashSet<>(i);

         for (com.eric.revill.t.a waypointx : c()) {
            ServerLevel world2 = b(server, waypointx);
            if (world2 != null) {
               BlockPos pos = waypointx.c();
               int chunkX = pos.getX() >> 4;
               int chunkZ = pos.getZ() >> 4;
               b.a chunk = new b.a(world2.dimension().location().toString(), chunkX, chunkZ);
               if (!world2.getForcedChunks().contains(ChunkPos.asLong(chunkX, chunkZ))) {
                  world2.setChunkForced(chunkX, chunkZ, true);
                  ownedChunks.add(chunk);
               }

               a(world2, waypointx);
            }
         }

         i.clear();
         i.addAll(ownedChunks);
         if (j != null) {
            j.a.clear();
            j.a.putAll(g);
            j.b.clear();
            j.b.addAll(i);
            j.setDirty();
         }
      }
   }

   private static ServerLevel b(MinecraftServer server, com.eric.revill.t.a waypoint) {
      return waypoint.f().isBlank() ? server.overworld() : b(server, waypoint.f());
   }

   private static ServerLevel b(MinecraftServer server, String dimension) {
      ResourceLocation id = ResourceLocation.tryParse(dimension);
      if (id == null) {
         return null;
      } else {
         ResourceKey key = ResourceKey.create(Registries.DIMENSION, id);
         return server.getLevel(key);
      }
   }

   private static void a(ServerLevel world, com.eric.revill.t.a waypoint) {
      BlockPos pos = waypoint.c();
      AABB box = a(pos);
      String tag = a(waypoint.a());
      List<ArmorStand> existing = world.getEntitiesOfClass(
         ArmorStand.class, box, entity -> entity.getTags().contains(tag) || entity.getTags().contains("waypoint_" + waypoint.a())
      );
      existing.forEach(Entity::discard);
      List<c> markers = world.getEntitiesOfClass(c.class, box, entity -> waypoint.a().equals(entity.a()));
      if (markers.isEmpty()) {
         c marker = new c((EntityType<? extends c>)com.eric.revill.i.b.b.get(), world);
         marker.a(waypoint);
         world.addFreshEntity(marker);
      } else {
         c marker = markers.get(0);

         for (int index = 1; index < markers.size(); index++) {
            markers.get(index).discard();
         }

         marker.a(waypoint);
      }
   }

   private static void c(MinecraftServer server, com.eric.revill.t.a waypoint) {
      ServerLevel world = b(server, waypoint);
      if (world != null) {
         String tag = a(waypoint.a());

         for (ArmorStand marker : world.getEntitiesOfClass(
            ArmorStand.class, a(waypoint.c()), entity -> entity.getTags().contains(tag) || entity.getTags().contains("waypoint_" + waypoint.a())
         )) {
            marker.discard();
         }

         for (c marker : world.getEntitiesOfClass(c.class, a(waypoint.c()), entity -> waypoint.a().equals(entity.a()))) {
            marker.discard();
         }
      }
   }

   private static AABB a(BlockPos pos) {
      double x = (double)pos.getX() + 0.5;
      double y = (double)pos.getY() + 1.0;
      double z = (double)pos.getZ() + 0.5;
      return new AABB(x - 1.0, y - 1.0, z - 1.0, x + 1.0, y + 2.0, z + 1.0);
   }

   private static String a(String waypointId) {
      return "dedsafio4_waypoint_" + waypointId;
   }

   private static void b() {
      if (j != null) {
         j.a.clear();
         j.a.putAll(g);
         j.setDirty();
      }
   }

   private static List<com.eric.revill.t.a> c() {
      LinkedHashMap<String, com.eric.revill.t.a> combined = new LinkedHashMap<>(g);
      combined.putAll(h);
      return new ArrayList<>(combined.values());
   }

   public static List<com.eric.revill.t.a> a() {
      return Collections.unmodifiableList(new ArrayList<>(g.values()));
   }

   public static void a(MinecraftServer server, BlockPos pos, String dimension) {
      com.eric.revill.t.a bunkerWp = new com.eric.revill.t.a("bunker", "BÚNKER", pos, "bunker", -56798, dimension);
      a(server, bunkerWp);
   }

   public static void a(ServerPlayer player) {
      if (player != null) {
         FriendlyByteBuf buf = d();
         ServerPlayNetworking.send(player, a, buf);
      }
   }

   public static void c(MinecraftServer server) {
      if (server != null) {
         FriendlyByteBuf buf = d();

         for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            ServerPlayNetworking.send(player, a, buf);
         }
      }
   }

   private static FriendlyByteBuf d() {
      FriendlyByteBuf buf = PacketByteBufs.create();
      ArrayList<com.eric.revill.t.a> waypoints = new ArrayList<>(g.values());
      buf.writeInt(waypoints.size());

      for (com.eric.revill.t.a wp : waypoints) {
         buf.writeUtf(wp.a());
         buf.writeUtf(wp.b());
         buf.writeBlockPos(wp.c());
         buf.writeUtf(wp.d());
         buf.writeInt(wp.e());
         buf.writeUtf(wp.f());
      }

      return buf;
   }

   public static List<com.eric.revill.t.a> a(FriendlyByteBuf buf) {
      ArrayList<com.eric.revill.t.a> list = new ArrayList<>();
      int count = buf.readInt();

      for (int i2 = 0; i2 < count; i2++) {
         String id = buf.readUtf();
         String name = buf.readUtf();
         BlockPos pos = buf.readBlockPos();
         String icon = buf.readUtf();
         int color = buf.readInt();
         String dimension = buf.readUtf();
         list.add(new com.eric.revill.t.a(id, name, pos, icon, color, dimension));
      }

      return list;
   }

   private static final class WaypointState extends SavedData {
      public static final SavedData.Factory<WaypointState> FACTORY = new SavedData.Factory<>(WaypointState::new, WaypointState::a);
      private final Map<String, com.eric.revill.t.a> a = new LinkedHashMap<>();
      private final Set<b.a> b = new HashSet<>();

      private static WaypointState a(CompoundTag nbt, net.minecraft.core.HolderLookup.Provider registries) {
         WaypointState state = new WaypointState();
         ListTag entries = nbt.getList("waypoints", 10);

         for (int index = 0; index < entries.size(); index++) {
            CompoundTag entry = entries.getCompound(index);
            com.eric.revill.t.a waypoint = new com.eric.revill.t.a(
               entry.getString("id"),
               entry.getString("name"),
               new BlockPos(entry.getInt("x"), entry.getInt("y"), entry.getInt("z")),
               entry.getString("icon"),
               entry.getInt("color"),
               entry.getString("dimension")
            );
            state.a.put(waypoint.a(), waypoint);
         }

         ListTag chunks = nbt.getList("forcedChunks", 10);

         for (int index = 0; index < chunks.size(); index++) {
            CompoundTag chunk = chunks.getCompound(index);
            state.b.add(new b.a(chunk.getString("dimension"), chunk.getInt("x"), chunk.getInt("z")));
         }

         return state;
      }

      @Override
      public CompoundTag save(CompoundTag nbt, net.minecraft.core.HolderLookup.Provider registries) {
         ListTag entries = new ListTag();

         for (com.eric.revill.t.a waypoint : this.a.values()) {
            CompoundTag entry = new CompoundTag();
            entry.putString("id", waypoint.a());
            entry.putString("name", waypoint.b());
            entry.putInt("x", waypoint.c().getX());
            entry.putInt("y", waypoint.c().getY());
            entry.putInt("z", waypoint.c().getZ());
            entry.putString("icon", waypoint.d());
            entry.putInt("color", waypoint.e());
            entry.putString("dimension", waypoint.f());
            entries.add(entry);
         }

         nbt.put("waypoints", entries);
         ListTag chunks = new ListTag();

         for (b.a forcedChunk : this.b) {
            CompoundTag chunk = new CompoundTag();
            chunk.putString("dimension", forcedChunk.a);
            chunk.putInt("x", forcedChunk.b);
            chunk.putInt("z", forcedChunk.c);
            chunks.add(chunk);
         }

         nbt.put("forcedChunks", chunks);
         return nbt;
      }
   }

   private static record a(String a, int b, int c) {
   }
}
