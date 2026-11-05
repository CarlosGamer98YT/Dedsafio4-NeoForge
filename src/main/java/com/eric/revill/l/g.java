package com.eric.revill.l;

/**
 * Registro de ítems y pestaña del modo creativo "Dedsafio 4".
 * 
 * Contiene las referencias a todas las monedas (Dedita Azul, Roja, Verde),
 * consumibles (Corazones), herramientas (Palitomágico, Mochila, Guía de Admin)
 * y configura la pestaña creativa oficial del mod.
 */

import com.eric.revill.ForgeRegistration;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import com.eric.revill.fabric.fabric.api.event.lifecycle.v1.ServerTickEvents;
import com.eric.revill.fabric.fabric.api.event.lifecycle.v1.ServerTickEvents.EndTick;
import com.eric.revill.fabric.fabric.api.itemgroup.v1.FabricItemGroup;
import com.eric.revill.fabric.fabric.api.itemgroup.v1.ItemGroupEvents;
import com.eric.revill.fabric.fabric.api.itemgroup.v1.ItemGroupEvents.ModifyEntries;
import com.eric.revill.fabric.fabric.api.networking.v1.PacketByteBufs;
import com.eric.revill.fabric.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.food.FoodProperties.Builder;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.ItemLike;

public final class g {
   private static final ResourceLocation r = ResourceLocation.fromNamespaceAndPath("dedsafio-4-mechanics", "red_hearts_bonus");
   private static final ResourceLocation s = ResourceLocation.fromNamespaceAndPath("dedsafio-4-mechanics", "golem_totem_bonus");
   public static final ResourceLocation a = ResourceLocation.fromNamespaceAndPath("dedsafio-4-mechanics", "nutritotem_group");
   public static final ResourceKey<CreativeModeTab> b = ResourceKey.create(Registries.CREATIVE_MODE_TAB, a);

