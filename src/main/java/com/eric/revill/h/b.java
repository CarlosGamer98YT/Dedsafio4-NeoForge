package com.eric.revill.h;

/**
 * Gestor de economía y saldos persistentes en el servidor.
 * 
 * Almacena los balances de Dedita Azul, Dedita Roja y Dedita Verde de cada jugador
 * en los datos guardados del mundo (SavedData bajo la clave "dedsafio4_economy").
 * Maneja depósitos, retiros y sincronización hacia el cliente.
 */

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.saveddata.SavedData;

public final class b {
   private static final String a = "dedsafio4_economy";

   private b() {
   }

   /** Obtiene la cantidad de monedas del tipo indicado que posee un jugador */
   public static int a(MinecraftServer server, UUID playerId, c type) {
      return a(server).a(playerId, type);
   }

   /** Obtiene el array de saldos del jugador: [0] = Azul, [1] = Verde, [2] = Roja */
   public static int[] a(MinecraftServer server, UUID playerId) {
      b.EconomyState state = a(server);
      return new int[]{state.a(playerId, c.a), state.a(playerId, c.b), state.a(playerId, c.c)};
   }

   /** Deposita una cantidad positiva de monedas de un tipo en la cuenta del jugador */
   public static void a(MinecraftServer server, UUID playerId, c type, int amount) {
      if (amount > 0) {
         b.EconomyState state = a(server);
         state.a(playerId, type, Math.addExact(state.a(playerId, type), amount));
         state.setDirty();
         b(server, playerId);
      }
   }

   /** Retira una cantidad de monedas si el jugador tiene saldo suficiente; devuelve true si tuvo éxito */
   public static boolean b(MinecraftServer server, UUID playerId, c type, int amount) {
      if (amount <= 0) {
         return false;
      } else {
         b.EconomyState state = a(server);
         int balance = state.a(playerId, type);
         if (balance < amount) {
            return false;
         } else {
            state.a(playerId, type, balance - amount);
            state.setDirty();
            b(server, playerId);
            return true;
         }
      }
   }

   /** Retira simultáneamente cantidades de varios tipos de monedas si todos los fondos son suficientes */
   public static boolean a(MinecraftServer server, UUID playerId, int[] amounts) {
      if (amounts.length != c.a().length) {
         return false;
      } else {
         b.EconomyState state = a(server);

         for (c type : c.a()) {
            int amount = amounts[type.ordinal()];
            if (amount < 0 || state.a(playerId, type) < amount) {
               return false;
            }
         }

         for (c typex : c.a()) {
            state.a(playerId, typex, state.a(playerId, typex) - amounts[typex.ordinal()]);
         }

         state.setDirty();
         b(server, playerId);
         return true;
      }
   }

   /** Establece directamente el saldo de un jugador para un tipo de moneda (comando admin) */
   public static void c(MinecraftServer server, UUID playerId, c type, int amount) {
      b.EconomyState state = a(server);
      state.a(playerId, type, Math.max(0, amount));
      state.setDirty();
      b(server, playerId);
   }

   /** Sincroniza por paquete de red los saldos bancarios actuales hacia el cliente del jugador */
   public static void a(ServerPlayer player) {
      d.a(player, a(player.getServer(), player.getUUID()));
   }

   /** Notifica al jugador si está conectado para actualizar sus balances en pantalla */
   private static void b(MinecraftServer server, UUID playerId) {
      ServerPlayer player = server.getPlayerList().getPlayer(playerId);
      if (player != null) {
         a(player);
      }
   }

   /** Obtiene o inicializa los datos de economía guardados del Overworld */
   private static b.EconomyState a(MinecraftServer server) {
      ServerLevel world = server.overworld();
      return world.getDataStorage().computeIfAbsent(b.EconomyState.FACTORY, "dedsafio4_economy");
   }

   /**
    * Estructura persistente SavedData de Minecraft que serializa los saldos en NBT.
    */
   private static final class EconomyState extends SavedData {
      public static final SavedData.Factory<b.EconomyState> FACTORY = new SavedData.Factory<>(b.EconomyState::new, b.EconomyState::a);
      private final Map<UUID, int[]> a = new HashMap<>();

      private int a(UUID playerId, c type) {
         int[] values = this.a.get(playerId);
         return values == null ? 0 : values[type.ordinal()];
      }

      private void a(UUID playerId, c type, int value) {
         int[] values = this.a.computeIfAbsent(playerId, ignored -> new int[c.a().length]);
         values[type.ordinal()] = value;
      }

      private static b.EconomyState a(CompoundTag nbt, net.minecraft.core.HolderLookup.Provider registries) {
         b.EconomyState state = new b.EconomyState();

         for (String uuidKey : nbt.getAllKeys()) {
            try {
               UUID playerId = UUID.fromString(uuidKey);
               CompoundTag playerNbt = nbt.getCompound(uuidKey);
               int[] values = new int[c.a().length];

               for (c type : c.a()) {
                  values[type.ordinal()] = Math.max(0, playerNbt.getInt(type.b()));
               }

               state.a.put(playerId, values);
            } catch (IllegalArgumentException var11) {
            }
         }

         return state;
      }

      @Override
      public CompoundTag save(CompoundTag nbt, net.minecraft.core.HolderLookup.Provider registries) {
         this.a.forEach((playerId, values) -> {
            CompoundTag playerNbt = new CompoundTag();

            for (c type : c.a()) {
               playerNbt.putInt(type.b(), values[type.ordinal()]);
            }

            nbt.put(playerId.toString(), playerNbt);
         });
         return nbt;
      }
   }
}
