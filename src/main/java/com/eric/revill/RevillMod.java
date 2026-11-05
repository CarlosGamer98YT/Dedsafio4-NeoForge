package com.eric.revill;

import com.eric.revill.f.a;
import com.eric.revill.h.d;
import com.eric.revill.i.b;
import com.eric.revill.l.AdminGuideItem;
import com.eric.revill.l.g;
import com.eric.revill.m.c;
import com.eric.revill.m.h;
import com.eric.revill.mixin.CreeperEntityChargedAccessor;
import com.eric.revill.o.e;
import com.eric.revill.fabric.fabric.api.command.v2.CommandRegistrationCallback;
import com.eric.revill.fabric.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import com.eric.revill.fabric.fabric.api.entity.event.v1.ServerLivingEntityEvents.AfterDeath;
import com.eric.revill.fabric.fabric.api.entity.event.v1.ServerLivingEntityEvents.AllowDeath;
import com.eric.revill.fabric.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import com.eric.revill.fabric.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import com.eric.revill.fabric.fabric.api.event.lifecycle.v1.ServerTickEvents;
import com.eric.revill.fabric.fabric.api.event.lifecycle.v1.ServerEntityEvents.Load;
import com.eric.revill.fabric.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.ServerStarted;
import com.eric.revill.fabric.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.ServerStopping;
import com.eric.revill.fabric.fabric.api.event.lifecycle.v1.ServerTickEvents.EndTick;
import com.eric.revill.fabric.fabric.api.event.player.PlayerBlockBreakEvents;
import com.eric.revill.fabric.fabric.api.event.player.PlayerBlockBreakEvents.After;
import com.eric.revill.fabric.fabric.api.event.player.UseBlockCallback;
import com.eric.revill.fabric.fabric.api.event.player.UseEntityCallback;
import com.eric.revill.fabric.fabric.api.itemgroup.v1.ItemGroupEvents;
import com.eric.revill.fabric.fabric.api.networking.v1.ModChannelPayload;
import com.eric.revill.fabric.fabric.api.networking.v1.ServerPlayConnectionEvents;
import com.eric.revill.fabric.fabric.api.networking.v1.ServerPlayNetworking;
import com.eric.revill.fabric.fabric.api.networking.v1.ServerPlayConnectionEvents.Join;
import com.eric.revill.fabric.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Clase principal del mod DEDSAFIO 4 Mechanics para Minecraft 1.21.1 en NeoForge.
 * 
 * Gestiona el ciclo de vida del mod, el registro de eventos en el ModBus y el GameBus,
 * la inicialización de subsistemas (economía, retos, misiones, waypoints, buzón,
 * evento REVILL) y la sincronización de paquetes de red cliente-servidor.
 */
@Mod("dedsafio_4_mechanics")
public class RevillMod {
   /** ID del mod. */
   public static final String a = "dedsafio-4-mechanics";

   /** Logger para el registro de mensajes en consola. */
   public static final Logger b = LoggerFactory.getLogger("dedsafio-4-mechanics");

   /** Configuración cargada desde config/revill.json. */
   public static final a c = com.eric.revill.f.a.a();

   /** Indica si la cinemática o secuencia de Fusión está activa en el servidor. */
   public static boolean d = false;

   /**
    * Constructor principal del mod invocado por el cargador de NeoForge.
    * 
    * @param modEventBus Bus de eventos de ciclo de vida del mod (ModBus).
    * @param modContainer Contenedor de metadatos del mod.
    */
   public RevillMod(IEventBus modEventBus, ModContainer modContainer) {
      // 1. Registro de bloques, ítems, menús y pestañas
      modEventBus.addListener(ForgeRegistration::onRegister);
      // 2. Registro de atributos de entidades personalizadas (ej. Cajero Automático)
      modEventBus.addListener(com.eric.revill.i.b::registerAttributes);
      // 3. Registro de canales de red (Payloads)
      modEventBus.addListener(this::registerPayloads);
      // 4. Registro de ítems en pestañas creativas (con prevención de duplicados)
      modEventBus.addListener(this::onBuildCreativeTabs);
      // 5. Registro de entidades
      com.eric.revill.i.b.register(modEventBus);

      // 6. Inicializar lógica interna del mod común (servidor y cliente)
      this.onInitialize();

      // 7. Registrar listeners del bus del juego (eventos de tick, daño, chat, etc.)
      NeoForge.EVENT_BUS.register(this);

      // 8. Inicialización específica del cliente si aplica
      if (FMLEnvironment.dist == Dist.CLIENT) {
         RevillClient.onInitializeClient(modEventBus);
      }
   }

