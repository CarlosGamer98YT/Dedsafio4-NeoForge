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

/**
 * Adaptador de registros para NeoForge.
 * 
 * Permite registrar objetos de juego (bloques, ítems, entidades, menús, pestañas)
 * de forma temprana mediante una cola de registros pendientes (PENDING), los cuales se
 * despachan de forma segura durante el evento {@link RegisterEvent} de NeoForge.
 */
public final class ForgeRegistration {
   /** Lista de registros en espera de que se dispare el RegisterEvent correspondiente. */
   private static final List<ForgeRegistration.PendingRegistration> PENDING = new ArrayList<>();

   private ForgeRegistration() {
   }

   /**
    * Registra un valor directo en el registro indicado.
    * 
    * @param registryKey Clave del registro (ej. Registries.ITEM, Registries.BLOCK).
    * @param id Identificador único (ResourceLocation).
    * @param value Objeto a registrar.
    * @return El mismo valor registrado.
    */
   public static <T> T register(ResourceKey<?> registryKey, ResourceLocation id, T value) {
      PENDING.add(new ForgeRegistration.PendingRegistration(registryKey, id, () -> value, ignored -> {
      }));
      return value;
   }

   /**
    * Registra un Supplier de forma impaciente (eager).
    * Retorna un Supplier que proveerá el objeto una vez registrado por NeoForge.
    * 
    * @param registryKey Clave del registro.
    * @param id Identificador único del recurso.
    * @param supplier Proveedor del objeto a instanciar.
    * @return Supplier que retorna la instancia registrada.
    */
   public static <T> Supplier<T> registerEager(ResourceKey<?> registryKey, ResourceLocation id, Supplier<T> supplier) {
      AtomicReference<T> ref = new AtomicReference<>();
      PENDING.add(new ForgeRegistration.PendingRegistration(registryKey, id, supplier, value -> ref.set((T)value)));
      return ref::get;
   }

   /**
    * Registra un Supplier de forma perezosa (lazy) con una acción de callback
    * que se ejecutará en cuanto el objeto haya sido registrado.
    */
   public static <T> void registerLazy(ResourceKey<?> registryKey, ResourceLocation id, Supplier<T> supplier, Consumer<T> onRegistered) {
      PENDING.add(new ForgeRegistration.PendingRegistration(registryKey, id, supplier, value -> onRegistered.accept((T)value)));
   }

   /**
    * Listener del evento de ciclo de vida RegisterEvent de NeoForge.
    * Itera los registros pendientes y registra aquellos que coincidan con la clave actual.
    */
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

   /**
    * Entrada pendiente de registro que encapsula la clave, el ID, el supplier y el callback opcional.
    */
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
