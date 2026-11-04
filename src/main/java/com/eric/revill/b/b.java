package com.eric.revill.b;

import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public final class b {
   private static final a a = new a();
   private static a b = new a();

   private b() {
   }

   public static void a(MinecraftServer server, BlockPos firstCorner, BlockPos secondCorner) {
      if (server != null) {
         ServerLevel world = server.overworld();
         b = new a();
         b.a(world.dimension().location().toString());
         b.b(firstCorner);
         b.c(secondCorner);
      }
   }

   public static a a() {
      return b == null ? a : b;
   }

   public static void b() {
      b = a;
   }

   public static boolean c() {
      return a().a();
   }

   public static boolean a(ServerPlayer player) {
      return player == null ? false : a().a(player.blockPosition());
   }

   public static void a(com.eric.revill.j.b state) {
      if (state != null) {
         state.a(a());
      }
   }
}