   /**
    * Registra el payload de red bidireccional de NeoForge 1.21.1
    * para interconectar los canales de red estilo Fabric con el sistema de paquetes de red de NeoForge.
    */
   private void registerPayloads(RegisterPayloadHandlersEvent event) {
      PayloadRegistrar registrar = event.registrar("1").optional();
      registrar.playBidirectional(
         ModChannelPayload.TYPE,
         ModChannelPayload.STREAM_CODEC,
         (payload, context) -> {
            if (context.flow().isServerbound()) {
               context.enqueueWork(() -> {
                  ServerPlayer player = (ServerPlayer) context.player();
                  MinecraftServer server = player.getServer();
                  ServerPlayNetworking.handle(server, player, payload.channel(), payload.data());
               });
            } else {
               context.enqueueWork(() -> {
                  ClientPlayNetworking.handle(payload.channel(), payload.data());
               });
            }
         }
      );
   }

   /**
    * Inicializa todos los subsistemas del mod (recetas, sonidos, bloques, ítems,
    * economía, retos diarios, eventos de ciclo de vida, paquetes de red y persistencia).
    */
   public void onInitialize() {
      b.info("Iniciando DEDSAFIO 4 Mechanics para Minecraft 1.21.1 NeoForge");

      // Inicialización de subsistemas individuales
      com.eric.revill.r.a.a(); // Recetas de crafteo
      com.eric.revill.h.a.a(); // Menú del Cajero Automático
      h.a();                   // Bloques y BlockEntities del Buzón
      e.a();                   // Bloque Pedestal de Desafíos Diarios
      com.eric.revill.m.c.b(); // Menú de lectura de cartas del Buzón
      com.eric.revill.m.b.b(); // Menú de envío de cartas del Buzón
      com.eric.revill.a.a.a(); // Menú de la Mochila Portátil (Ender Bag)
      g.a();                   // Registro de ítems y pestaña creativa
      com.eric.revill.g.c.a(); // Registro del catálogo de retos de dificultad
      com.eric.revill.h.d.a(); // Canales de sonido
      com.eric.revill.m.e.a(); // Canales de red del buzón
      com.eric.revill.o.h.a(); // Canales de red de sincronización de misiones
      com.eric.revill.o.c.a(); // Canales de red de pedestales
      com.eric.revill.k.b.a(); // Canales de red de gremios / clanes
      com.eric.revill.c.b.a(); // Notificador de desconexión de jugadores
      com.eric.revill.s.a.a(); // Sistema de globos de texto flotantes sobre jugadores

      // Listener de muerte de entidades (actualiza progreso de misiones)
      ServerLivingEntityEvents.AFTER_DEATH.register((AfterDeath)(entity, source) -> {
         if (source.getEntity() instanceof ServerPlayer player) {
            com.eric.revill.o.g.a(player, entity);
         }
      });

      // Listener de rotura de bloques (actualiza misiones de minería)
      PlayerBlockBreakEvents.AFTER.register((After)(world, player, pos, state, blockEntity) -> com.eric.revill.o.g.a(player, state.getBlock()));

      // Registro de comandos /dedsafio4, /revill y /fusion
      CommandRegistrationCallback.EVENT.register((CommandRegistrationCallback)(dispatcher, registryAccess, environment) -> {
         com.eric.revill.e.c.a(dispatcher); // /revill
         com.eric.revill.e.a.a(dispatcher); // /dedsafio4
         com.eric.revill.e.b.a(dispatcher); // /fusion
      });

      // Evento de servidor iniciado: carga estados guardados (REVILL, misiones, waypoints, gremios)
      ServerLifecycleEvents.SERVER_STARTED.register((ServerStarted)server -> {
         com.eric.revill.j.a.a.d(server); // Estado del evento REVILL
         com.eric.revill.o.g.a(server);    // Carga de misiones diarias
         com.eric.revill.t.b.a(server);    // Carga de waypoints
         com.eric.revill.k.a.a(server);    // Carga de gremios
      });

      // Evento de servidor deteniéndose: guarda estados pendientes
      ServerLifecycleEvents.SERVER_STOPPING.register((ServerStopping)server -> com.eric.revill.j.a.a.f(server));

      // Evento de fin de tick del servidor: actualiza temporizadores de REVILL y retos diarios
      ServerTickEvents.END_SERVER_TICK.register((EndTick)server -> {
         com.eric.revill.j.a.a.c(server); // Actualiza cuenta atrás y tareas de REVILL
         com.eric.revill.g.a.d(server);    // Actualiza efectos de los retos activos
      });

      // Evento al cargar entidad: aplica efectos especiales de retos (ej. Creepers electrificados en Día 1)
      ServerEntityEvents.ENTITY_LOAD.register((Load)(entity, world) -> {
         if (entity instanceof Creeper creeper && com.eric.revill.g.a.a("dia1_creepers_electricos")) {
            creeper.getEntityData().set(CreeperEntityChargedAccessor.dedsafio4$getChargedData(), true);
         }
      });

      // Evento de permiso de muerte: permite o bloquea la muerte del jugador (gestión de vidas y modo espectador)
      ServerLivingEntityEvents.ALLOW_DEATH.register((AllowDeath)(entity, source, damageAmount) -> {
         if (entity instanceof ServerPlayer player) {
            boolean bypasses = source.is(DamageTypeTags.BYPASSES_INVULNERABILITY);
            return !bypasses && g.a(player, source) ? false : !com.eric.revill.g.a.a(player, source, damageAmount);
         } else {
            return true;
         }
      });

      // Receptores de red del servidor para la gestión de configuración y retos
      ServerPlayNetworking.registerGlobalReceiver(com.eric.revill.g.b.b, (server, player, handler, buf, responseSender) -> {
         if (!player.hasPermissions(4)) {
            player.displayClientMessage(Component.literal("Necesitas ser operador para cambiar la configuración de DEDSAFIO 4."), false);
         } else {
            String changeId = buf.readUtf();
            server.execute(() -> com.eric.revill.g.a.a(server, changeId));
         }
      });

      // Habilitar o deshabilitar reto específico
      ServerPlayNetworking.registerGlobalReceiver(com.eric.revill.g.b.c, (server, player, handler, buf, responseSender) -> {
         if (!player.hasPermissions(4)) {
            player.displayClientMessage(Component.literal("Necesitas ser operador para cambiar la configuración de DEDSAFIO 4."), false);
         } else {
            String changeId = buf.readUtf(64);
            boolean enabled = buf.readBoolean();
            server.execute(() -> com.eric.revill.g.a.b(server, changeId, enabled));
         }
      });

      // Iniciar ruleta personalizada con color y mensaje
      ServerPlayNetworking.registerGlobalReceiver(com.eric.revill.g.b.e, (server, player, handler, buf, responseSender) -> {
         if (!player.hasPermissions(4)) {
            player.displayClientMessage(Component.literal("Necesitas ser operador para iniciar una ruleta personalizada."), false);
         } else {
            String color = buf.readUtf();
            String message = buf.readUtf();
            server.execute(() -> com.eric.revill.g.a.a(server, color, message));
         }
      });

      // Sincronización de animaciones de Nutria
      ServerPlayNetworking.registerGlobalReceiver(
         com.eric.revill.p.a.g, (server, player, handler, buf, responseSender) -> server.execute(() -> com.eric.revill.g.a.f(server))
      );
      ServerPlayNetworking.registerGlobalReceiver(
         com.eric.revill.p.a.i, (server, player, handler, buf, responseSender) -> server.execute(() -> com.eric.revill.g.a.g(server))
      );

      // Configurar y lanzar eventos REVILL
      ServerPlayNetworking.registerGlobalReceiver(com.eric.revill.g.b.f, (server, player, handler, buf, responseSender) -> {
         if (!player.hasPermissions(4)) {
            player.displayClientMessage(Component.literal("Necesitas ser operador para configurar los eventos REVILL."), false);
         } else {
            String taskId = buf.readUtf(64);
            String message = buf.readUtf(240);
            String color = buf.readUtf(32);
            int durationSeconds = buf.readVarInt();
            boolean musicEnabled = buf.readBoolean();
            server.execute(() -> com.eric.revill.j.a.a.a(server, taskId, message, color, durationSeconds, musicEnabled));
         }
      });

      // Detener eventos REVILL
      ServerPlayNetworking.registerGlobalReceiver(com.eric.revill.g.b.g, (server, player, handler, buf, responseSender) -> {
         if (!player.hasPermissions(4)) {
            player.displayClientMessage(Component.literal("Necesitas ser operador para detener REVILL."), false);
         } else {
            server.execute(() -> com.eric.revill.j.a.a.a(server, true));
         }
      });

      // Acciones directas de control de REVILL
      ServerPlayNetworking.registerGlobalReceiver(com.eric.revill.g.b.h, (server, player, handler, buf, responseSender) -> {
         int action = buf.readVarInt();
         if (!player.hasPermissions(4)) {
            player.displayClientMessage(Component.literal("Necesitas ser operador para controlar REVILL."), false);
         } else if (action >= 0 && action <= 3) {
            server.execute(() -> com.eric.revill.p.a.a(server, action));
         } else {
            player.displayClientMessage(Component.literal("Acción de REVILL no válida."), false);
         }
      });

      // Gestión de Waypoints (Añadir)
      ServerPlayNetworking.registerGlobalReceiver(com.eric.revill.t.b.b, (server, player, handler, buf, responseSender) -> {
         String name = buf.readUtf();
         String icon = buf.readUtf();
         server.execute(() -> {
            String id = "wp_" + System.currentTimeMillis();
            com.eric.revill.t.a wp = new com.eric.revill.t.a(id, name, player.blockPosition(), icon, -16711732, player.level().dimension().location().toString());
            com.eric.revill.t.b.a(server, wp);
            player.displayClientMessage(Component.literal("Waypoint '" + name + "' registrado."), false);
         });
      });

      // Gestión de Waypoints (Eliminar)
      ServerPlayNetworking.registerGlobalReceiver(com.eric.revill.t.b.c, (server, player, handler, buf, responseSender) -> {
         String id = buf.readUtf();
         server.execute(() -> {
            com.eric.revill.t.b.a(server, id);
            player.displayClientMessage(Component.literal("Waypoint eliminado."), false);
         });
      });

      // Gestión de Waypoints (Actualizar)
      ServerPlayNetworking.registerGlobalReceiver(com.eric.revill.t.b.d, (server, player, handler, buf, responseSender) -> {
         String id = buf.readUtf();
         String name = buf.readUtf();
         String icon = buf.readUtf();
         BlockPos pos = buf.readBlockPos();
         int color = buf.readInt();
         String dim = buf.readUtf();
         server.execute(() -> {
            com.eric.revill.t.a wp = new com.eric.revill.t.a(id, name, pos, icon, color, dim);
            com.eric.revill.t.b.a(server, wp);
            player.displayClientMessage(Component.literal("Waypoint '" + name + "' actualizado."), false);
         });
      });

      // Evento de entrada de jugador al servidor: sincroniza waypoints, retos, saldos y otorga la guía de administración
      ServerPlayConnectionEvents.JOIN.register((Join)(handler, sender, server) -> {
         com.eric.revill.t.b.a(handler.getPlayer());
         com.eric.revill.g.a.a(handler.getPlayer());
         com.eric.revill.h.b.a(handler.getPlayer());
         com.eric.revill.o.g.a(server, handler.getPlayer());
         AdminGuideItem.grantOnLogin(handler.getPlayer());
      });

      b.info("DEDSAFIO 4 Mechanics inicializado correctamente.");
   }

