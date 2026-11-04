package com.eric.revill;

import com.eric.revill.d.a.b;
import com.eric.revill.d.a.c;
import com.eric.revill.d.a.d;
import com.eric.revill.d.b.f;
import com.eric.revill.d.b.g;
import com.eric.revill.d.b.h;
import com.eric.revill.d.b.i;
import com.eric.revill.d.b.j;
import com.eric.revill.d.b.k;
import com.eric.revill.d.b.o;
import com.eric.revill.d.d.a;
import com.eric.revill.d.e.AdminGuideScreen;
import com.eric.revill.d.e.e;
import com.eric.revill.d.e.l;
import com.eric.revill.d.e.m;
import com.eric.revill.d.e.n;
import com.eric.revill.d.e.p;
import com.mojang.blaze3d.platform.InputConstants.Type;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.eric.revill.fabric.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import com.eric.revill.fabric.fabric.api.client.command.v2.ClientCommandManager;
import com.eric.revill.fabric.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import com.eric.revill.fabric.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import com.eric.revill.fabric.fabric.api.client.event.lifecycle.v1.ClientTickEvents.EndTick;
import com.eric.revill.fabric.fabric.api.client.keybinding.v1.KeyBindingHelper;
import com.eric.revill.fabric.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import com.eric.revill.fabric.fabric.api.client.networking.v1.ClientPlayNetworking;
import com.eric.revill.fabric.fabric.api.client.networking.v1.ClientPlayConnectionEvents.Disconnect;
import com.eric.revill.fabric.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import com.eric.revill.fabric.fabric.api.client.rendering.v1.HudRenderCallback;
import com.eric.revill.fabric.fabric.api.client.rendering.v1.WorldRenderEvents;
import com.eric.revill.fabric.fabric.api.client.rendering.v1.WorldRenderEvents.AfterEntities;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.common.NeoForge;

public class RevillClient {
   private static KeyMapping f;
   private static KeyMapping g;
   private static KeyMapping h;
   public static final k a = new k();
   public static final j b = new j();
   public static final f c = new f();
   public static final g d = new g();
   public static final o e = new o();
   private static ItemStack customTotemPop = ItemStack.EMPTY;

   public static void setCustomTotemPop(ItemStack stack) {
      customTotemPop = stack == null ? ItemStack.EMPTY : stack.copy();
   }

   public static ItemStack consumeCustomTotemPop() {
      ItemStack stack = customTotemPop;
      customTotemPop = ItemStack.EMPTY;
      return stack;
   }

   public static void onInitializeClient(IEventBus modEventBus) {
      modEventBus.addListener(RevillClient::registerEntityRenderers);
      modEventBus.addListener(RevillClient::registerLayerDefinitions);
      modEventBus.addListener(RevillClient::registerScreens);
      modEventBus.addListener(RevillClient::registerKeyMappings);
      modEventBus.addListener(RevillClient::onClientSetup);
      NeoForge.EVENT_BUS.register(RevillClient.class);
      initClient();
   }

