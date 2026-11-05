package com.eric.revill.i;

/**
 * Registro de tipos de entidades de DEDSAFIO 4 para NeoForge.
 * 
 * Registra:
 * - "cajero": Entidad Mob del Cajero Automático.
 * - "waypoint_marker": Entidad invisible auxiliar usada como marcador de waypoints.
 */

import com.eric.revill.t.c;
import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType.Builder;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class b {
   private static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, "dedsafio-4-mechanics");
   public static final DeferredHolder<EntityType<?>, EntityType<a>> a = ENTITY_TYPES.register(
      "cajero", () -> Builder.of(a::new, MobCategory.MISC).sized(0.75F, 1.8F).clientTrackingRange(10).updateInterval(3).build("cajero")
   );
   public static final DeferredHolder<EntityType<?>, EntityType<c>> b = ENTITY_TYPES.register(
      "waypoint_marker", () -> Builder.of(c::new, MobCategory.MISC).sized(0.1F, 0.1F).clientTrackingRange(8).updateInterval(20).build("waypoint_marker")
   );

   private b() {
   }

   public static void register(IEventBus modEventBus) {
      ENTITY_TYPES.register(modEventBus);
   }

   public static void registerAttributes(EntityAttributeCreationEvent event) {
      event.put((EntityType)a.get(), Mob.createMobAttributes().build());
   }
}
