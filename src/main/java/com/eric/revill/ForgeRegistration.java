package com.eric.revill;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.RegisterEvent;

public final class ForgeRegistration {
   private static final List<ForgeRegistration.PendingRegistration> PENDING = new ArrayList<>();

   private ForgeRegistration() {
   }

   public static <T> T register(ResourceKey<?> registryKey, ResourceLocation id, T value) {
      PENDING.add(new ForgeRegistration.PendingRegistration(registryKey, id, () -> value, ignored -> {
      }));
      return value;
   }

   public static <T> Supplier<T> registerEager(ResourceKey<?> registryKey, ResourceLocation id, Supplier<T> supplier) {
      AtomicReference<T> ref = new AtomicReference<>();
      PENDING.add(new ForgeRegistration.PendingRegistration(registryKey, id, supplier, value -> ref.set((T)value)));
      return ref::get;
   }

   public static <T> void registerLazy(ResourceKey<?> registryKey, ResourceLocation id, Supplier<T> supplier, Consumer<T> onRegistered) {
      PENDING.add(new ForgeRegistration.PendingRegistration(registryKey, id, supplier, value -> onRegistered.accept((T)value)));
   }

   public static void onRegister(RegisterEvent event) {
      Iterator<ForgeRegistration.PendingRegistration> iterator = PENDING.iterator();

      while (iterator.hasNext()) {
         ForgeRegistration.PendingRegistration registration = iterator.next();
         if (registration.registryKey().equals(event.getRegistryKey())) {
            registration.register(event);
            iterator.remove();
         }
      }
   }

   @SuppressWarnings("unchecked")
   private static <T> void register(RegisterEvent event, ResourceKey<?> registryKey, ResourceLocation id, Supplier<?> supplier) {
      event.register((ResourceKey<? extends net.minecraft.core.Registry<T>>) registryKey, id, (Supplier<T>) supplier);
   }

   private static record PendingRegistration(ResourceKey<?> registryKey, ResourceLocation id, Supplier<?> supplier, Consumer<Object> onRegistered) {
      private void register(RegisterEvent event) {
         ForgeRegistration.register(event, this.registryKey, this.id, () -> {
            Object value = this.supplier.get();
            this.onRegistered.accept(value);
            return value;
         });
      }
   }
}
