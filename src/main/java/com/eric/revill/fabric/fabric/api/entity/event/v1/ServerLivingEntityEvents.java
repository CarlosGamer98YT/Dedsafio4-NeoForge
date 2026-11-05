package com.eric.revill.fabric.fabric.api.entity.event.v1;

/**
 * Adaptador de eventos de entidades vivas de Fabric API a NeoForge.
 * 
 * Captura eventos de muerte y daño letal de jugadores y entidades para aplicar mecánicas
 * de modo espectador y recompensas de misiones.
 */

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public final class ServerLivingEntityEvents {
    @FunctionalInterface
    public interface AfterDeath {
        void afterDeath(LivingEntity entity, DamageSource damageSource);
    }

    @FunctionalInterface
    public interface AllowDeath {
        boolean allowDeath(LivingEntity entity, DamageSource damageSource, float damageAmount);
    }

    @FunctionalInterface
    public interface AllowDamage {
        boolean allowDamage(LivingEntity entity, DamageSource source, float amount);
    }

    public static class Event<T> {
        private final List<T> listeners = new CopyOnWriteArrayList<>();
        public void register(T listener) { listeners.add(listener); }
        public List<T> getListeners() { return listeners; }
    }

    public static final Event<AfterDeath> AFTER_DEATH = new Event<>();
    public static final Event<AllowDeath> ALLOW_DEATH = new Event<>();
    public static final Event<AllowDamage> ALLOW_DAMAGE = new Event<>();
}
