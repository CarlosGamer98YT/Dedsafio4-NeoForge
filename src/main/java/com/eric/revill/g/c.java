package com.eric.revill.g;

import java.util.ArrayList;
import java.util.Map;
import java.util.WeakHashMap;
import com.eric.revill.fabric.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import com.eric.revill.fabric.fabric.api.entity.event.v1.ServerLivingEntityEvents.AfterDeath;
import com.eric.revill.fabric.fabric.api.entity.event.v1.ServerLivingEntityEvents.AllowDamage;
import com.eric.revill.fabric.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import com.eric.revill.fabric.fabric.api.event.lifecycle.v1.ServerTickEvents;
import com.eric.revill.fabric.fabric.api.event.lifecycle.v1.ServerEntityEvents.Load;
import com.eric.revill.fabric.fabric.api.event.lifecycle.v1.ServerTickEvents.EndWorldTick;
import com.eric.revill.fabric.fabric.api.event.player.UseBlockCallback;
import com.eric.revill.fabric.fabric.api.event.player.UseEntityCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ambient.AmbientCreature;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.monster.CaveSpider;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.monster.Stray;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.GameRules.BooleanValue;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.TrapDoorBlock;

public final class c {
   private static final ThreadLocal<Boolean> a = ThreadLocal.withInitial(() -> false);
   private static final ThreadLocal<Boolean> b = ThreadLocal.withInitial(() -> false);
   private static final Map<ServerLevel, Boolean> c = new WeakHashMap<>();

   private c() {
   }

   public static void a() {
      f();
      g();
      h();
      j();
      i();
      k();
      l();
      m();
      n();
      o();
      p();
      q();
      r();
      s();
      t();
      u();
      c();
      d();
      e();
      b();
   }