   /** Moneda básica de Dedita Azul */
   public static final Supplier<Item> c = ForgeRegistration.registerEager(
      Registries.ITEM,
      ResourceLocation.fromNamespaceAndPath("dedsafio-4-mechanics", "dedita"),
      () -> new b(new Properties().stacksTo(64), "item.dedsafio-4-mechanics.dedita.tooltip")
   );
   /** Moneda de alto valor: Dedita Roja (10000 unidades) */
   public static final Supplier<Item> d = ForgeRegistration.registerEager(
      Registries.ITEM,
      ResourceLocation.fromNamespaceAndPath("dedsafio-4-mechanics", "dedita_roja"),
      () -> new b(new Properties().stacksTo(64), "item.dedsafio-4-mechanics.dedita_roja.tooltip")
   );
   /** Moneda de Dedita Rosa */
   public static final Supplier<Item> e = ForgeRegistration.registerEager(
      Registries.ITEM,
      ResourceLocation.fromNamespaceAndPath("dedsafio-4-mechanics", "dedita_rosa"),
      () -> new b(new Properties().stacksTo(64), "item.dedsafio-4-mechanics.dedita_rosa.tooltip")
   );
   /** Moneda intermedia: Dedita Verde (100 unidades) */
   public static final Supplier<Item> f = ForgeRegistration.registerEager(
      Registries.ITEM,
      ResourceLocation.fromNamespaceAndPath("dedsafio-4-mechanics", "dedita_verde"),
      () -> new b(new Properties().stacksTo(64), "item.dedsafio-4-mechanics.dedita_verde.tooltip")
   );
   /** Huevo generador para invocar la entidad del Cajero Automático (ATM) */
   public static final Supplier<Item> g2 = ForgeRegistration.registerEager(
      Registries.ITEM,
      ResourceLocation.fromNamespaceAndPath("dedsafio-4-mechanics", "cajero_spawn_egg"),
      () -> new a(com.eric.revill.i.b.a, 2503224, 5416959, new Properties())
   );
   /** Palitomágico: herramienta de teletransporte instantáneo al punto de reaparición */
   public static final Supplier<Item> h = ForgeRegistration.registerEager(
      Registries.ITEM, ResourceLocation.fromNamespaceAndPath("dedsafio-4-mechanics", "palitomagico"), () -> new f(new Properties())
   );
   /** Tótem Eon */
   public static final Supplier<Item> i = a("totem_eon", () -> new b(new Properties().stacksTo(1), "item.dedsafio-4-mechanics.totem_eon.tooltip"));
   /** Tótem Flor */
   public static final Supplier<Item> j = a("totem_flor", () -> new b(new Properties().stacksTo(1), "item.dedsafio-4-mechanics.totem_flor.tooltip"));
   /** Tótem Gólem */
   public static final Supplier<Item> k = a("totem_golem", () -> new b(new Properties().stacksTo(1), "item.dedsafio-4-mechanics.totem_golem.tooltip"));
   /** Corazón del Limbo: resetea a cero los corazones adicionales acumulados */
   public static final Supplier<Item> l = a("corazon_limbo", () -> new e(b()));
   /** Bolsa Primitiva: mochila portátil de capacidad básica */
   public static final Supplier<Item> m = a("bolsa_primitiva", () -> new h(false, new Properties().stacksTo(1)));
   /** Ender Bag: mochila portátil avanzada vinculada al jugador */
   public static final Supplier<Item> n = a("ender_bag", () -> new h(true, new Properties().stacksTo(1)));
   /** Corazón Rojo: otorga permanentemente +2 HP (un corazón completo) de salud máxima */
   public static final Supplier<Item> o = a("corazon_rojo", () -> new d(b()));
   /** Corazón Dorado: concede absorción temporal prolongada */
   public static final Supplier<Item> p = a("corazon_dorado", () -> new c(b()));
   /** Guía de Administrador con manual interactivo paginado */
   public static final Supplier<Item> t = a("admin_guide", () -> new AdminGuideItem(new Properties().stacksTo(1)));
   /** Pestaña oficial del Modo Creativo para Dedsafio 4 */
   public static final Supplier<CreativeModeTab> q = ForgeRegistration.registerEager(
      Registries.CREATIVE_MODE_TAB,
      a,
      () -> FabricItemGroup.builder()
            .icon(() -> new ItemStack(Items.TOTEM_OF_UNDYING))
            .title(Component.translatable("itemGroup.dedsafio-4-mechanics.nutritotem_group"))
            .displayItems((displayContext, entries) -> {
               entries.accept(Items.TOTEM_OF_UNDYING);
               entries.accept((ItemLike)c.get());
               entries.accept((ItemLike)d.get());
               entries.accept((ItemLike)e.get());
               entries.accept((ItemLike)f.get());
               entries.accept((ItemLike)i.get());
               entries.accept((ItemLike)j.get());
               entries.accept((ItemLike)k.get());
               entries.accept((ItemLike)l.get());
               entries.accept((ItemLike)m.get());
               entries.accept((ItemLike)n.get());
               entries.accept((ItemLike)o.get());
               entries.accept((ItemLike)p.get());
               entries.accept((ItemLike)g2.get());
               entries.accept((ItemLike)h.get());
               entries.accept(com.eric.revill.m.h.b);
               entries.accept(com.eric.revill.o.e.b);
               entries.accept((ItemLike)t.get());
            })
            .build()
   );

   private g() {
   }

