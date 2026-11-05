package com.eric.revill.mixin;

/**
 * Inyección en el HUD principal del juego {@link net.minecraft.client.gui.Gui}.
 * 
 * Renderiza en pantalla:
 * - El balance de Dedita del jugador.
 * - La barra y cuenta atrás del Momento Revill.
 * - Avisos de retos y alertas del desafío.
 */

import com.eric.revill.RevillClient;
import com.eric.revill.l.g;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import com.eric.revill.fabric.api.EnvType;
import com.eric.revill.fabric.api.Environment;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin({Gui.class})
public abstract class InGameHudMixin {
   private static final int DEDSAFIO4_GREEN = -12327313;

   @Inject(
      method = {"render(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/DeltaTracker;)V"},
      at = {@At("TAIL")}
   )
   private void dedsafio4$renderRedOverlayAboveChat(GuiGraphics context, DeltaTracker deltaTracker, CallbackInfo callbackInfo) {
      RevillClient.a.a(context);
   }

   @Inject(
      method = {"renderEffects"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void dedsafio4$renderStatusEffectsAtBottomLeft(GuiGraphics context, DeltaTracker deltaTracker, CallbackInfo callbackInfo) {
      Minecraft client = Minecraft.getInstance();
      if (client.player != null && !client.player.getActiveEffects().isEmpty()) {
         Screen screen = client.screen;
         EffectRenderingInventoryScreen inventoryScreen;
         if (screen instanceof EffectRenderingInventoryScreen && (inventoryScreen = (EffectRenderingInventoryScreen)screen).canSeeEffects()) {
            callbackInfo.cancel();
         } else {
            Collection<MobEffectInstance> activeEffects = client.player.getActiveEffects();
            ArrayList<MobEffectInstance> effects = Lists.newArrayList(activeEffects);
            effects.sort(Comparator.<MobEffectInstance>naturalOrder().reversed());
            int width = client.getWindow().getGuiScaledWidth();
            int height = client.getWindow().getGuiScaledHeight();
            int beneficial = 0;
            int harmful = 0;
            RenderSystem.enableBlend();

            for (MobEffectInstance effect : effects) {
               if (effect.showIcon()) {
                  Holder<MobEffect> statusEffectHolder = effect.getEffect();
                  MobEffect statusEffect = statusEffectHolder.value();
                  int index = statusEffect.isBeneficial() ? beneficial++ : harmful++;
                  int x = 2 + index % Math.max(1, (width - 4) / 25) * 25;
                  int row = index / Math.max(1, (width - 4) / 25);
                  int y = height - (statusEffect.isBeneficial() ? 26 : 51) - row * 25;
                  float alpha = 1.0F;
                  if (effect.isAmbient()) {
                     context.blit(AbstractContainerScreen.INVENTORY_LOCATION, x, y, 165, 166, 24, 24);
                  } else {
                     context.blit(AbstractContainerScreen.INVENTORY_LOCATION, x, y, 141, 166, 24, 24);
                     if (effect.endsWithin(200)) {
                        int duration = effect.getDuration();
                        int blinkStep = 10 - duration / 20;
                        alpha = Mth.clamp((float)duration / 50.0F, 0.0F, 0.5F)
                           + Mth.cos((float)duration * (float) Math.PI / 5.0F) * Mth.clamp((float)blinkStep / 10.0F * 0.25F, 0.0F, 0.25F);
                     }
                  }

                  TextureAtlasSprite sprite = client.getMobEffectTextures().get(statusEffectHolder);
                  context.setColor(1.0F, 1.0F, 1.0F, alpha);
                  context.blit(x + 3, y + 3, 0, 18, 18, sprite);
                  context.setColor(1.0F, 1.0F, 1.0F, 1.0F);
               }
            }

            RenderSystem.disableBlend();
            callbackInfo.cancel();
         }
      } else {
         callbackInfo.cancel();
      }
   }

   @Inject(
      method = {"renderSlot"},
      at = {@At("TAIL")}
   )
   private void dedsafio4$drawGreenHotbarBorder(
      GuiGraphics context, int x, int y, DeltaTracker deltaTracker, Player player, ItemStack stack, int seed, CallbackInfo callbackInfo
   ) {
      if (stack.is(g.c.get())
         || stack.is(g.d.get())
         || stack.is(g.e.get())
         || stack.is(g.f.get())
         || stack.is(Items.TOTEM_OF_UNDYING)) {
         context.fill(x, y, x + 16, y + 1, -12327313);
         context.fill(x, y + 15, x + 16, y + 16, -12327313);
         context.fill(x, y + 1, x + 1, y + 15, -12327313);
         context.fill(x + 15, y + 1, x + 16, y + 15, -12327313);
      }
   }
}