   // =========================================================================
   // EVENT LISTENERS DE LA INSTANCIA (NeoForge.EVENT_BUS)
   // =========================================================================

   @SubscribeEvent
   public void onServerTick(ServerTickEvent.Post event) {
      for (var l : ServerTickEvents.END_SERVER_TICK.getListeners()) {
         l.onEndTick(event.getServer());
      }
   }

   @SubscribeEvent
   public void onLevelTick(LevelTickEvent.Post event) {
      if (event.getLevel() instanceof ServerLevel sw) {
         for (var l : ServerTickEvents.END_WORLD_TICK.getListeners()) {
            l.onEndTick(sw);
         }
      }
   }

   @SubscribeEvent
   public void onServerStarted(ServerStartedEvent event) {
      for (var l : ServerLifecycleEvents.SERVER_STARTED.getListeners()) {
         l.onServerStarted(event.getServer());
      }
   }

   @SubscribeEvent
   public void onServerStopping(ServerStoppingEvent event) {
      for (var l : ServerLifecycleEvents.SERVER_STOPPING.getListeners()) {
         l.onServerStopping(event.getServer());
      }
   }

   @SubscribeEvent
   public void onEntityJoin(EntityJoinLevelEvent event) {
      if (event.getLevel() instanceof ServerLevel sw) {
         for (var l : ServerEntityEvents.ENTITY_LOAD.getListeners()) {
            l.onLoad(event.getEntity(), sw);
         }
      }
   }