   public static void a() {
      ItemGroupEvents.modifyEntriesEvent(b).register((ModifyEntries)entries -> {
         entries.accept(Items.TOTEM_OF_UNDYING);
         entries.accept((ItemLike)c.get());
         entries.accept((ItemLike)d.get());
         entries.accept((ItemLike)e.get());
         entries.accept((ItemLike)f.get());
         entries.accept((ItemLike)i.get());
         entries.accept((ItemLike)j.get());
         entries.accept((ItemLike)k.get());
         entries.accept((ItemLike)l.get());
         entries.accept((ItemLike)m.get());
         entries.accept((ItemLike)n.get());
         entries.accept((ItemLike)o.get());
         entries.accept((ItemLike)p.get());
         entries.accept((ItemLike)g2.get());
         entries.accept((ItemLike)h.get());
         entries.accept(com.eric.revill.m.h.b);
         entries.accept(com.eric.revill.o.e.b);
         entries.accept((ItemLike)t.get());
      });
      ServerTickEvents.END_SERVER_TICK.register((EndTick)server -> {
         for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            a(player);
         }
      });
   }

   public static boolean a(ItemStack stack) {
      return stack.is(Items.TOTEM_OF_UNDYING) || stack.is(i.get()) || stack.is(j.get()) || stack.is(k.get());
   }

   public static boolean a(LivingEntity entity, DamageSource source) {
      if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
         return false;
      } else {
         ItemStack usedTotem = null;

         for (InteractionHand hand : InteractionHand.values()) {
            ItemStack heldStack = entity.getItemInHand(hand);
            if (a(heldStack)) {
               usedTotem = heldStack;
               break;
            }
         }

         if (usedTotem == null && entity instanceof Player player) {
            for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++) {
               ItemStack stack = player.getInventory().getItem(slot);
               if (a(stack)) {
                  usedTotem = stack;
                  break;
               }
            }
         }

         if (usedTotem == null) {
            return false;
         } else {
            ItemStack consumedTotem = usedTotem.copy();
            usedTotem.shrink(1);
            if (entity instanceof ServerPlayer player) {
               FriendlyByteBuf buf = PacketByteBufs.create();
               buf.writeUtf(BuiltInRegistries.ITEM.getKey(consumedTotem.getItem()).toString(), 128);
               ServerPlayNetworking.send(player, com.eric.revill.p.a.k, buf);
            }

            entity.level().broadcastEntityEvent(entity, (byte)35);
            if (entity instanceof ServerPlayer player) {
               player.awardStat(Stats.ITEM_USED.get(consumedTotem.getItem()));
               a(player);
               com.eric.revill.l.i.a(player, consumedTotem);
            }

            entity.setHealth(1.0F);
            entity.removeAllEffects();
            entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 900, 1));
            entity.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1));
            entity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 800, 0));
            if (consumedTotem.is(j.get())) {
               entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 20, 4));
            }

            return true;
         }
      }
   }

   public static void a(ServerPlayer player) {
      if (player != null) {
         AttributeInstance maxHealth = player.getAttribute(Attributes.MAX_HEALTH);
         if (maxHealth != null) {
            int redHearts = ((com.eric.revill.q.a)player).dedsafio4$getRedHearts();
            a(maxHealth, r, (double)redHearts * 2.0);
            boolean hasGolemTotem = false;

            for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++) {
               if (player.getInventory().getItem(slot).is(k.get())) {
                  hasGolemTotem = true;
                  break;
               }
            }

            a(maxHealth, s, hasGolemTotem ? 4.0 : 0.0);
            player.setHealth(Math.min(player.getHealth(), player.getMaxHealth()));
         }
      }
   }

   public static void b(ServerPlayer player) {
      ((com.eric.revill.q.a)player).dedsafio4$setRedHearts(0);
      a(player);
   }

   private static void a(AttributeInstance attribute, ResourceLocation id, double amount) {
      AttributeModifier existing = attribute.getModifier(id);
      if (existing == null || existing.amount() != amount) {
         if (existing != null) {
            attribute.removeModifier(id);
         }

         if (amount > 0.0) {
            attribute.addPermanentModifier(new AttributeModifier(id, amount, Operation.ADD_VALUE));
         }
      }
   }

   private static Supplier<Item> a(String path, Supplier<Item> supplier) {
      return ForgeRegistration.registerEager(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("dedsafio-4-mechanics", path), supplier);
   }

   private static Properties b() {
      FoodProperties food = new Builder().nutrition(0).saturationModifier(0.0F).alwaysEdible().build();
      return new Properties().food(food);
   }
}