   private static void initClient() {
      com.eric.revill.d.a.d.a();
      WorldRenderEvents.AFTER_ENTITIES.register((AfterEntities)context -> {
         MultiBufferSource patt4219$temp;
         if (context.matrixStack() != null && (patt4219$temp = context.consumers()) instanceof BufferSource) {
            BufferSource consumers = (BufferSource)patt4219$temp;
            com.eric.revill.d.d.a.a.a(context.matrixStack(), consumers, context.camera(), context.tickDelta());
         }
      });
      com.eric.revill.d.a.a.a();
      com.eric.revill.d.a.c.a();
      com.eric.revill.d.a.b.a();
      f = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.dedsafio-4-mechanics.open_catalog", Type.KEYSYM, 71, "category.dedsafio-4-mechanics"));
      g = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.dedsafio-4-mechanics.open_guild", Type.KEYSYM, 72, "category.dedsafio-4-mechanics"));
      h = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.dedsafio-4-mechanics.open_missions", Type.KEYSYM, 77, "category.dedsafio-4-mechanics"));
      EntityModelLayerRegistry.registerModelLayer(com.eric.revill.d.b.a.a, com.eric.revill.d.b.a::a);
      HudRenderCallback.EVENT.register((HudRenderCallback)(context, tickDelta) -> {
         c.a(context);
         com.eric.revill.d.b.d.a(context);
         com.eric.revill.d.b.h.a(context);
         Minecraft client = Minecraft.getInstance();
         if (client != null) {
            b.a(context, client.font, context.guiWidth(), context.guiHeight());
         }
      });
      ClientPlayNetworking.registerGlobalReceiver(com.eric.revill.g.b.a, (client, handler, buf, responseSender) -> client.execute(() -> {
            if (client.player != null) {
               client.setScreen(new com.eric.revill.d.e.h());
            }
         }));
      ClientPlayNetworking.registerGlobalReceiver(com.eric.revill.g.b.i, (client, handler, buf, responseSender) -> client.execute(() -> c.c()));
      ClientPlayNetworking.registerGlobalReceiver(com.eric.revill.g.b.o, (client, handler, buf, responseSender) -> client.execute(() -> {
            if (client.player != null) {
               client.setScreen(new AdminGuideScreen());
            }
         }));
      ClientTickEvents.END_CLIENT_TICK.register((EndTick)client -> {
         while (f.consumeClick()) {
            if (client.player != null && client.level != null) {
               client.setScreen(new l());
            }
         }

         while (g.consumeClick()) {
            if (client.player != null && client.level != null) {
               com.eric.revill.d.a.b.c();
               client.setScreen(new com.eric.revill.d.e.k());
            }
         }

         while (h.consumeClick()) {
            if (client.player != null && client.level != null) {
               com.eric.revill.d.a.c.c();
               client.setScreen(new p(null));
            }
         }

         d.e();
         c.e();
         c.a(client);
         com.eric.revill.d.b.d.b();
         b.a(client);
         a.a(client);
         com.eric.revill.d.d.a.a.b();
      });
      ClientCommandRegistrationCallback.EVENT
         .register(
            (ClientCommandRegistrationCallback)(dispatcher, registryAccess) -> {
               dispatcher.register(
                  (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)ClientCommandManager.literal("fusion").executes(context -> {
                     d.a();
                     return 1;
                  })).then(ClientCommandManager.literal("entrar").executes(context -> {
                     d.a(true);
                     return 1;
                  }))).then(ClientCommandManager.literal("salir").executes(context -> {
                     d.a(false);
                     return 1;
                  }))
               );
               dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)ClientCommandManager.literal("revill").executes(context -> {
                  d.a();
                  return 1;
               })).then(((LiteralArgumentBuilder)((LiteralArgumentBuilder)ClientCommandManager.literal("fusion").executes(context -> {
                  d.a();
                  return 1;
               })).then(ClientCommandManager.literal("entrar").executes(context -> {
                  d.a(true);
                  return 1;
               }))).then(ClientCommandManager.literal("salir").executes(context -> {
                  d.a(false);
                  return 1;
               }))));
            }
         );
      ClientPlayConnectionEvents.DISCONNECT.register((Disconnect)(handler, client) -> {
         com.eric.revill.d.b.h.a(0);
         com.eric.revill.d.b.c.a();
         com.eric.revill.d.a.b.e();
         com.eric.revill.d.c.a.b();
         com.eric.revill.d.c.a.h();
         a.a(false);
         e.d();
         b.b();
         c.a(false, 0);
         d.d();
         com.eric.revill.d.d.a.a.a();
      });
   }

   private static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
      event.registerEntityRenderer((EntityType)com.eric.revill.i.b.a.get(), com.eric.revill.d.b.b::new);
      event.registerEntityRenderer((EntityType)com.eric.revill.i.b.b.get(), com.eric.revill.d.b.n::new);
      event.registerBlockEntityRenderer(com.eric.revill.m.h.c, i::new);
      event.registerBlockEntityRenderer(com.eric.revill.o.e.c, com.eric.revill.d.b.e::new);
   }

   private static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
      for (var entry : EntityModelLayerRegistry.PROVIDERS.entrySet()) {
         event.registerLayerDefinition(entry.getKey(), () -> entry.getValue().createModelData());
      }
   }

   private static void registerScreens(RegisterMenuScreensEvent event) {
      event.register(com.eric.revill.h.a.b, com.eric.revill.d.e.b::new);
      event.register(com.eric.revill.m.c.a, n::new);
      event.register(com.eric.revill.m.b.a, m::new);
      event.register(com.eric.revill.a.a.a, e::new);
      event.register(com.eric.revill.o.d.a, com.eric.revill.d.e.g::new);
   }

   private static void registerKeyMappings(RegisterKeyMappingsEvent event) {
      for (KeyMapping k : KeyBindingHelper.KEYS) {
         event.register(k);
      }
   }

   private static void onClientSetup(FMLClientSetupEvent event) {
      event.enqueueWork(() -> {
         BlockRenderLayerMap.INSTANCE.putBlock(com.eric.revill.o.e.a, RenderType.cutout());
         BlockRenderLayerMap.INSTANCE.putBlock(com.eric.revill.m.h.a, RenderType.cutout());
      });
   }

   @SubscribeEvent
   public static void onClientTick(ClientTickEvent.Post event) {
      for (var l : ClientTickEvents.END_CLIENT_TICK.getListeners()) {
         l.onEndTick(Minecraft.getInstance());
      }
   }

   @SubscribeEvent
   public static void onRenderGui(RenderGuiEvent.Post event) {
      for (var l : HudRenderCallback.EVENT.getListeners()) {
         l.onHudRender(event.getGuiGraphics(), event.getPartialTick().getGameTimeDeltaPartialTick(false));
      }
   }

   @SubscribeEvent
   public static void onRenderLevelStage(RenderLevelStageEvent event) {
      if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_ENTITIES) {
         WorldRenderEvents.Context context = new WorldRenderEvents.Context(
            event.getPoseStack(),
            Minecraft.getInstance().renderBuffers().bufferSource(),
            event.getCamera(),
            event.getPartialTick().getGameTimeDeltaPartialTick(false)
         );
         for (var l : WorldRenderEvents.AFTER_ENTITIES.getListeners()) {
            l.afterEntities(context);
         }
      }
   }

   @SubscribeEvent
   public static void onClientLogout(ClientPlayerNetworkEvent.LoggingOut event) {
      ClientPacketListener listener = event.getConnection() != null && event.getConnection().getPacketListener() instanceof ClientPacketListener cl ? cl : null;
      for (var l : ClientPlayConnectionEvents.DISCONNECT.getListeners()) {
         l.onPlayDisconnect(listener, Minecraft.getInstance());
      }
   }

   @SubscribeEvent
   public static void onRegisterClientCommands(RegisterClientCommandsEvent event) {
      for (var l : ClientCommandRegistrationCallback.EVENT.getListeners()) {
         l.register(event.getDispatcher(), event.getBuildContext());
      }
   }
}