   @SubscribeEvent
   public void onLivingDeath(LivingDeathEvent event) {
      for (var l : ServerLivingEntityEvents.ALLOW_DEATH.getListeners()) {
         if (!l.allowDeath(event.getEntity(), event.getSource(), event.getEntity().getHealth())) {
            event.setCanceled(true);
            return;
         }
      }
      for (var l : ServerLivingEntityEvents.AFTER_DEATH.getListeners()) {
         l.afterDeath(event.getEntity(), event.getSource());
      }
   }

   @SubscribeEvent
   public void onLivingIncomingDamage(LivingIncomingDamageEvent event) {
      for (var l : ServerLivingEntityEvents.ALLOW_DAMAGE.getListeners()) {
         if (!l.allowDamage(event.getEntity(), event.getSource(), event.getAmount())) {
            event.setCanceled(true);
         }
      }
   }

   @SubscribeEvent
   public void onBlockBreak(BlockEvent.BreakEvent event) {
      for (var l : PlayerBlockBreakEvents.AFTER.getListeners()) {
         l.afterBlockBreak((Level)event.getLevel(), event.getPlayer(), event.getPos(), event.getState(), null);
      }
   }

   @SubscribeEvent
   public void onUseBlock(PlayerInteractEvent.RightClickBlock event) {
      for (var l : UseBlockCallback.EVENT.getListeners()) {
         InteractionResult res = l.interact(event.getEntity(), event.getLevel(), event.getHand(), event.getHitVec());
         if (res == InteractionResult.FAIL || res == InteractionResult.CONSUME || res == InteractionResult.SUCCESS) {
            event.setCanceled(true);
            event.setCancellationResult(res);
         }
      }
   }