   private static void b() {
      ServerEntityEvents.ENTITY_LOAD.register((Load)(entity, world) -> a(entity));
      ServerTickEvents.END_WORLD_TICK
         .register(
            (EndWorldTick)world -> {
               if (world.getGameTime() % 20L == 0L) {
                  for (Entity entity : world.getAllEntities()) {
                     a(entity);
                     if (com.eric.revill.g.a.a("dia7_fuego_infinito_nether") && world.dimension().equals(Level.NETHER) && entity.isOnFire()) {
                        entity.igniteForSeconds(1000000);
                     }
                  }
               }

               if (com.eric.revill.g.a.a("dia7_sin_regeneracion_natural")) {
                  c.putIfAbsent(world, world.getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION));
                  ((BooleanValue)world.getGameRules().getRule(GameRules.RULE_NATURAL_REGENERATION)).set(false, world.getServer());
               } else {
                  Boolean original = c.remove(world);
                  if (original != null) {
                     ((BooleanValue)world.getGameRules().getRule(GameRules.RULE_NATURAL_REGENERATION)).set(original, world.getServer());
                  }
               }

               if (com.eric.revill.g.a.a("dia7_aranas_cueva_overworld")
                  && world.dimension().equals(Level.OVERWORLD)
                  && world.isNight()
                  && world.getGameTime() % 60L == 0L) {
                  for (ServerPlayer player : world.players()) {
                     CaveSpider spider;
                     BlockPos spawnPos;
                     if (world.getRandom().nextInt(10) == 0
                        && (spawnPos = a(world, player.blockPosition().offset(world.getRandom().nextIntBetweenInclusive(-24, 24), 0, world.getRandom().nextIntBetweenInclusive(-24, 24))))
                           != null
                        && (spider = (CaveSpider)EntityType.CAVE_SPIDER.create(world)) != null) {
                        spider.moveTo((double)spawnPos.getX() + 0.5, (double)spawnPos.getY(), (double)spawnPos.getZ() + 0.5, 0.0F, 0.0F);
                        spider.finalizeSpawn(world, world.getCurrentDifficultyAt(spawnPos), MobSpawnType.NATURAL, null);
                        world.addFreshEntityWithPassengers(spider);
                        a(spider);
                     }
                  }
               }
            }
         );
      ServerLivingEntityEvents.ALLOW_DAMAGE
         .register(
            (AllowDamage)(entity, source, amount) -> {
               if (b.get()) {
                  return true;
               } else {
                  float multiplier = 1.0F;
                  if (com.eric.revill.g.a.a("dia7_dano_caida_doble") && source.is(DamageTypes.FALL)) {
                     multiplier = 2.0F;
                  } else if (com.eric.revill.g.a.a("dia7_ahogamiento_aumentado") && source.is(DamageTypes.DROWN)) {
                     multiplier = 10.0F;
                  } else if (com.eric.revill.g.a.a("dia7_fuego_lava_x10")
                     && (source.is(DamageTypes.IN_FIRE) || source.is(DamageTypes.ON_FIRE) || source.is(DamageTypes.LAVA))) {
                     multiplier = 10.0F;
                  }

                  if (multiplier == 1.0F) {
                     return true;
                  } else {
                     b.set(true);

                     try {
                        entity.hurt(source, Math.min(Float.MAX_VALUE, amount * multiplier));
                     } finally {
                        b.set(false);
                     }

                     return false;
                  }
               }
            }
         );
      UseBlockCallback.EVENT.register((UseBlockCallback)(player, world, hand, hitResult) -> {
         if (!world.isClientSide && player instanceof ServerPlayer serverPlayer && com.eric.revill.g.a.a("dia7_dano_boton")) {
            Block block = world.getBlockState(hitResult.getBlockPos()).getBlock();
            if (block instanceof ButtonBlock) {
               serverPlayer.hurt(world.damageSources().generic(), 4.0E27F);
            }

            return InteractionResult.PASS;
         }

         return InteractionResult.PASS;
      });
      ServerLivingEntityEvents.ALLOW_DAMAGE.register((AllowDamage)(entity, source, amount) -> {
         if (com.eric.revill.g.a.a("dia7_aranas_telaranas") && source.getEntity() instanceof Spider && amount > 0.0F && !entity.level().isClientSide) {
            BlockPos pos = entity.blockPosition();
            if (entity.level().getBlockState(pos).canBeReplaced()) {
               entity.level().setBlockAndUpdate(pos, Blocks.COBWEB.defaultBlockState());
            }
         }

         return true;
      });
      ServerLivingEntityEvents.AFTER_DEATH.register((AfterDeath)(entity, source) -> {
         if (com.eric.revill.g.a.a("dia7_aranas_telaranas") && entity instanceof Spider && !entity.level().isClientSide) {
            BlockPos pos = entity.blockPosition();
            if (entity.level().getBlockState(pos).canBeReplaced()) {
               entity.level().setBlockAndUpdate(pos, Blocks.COBWEB.defaultBlockState());
            }
         }
      });
   }

   private static void a(Entity entity) {
      if (entity instanceof Spider spider) {
         if (com.eric.revill.g.a.a("dia7_aranas_cueva_overworld")) {
            spider.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 40, 0, false, false, false));
         }

         if (com.eric.revill.g.a.a("dia7_aranas_fuerza")) {
            spider.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 40, 2, false, false, false));
         }
      }
   }

   public static void a(MinecraftServer server) {
      if (server != null) {
         for (ServerLevel world : server.getAllLevels()) {
            ArrayList<Entity> animals = new ArrayList<>();

            for (Entity entity : world.getAllEntities()) {
               if (b(entity)) {
                  animals.add(entity);
               }
            }

            animals.forEach(Entity::discard);
         }
      }
   }

   private static void c() {
      ServerTickEvents.END_WORLD_TICK
         .register(
            (EndWorldTick)world -> {
               if (com.eric.revill.g.a.a("dia6_wither_skeletons_nether")) {
                  ServerLevel serverWorld = world;
                  if (world.dimension().equals(Level.NETHER) && world.getGameTime() % 60L == 0L) {
                     for (ServerPlayer player : world.players()) {
                        WitherSkeleton skeleton;
                        BlockPos spawnPos;
                        if (serverWorld.getRandom().nextInt(10) == 0
                           && (
                                 spawnPos = a(
                                    serverWorld,
                                    player.blockPosition().offset(serverWorld.getRandom().nextIntBetweenInclusive(-24, 24), 0, serverWorld.getRandom().nextIntBetweenInclusive(-24, 24))
                                 )
                              )
                              != null
                           && (skeleton = (WitherSkeleton)EntityType.WITHER_SKELETON.create(serverWorld)) != null) {
                           skeleton.moveTo((double)spawnPos.getX() + 0.5, (double)spawnPos.getY(), (double)spawnPos.getZ() + 0.5, 0.0F, 0.0F);
                           skeleton.finalizeSpawn(serverWorld, serverWorld.getCurrentDifficultyAt(spawnPos), MobSpawnType.NATURAL, null);
                           serverWorld.addFreshEntityWithPassengers(skeleton);
                        }
                     }
                  }
               }
            }
         );
   }

   private static void d() {
      ServerLivingEntityEvents.ALLOW_DAMAGE
         .register(
            (AllowDamage)(entity, source, amount) -> {
               if (a.get()) {
                  return true;
               } else if (com.eric.revill.g.a.a("dia6_fuego_lava_triple")
                  && (source.is(DamageTypes.IN_FIRE) || source.is(DamageTypes.ON_FIRE) || source.is(DamageTypes.LAVA))) {
                  a.set(true);

                  try {
                     entity.hurt(source, amount * 3.0F);
                  } finally {
                     a.set(false);
                  }

                  return false;
               } else {
                  return true;
               }
            }
         );
   }

   private static void e() {
      ServerEntityEvents.ENTITY_LOAD.register((Load)(entity, world) -> {
         if (com.eric.revill.g.a.a("dia6_animales_extintos") && b(entity)) {
            entity.discard();
         }
      });
   }

   private static boolean b(Entity entity) {
      return entity instanceof Animal || entity instanceof WaterAnimal || entity instanceof AmbientCreature;
   }

   private static void f() {
   }

   private static void g() {
   }

   private static void h() {
      UseBlockCallback.EVENT.register((UseBlockCallback)(player, world, hand, hitResult) -> {
         if (world.isClientSide) {
            return InteractionResult.PASS;
         } else if (!com.eric.revill.g.a.a("dia1_no_leche")) {
            return InteractionResult.PASS;
         } else if (!(player instanceof ServerPlayer sp)) {
            return InteractionResult.PASS;
         } else {
            BlockPos pos = hitResult.getBlockPos();
            return InteractionResult.PASS;
         }
      });
      UseEntityCallback.EVENT.register((UseEntityCallback)(player, world, hand, entity, hitResult) -> {
         if (world.isClientSide) {
            return InteractionResult.PASS;
         } else if (!com.eric.revill.g.a.a("dia1_no_leche")) {
            return InteractionResult.PASS;
         } else if (!(entity instanceof Cow)) {
            return InteractionResult.PASS;
         } else {
            ItemStack stack = player.getItemInHand(hand);
            if (stack.is(Items.BUCKET)) {
               player.displayClientMessage(Component.literal("§cLas vacas ya no producen leche."), true);
               return InteractionResult.FAIL;
            } else {
               return InteractionResult.PASS;
            }
         }
      });
   }

   private static void i() {
   }

   private static void j() {
      UseBlockCallback.EVENT.register((UseBlockCallback)(player, world, hand, hitResult) -> {
         if (world.isClientSide || !com.eric.revill.g.a.a("dia1_respirar_bajo_agua")) {
            return InteractionResult.PASS;
         } else if (!player.isUnderWater()) {
            return InteractionResult.PASS;
         } else {
            ItemStack stack = player.getItemInHand(hand);
            if (!(stack.getItem() instanceof BlockItem blockItem)) {
               return InteractionResult.PASS;
            } else {
               Block block = blockItem.getBlock();
               return !(block instanceof DoorBlock) && !(block instanceof TrapDoorBlock) ? InteractionResult.PASS : InteractionResult.FAIL;
            }
         }
      });
   }

   private static void k() {
   }

   private static void l() {
      ServerEntityEvents.ENTITY_LOAD.register((Load)(entity, world) -> {
         if (com.eric.revill.g.a.a("dia2_zombies_trepadores") || com.eric.revill.g.a.a("dia4_zombies_mejorados")) {
            if (entity instanceof Zombie zombie) {
               if (!(zombie instanceof Drowned) && !(zombie instanceof ZombieVillager)) {
                  boolean sunImmune;
                  boolean climber;
                  if (com.eric.revill.g.a.a("dia4_zombies_mejorados")) {
                     climber = true;
                     sunImmune = true;
                  } else {
                     sunImmune = climber = world.getRandom().nextBoolean();
                  }

                  if (climber) {
                     zombie.setCanPickUpLoot(true);
                  }

                  if (sunImmune) {
                     zombie.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false, false));
                  }
               }
            }
         }
      });
   }

   private static void m() {
      ServerTickEvents.END_WORLD_TICK
         .register(
            (EndWorldTick)world -> {
               if (com.eric.revill.g.a.a("dia2_blazes_nether")) {
                  ServerLevel sw = world;
                  if (world.dimension().equals(Level.NETHER)) {
                     if (world.getGameTime() % 60L == 0L) {
                        for (ServerPlayer player : world.players()) {
                           Blaze blaze;
                           BlockPos spawnPos;
                           if (sw.getRandom().nextInt(10) == 0
                              && (spawnPos = a(sw, player.blockPosition().offset(sw.getRandom().nextIntBetweenInclusive(-24, 24), 0, sw.getRandom().nextIntBetweenInclusive(-24, 24)))) != null
                              && (blaze = (Blaze)EntityType.BLAZE.create(sw)) != null) {
                              blaze.moveTo((double)spawnPos.getX() + 0.5, (double)spawnPos.getY(), (double)spawnPos.getZ() + 0.5, 0.0F, 0.0F);
                              blaze.finalizeSpawn(sw, sw.getCurrentDifficultyAt(spawnPos), MobSpawnType.NATURAL, null);
                              sw.addFreshEntityWithPassengers(blaze);
                           }
                        }
                     }
                  }
               }
            }
         );
   }

   private static void n() {
      ServerTickEvents.END_WORLD_TICK.register((EndWorldTick)world -> {
         if (com.eric.revill.g.a.a("dia5_zombies_nether")) {
            ServerLevel sw = world;
            if (world.dimension().equals(Level.NETHER)) {
               if (world.getGameTime() % 40L == 0L) {
                  for (ServerPlayer player : world.players()) {
                     if (sw.getRandom().nextInt(10) == 0) {
                        int offsetX = sw.getRandom().nextIntBetweenInclusive(-24, 24);
                        int offsetZ = sw.getRandom().nextIntBetweenInclusive(-24, 24);
                        BlockPos spawnPos = player.blockPosition().offset(offsetX, 0, offsetZ);
                        Zombie zombie;
                        if ((spawnPos = a(sw, spawnPos)) != null && (zombie = (Zombie)EntityType.ZOMBIE.create(sw)) != null) {
                           zombie.moveTo((double)spawnPos.getX() + 0.5, (double)spawnPos.getY(), (double)spawnPos.getZ() + 0.5, 0.0F, 0.0F);
                           zombie.finalizeSpawn(sw, sw.getCurrentDifficultyAt(spawnPos), MobSpawnType.SPAWNER, null);
                           sw.addFreshEntityWithPassengers(zombie);
                        }
                     }
                  }
               }
            }
         }
      });
   }

   private static void o() {
      ServerEntityEvents.ENTITY_LOAD.register((Load)(entity, world) -> {
         if (com.eric.revill.g.a.a("dia4_zombies_mejorados")) {
            if (entity instanceof Zombie zombie) {
               if (!(zombie instanceof Drowned) && !(zombie instanceof ZombieVillager)) {
                  zombie.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false, false));
                  ItemStack sword = new ItemStack(Items.IRON_SWORD);
                  sword.enchant(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.SHARPNESS), 5);
                  zombie.setItemSlot(EquipmentSlot.MAINHAND, sword);
               }
            }
         }
      });
   }

   private static void p() {
      ServerTickEvents.END_WORLD_TICK.register((EndWorldTick)world -> {
         if (com.eric.revill.g.a.a("dia3_magma_letal")) {
            ServerLevel sw = world;

            for (ServerPlayer player : world.players()) {
               if (!player.isSpectator() && !player.isCreative()) {
                  BlockPos pos = player.blockPosition();

                  for (BlockPos check : new BlockPos[]{pos, pos.below(), pos.above(), pos.north(), pos.south(), pos.east(), pos.west()}) {
                     if (sw.getBlockState(check).is(Blocks.MAGMA_BLOCK)) {
                        player.hurt(sw.damageSources().hotFloor(), Float.MAX_VALUE);
                        break;
                     }
                  }
               }
            }
         }
      });
   }

   private static void q() {
      ServerTickEvents.END_WORLD_TICK
         .register(
            (EndWorldTick)world -> {
               if (com.eric.revill.g.a.a("dia5_bogged_noche")) {
                  ServerLevel sw = world;
                  if (!world.dimension().equals(Level.NETHER)) {
                     if (!world.dimension().equals(Level.END)) {
                        if (world.isNight()) {
                           if (world.getGameTime() % 60L == 0L) {
                              for (ServerPlayer player : world.players()) {
                                 if (sw.getRandom().nextInt(8) == 0) {
                                    int offsetX = sw.getRandom().nextIntBetweenInclusive(-20, 20);
                                    int offsetZ = sw.getRandom().nextIntBetweenInclusive(-20, 20);
                                    BlockPos spawnPos = a(sw, player.blockPosition().offset(offsetX, 0, offsetZ));
                                    Skeleton bogged;
                                    if (spawnPos != null && (bogged = (Skeleton)EntityType.SKELETON.create(sw)) != null) {
                                       bogged.addTag("dedsafio4_bogged");
                                       bogged.moveTo(
                                          (double)spawnPos.getX() + 0.5, (double)spawnPos.getY(), (double)spawnPos.getZ() + 0.5, 0.0F, 0.0F
                                       );
                                       bogged.finalizeSpawn(sw, sw.getCurrentDifficultyAt(spawnPos), MobSpawnType.NATURAL, null);
                                       sw.addFreshEntityWithPassengers(bogged);
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         );
   }

   private static void r() {
      ServerTickEvents.END_WORLD_TICK.register((EndWorldTick)world -> {
         if (com.eric.revill.g.a.a("dia5_stray_noche")) {
            ServerLevel sw = world;
            if (world.dimension().equals(Level.OVERWORLD)) {
               if (world.isNight()) {
                  if (world.getGameTime() % 60L == 0L) {
                     for (ServerPlayer player : world.players()) {
                        if (sw.getRandom().nextInt(8) == 0) {
                           int offsetX = sw.getRandom().nextIntBetweenInclusive(-20, 20);
                           int offsetZ = sw.getRandom().nextIntBetweenInclusive(-20, 20);
                           BlockPos spawnPos = a(sw, player.blockPosition().offset(offsetX, 0, offsetZ));
                           Stray stray;
                           if (spawnPos != null && (stray = (Stray)EntityType.STRAY.create(sw)) != null) {
                              stray.moveTo((double)spawnPos.getX() + 0.5, (double)spawnPos.getY(), (double)spawnPos.getZ() + 0.5, 0.0F, 0.0F);
                              stray.finalizeSpawn(sw, sw.getCurrentDifficultyAt(spawnPos), MobSpawnType.NATURAL, null);
                              sw.addFreshEntityWithPassengers(stray);
                           }
                        }
                     }
                  }
               }
            }
         }
      });
   }

   private static void s() {
      ServerTickEvents.END_WORLD_TICK.register((EndWorldTick)world -> {
         if (com.eric.revill.g.a.a("dia5_luna_mata")) {
            ServerLevel sw = world;
            if (world.dimension().equals(Level.OVERWORLD)) {
               if (world.isNight()) {
                  if (world.getGameTime() % 5L == 0L) {
                     for (ServerPlayer player : world.players()) {
                        if (!player.isSpectator() && !player.isCreative() && !(player.getXRot() > -40.0F) && !sw.getBlockState(player.blockPosition().above()).canOcclude()) {
                           player.hurt(sw.damageSources().magic(), Float.MAX_VALUE);
                           com.eric.revill.g.a.b("dia5_luna_mata");
                           break;
                        }
                     }
                  }
               }
            }
         }
      });
   }

   private static void t() {
      ServerEntityEvents.ENTITY_LOAD.register((Load)(entity, world) -> {
      });
      ServerTickEvents.END_WORLD_TICK
         .register(
            (EndWorldTick)world -> {
               if (com.eric.revill.g.a.a("dia1_wardens_deep_dark")) {
                  ServerLevel sw = world;
                  if (world.dimension().equals(Level.OVERWORLD)) {
                     if (world.getGameTime() % 300L == 0L) {
                        for (ServerPlayer player : world.players()) {
                           Warden warden;
                           BlockPos spawnPos;
                           if (!player.isSpectator()
                              && !player.isCreative()
                              && sw.getBiome(player.blockPosition()).is(Biomes.DEEP_DARK)
                              && sw.getRandom().nextInt(5) == 0
                              && (spawnPos = a(sw, player.blockPosition().offset(sw.getRandom().nextIntBetweenInclusive(-15, 15), 0, sw.getRandom().nextIntBetweenInclusive(-15, 15)))) != null
                              && sw.getBiome(spawnPos).is(Biomes.DEEP_DARK)
                              && (warden = (Warden)EntityType.WARDEN.create(sw)) != null) {
                              warden.moveTo((double)spawnPos.getX() + 0.5, (double)spawnPos.getY(), (double)spawnPos.getZ() + 0.5, 0.0F, 0.0F);
                              sw.addFreshEntityWithPassengers(warden);
                           }
                        }
                     }
                  }
               }
            }
         );
   }

   private static void u() {
   }

   private static BlockPos a(ServerLevel world, BlockPos start) {
      int topY = Math.min(world.getMaxBuildHeight(), start.getY() + 8);
      BlockPos check = new BlockPos(start.getX(), topY, start.getZ());

      for (int dy = 0; dy < 20; dy++) {
         BlockPos above = check.above(dy);
         BlockPos at = check.above(dy + 1);
         if (world.getBlockState(above).isRedstoneConductor(world, above) && !world.getBlockState(at).isRedstoneConductor(world, at)) {
            return at;
         }
      }

      for (int var8 = 0; var8 > -30; var8--) {
         BlockPos below = start.above(var8 - 1);
         BlockPos at = start.above(var8);
         if (world.getBlockState(below).isRedstoneConductor(world, below) && !world.getBlockState(at).isRedstoneConductor(world, at)) {
            return at;
         }
      }

      return null;
   }
}
