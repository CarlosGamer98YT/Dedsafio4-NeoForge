package com.eric.revill.mixin;

import com.eric.revill.d.a.b;
import com.mojang.blaze3d.vertex.PoseStack;
import com.eric.revill.fabric.api.EnvType;
import com.eric.revill.fabric.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Font.DisplayMode;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin({EntityRenderer.class})
public abstract class LivingEntityRendererMixin {
   @Inject(
      method = {"renderNameTag"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void dedsafio4$renderGuildNameplate(
      Entity entity, Component name, PoseStack matrices, MultiBufferSource vertexConsumers, int light, float partialTick, CallbackInfo callbackInfo
   ) {
      if (entity instanceof Player player) {
         b.a guild = b.a(player.getUUID());
         if (guild != null) {
            Minecraft client = Minecraft.getInstance();
            if (client.gameRenderer.getMainCamera().getPosition().distanceToSqr(player.position()) > 4096.0) {
               callbackInfo.cancel();
            } else {
               callbackInfo.cancel();
               Font textRenderer = ((EntityRenderer)(Object)this).getFont();
               MutableComponent guildName = Component.literal(guild.a()).withStyle(style -> style.withColor(TextColor.fromRgb(guild.b() & 16777215)));
               MutableComponent coloredName = name.copy().withStyle(style -> style.withColor(TextColor.fromRgb(guild.b() & 16777215)));
               matrices.pushPose();
               net.minecraft.world.phys.Vec3 nameTagVec = player.getAttachments().getNullable(net.minecraft.world.entity.EntityAttachment.NAME_TAG, 0, player.getViewYRot(partialTick));
               double nameTagY = nameTagVec != null ? nameTagVec.y : (double)player.getBbHeight() + 0.5;
               matrices.translate(0.0, nameTagY, 0.0);
               matrices.mulPose(client.gameRenderer.getMainCamera().rotation());
               matrices.scale(-0.025F, -0.025F, 0.025F);
               float nameX = (float)(-textRenderer.width(coloredName)) / 2.0F;
               textRenderer.drawInBatch(
                  coloredName, nameX, 0.0F, -1, false, matrices.last().pose(), vertexConsumers, DisplayMode.SEE_THROUGH, Integer.MIN_VALUE, light
               );
               if (!player.isDiscrete()) {
                  textRenderer.drawInBatch(
                     coloredName, nameX, 0.0F, -1, false, matrices.last().pose(), vertexConsumers, DisplayMode.NORMAL, Integer.MIN_VALUE, light
                  );
               }

               float guildX = (float)(-textRenderer.width(guildName)) / 2.0F;
               textRenderer.drawInBatch(
                  guildName, guildX, 10.0F, -1, false, matrices.last().pose(), vertexConsumers, DisplayMode.SEE_THROUGH, -1728053248, light
               );
               if (!player.isDiscrete()) {
                  textRenderer.drawInBatch(
                     guildName, guildX, 10.0F, -1, false, matrices.last().pose(), vertexConsumers, DisplayMode.NORMAL, -1728053248, light
                  );
               }

               matrices.popPose();
               if (player != client.player && !player.isSpectator() && !player.isDeadOrDying()) {
                  this.dedsafio4$drawHealthLabel(player, matrices, vertexConsumers);
               }
            }
         }
      }
   }

   @Inject(
      method = {"renderNameTag"},
      at = {@At("TAIL")}
   )
   private void dedsafio4$renderPlayerHealth(
      Entity entity, Component text, PoseStack matrices, MultiBufferSource vertexConsumers, int light, float partialTick, CallbackInfo callbackInfo
   ) {
      Player player;
      if (entity instanceof Player && !(player = (Player)entity).isSpectator() && !player.isDeadOrDying()) {
         if (player != Minecraft.getInstance().player) {
            this.dedsafio4$drawHealthLabel(player, matrices, vertexConsumers);
         }
      }
   }

   private void dedsafio4$drawHealthLabel(Player player, PoseStack matrices, MultiBufferSource vertexConsumers) {
      int currentHealth = Mth.ceil(player.getHealth());
      MutableComponent healthText = Component.literal("❤ " + currentHealth).copy().withStyle(style -> style.withColor(TextColor.fromRgb(16731212)));
      Font textRenderer = Minecraft.getInstance().font;
      matrices.pushPose();
      matrices.translate(0.0, (double)player.getBbHeight() + 0.85, 0.0);
      matrices.mulPose(Minecraft.getInstance().gameRenderer.getMainCamera().rotation());
      matrices.scale(-0.025F, -0.025F, 0.025F);
      float width = (float)(-textRenderer.width(healthText) / 2);
      textRenderer.drawInBatch(healthText, width, 0.0F, 16731212, false, matrices.last().pose(), vertexConsumers, DisplayMode.SEE_THROUGH, 0, 15728880);
      matrices.popPose();
   }
}