   @SubscribeEvent
   public void onUseEntity(PlayerInteractEvent.EntityInteract event) {
      for (var l : UseEntityCallback.EVENT.getListeners()) {
         InteractionResult res = l.interact(event.getEntity(), event.getLevel(), event.getHand(), event.getTarget(), null);
         if (res == InteractionResult.FAIL || res == InteractionResult.CONSUME || res == InteractionResult.SUCCESS) {
            event.setCanceled(true);
            event.setCancellationResult(res);
         }
      }
   }

   @SubscribeEvent
   public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
      if (event.getEntity() instanceof ServerPlayer player) {
         for (var l : ServerPlayConnectionEvents.JOIN.getListeners()) {
            l.onPlayReady(player.connection, (id, buf) -> ServerPlayNetworking.send(player, id, buf), player.getServer());
         }
      }
   }

   @SubscribeEvent
   public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
      if (event.getEntity() instanceof ServerPlayer player) {
         for (var l : ServerPlayConnectionEvents.DISCONNECT.getListeners()) {
            l.onPlayDisconnect(player.connection, player.getServer());
         }
      }
   }

   @SubscribeEvent
   public void onRegisterCommands(RegisterCommandsEvent event) {
      for (var l : CommandRegistrationCallback.EVENT.getListeners()) {
         l.register(event.getDispatcher(), event.getBuildContext(), event.getCommandSelection());
      }
   }

   /**
    * Listener registrado en el bus del mod (ModBus) para añadir los ítems del mod
    * a las pestañas creativas correspondientes, verificando que no existan duplicados
    * para evitar la excepción IllegalArgumentException de NeoForge.
    */
   public void onBuildCreativeTabs(BuildCreativeModeTabContentsEvent event) {
      var modifiers = ItemGroupEvents.getModifiers(event.getTabKey());
      if (modifiers != null) {
         for (var m : modifiers) {
            m.modifyEntries(stack -> {
               if (stack != null && !stack.isEmpty()) {
                  if (!event.getParentEntries().contains(stack)) {
                     try {
                        event.accept(stack);
                     } catch (IllegalArgumentException ignored) {
                     }
                  }
               }
            });
         }
      }
   }
}
