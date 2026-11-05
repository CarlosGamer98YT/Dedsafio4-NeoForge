package com.eric.revill.l;

/**
 * Ítem Palitomágico.
 * 
 * Al interactuar sobre otro jugador, lo teletransporta instantáneamente a su punto
 * de reaparición (cama / ancla de reaparición) con efectos de partículas y sonido.
 */

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item.Properties;
import org.joml.Vector3f;

public final class f extends Item {
   private static final SoundEvent a = SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("revill", "ding"));
   private static final DustParticleOptions b = new DustParticleOptions(new Vector3f(0.01F, 0.01F, 0.01F), 1.5F);

   public f(Properties settings) {
      super(settings);
   }

   public InteractionResult interactLivingEntity(ItemStack stack, Player user, LivingEntity entity, InteractionHand hand) {
      if (!(entity instanceof Player target)) {
         return InteractionResult.PASS;
      } else if (user.level().isClientSide) {
         return InteractionResult.SUCCESS;
      } else if (!(target instanceof ServerPlayer serverTarget)) {
         return InteractionResult.PASS;
      } else {
         a(serverTarget);
         return InteractionResult.SUCCESS;
      }
   }

   private static void a(ServerPlayer target) {
      ServerLevel sourceWorld = target.serverLevel();
      double sourceX = target.getX();
      double sourceY = target.getY();
      double sourceZ = target.getZ();
      BlockPos respawnPosition = target.getRespawnPosition();
      ServerLevel destinationWorld = respawnPosition == null ? target.getServer().overworld() : target.getServer().getLevel(target.getRespawnDimension());
      if (destinationWorld == null) {
         destinationWorld = target.getServer().overworld();
         respawnPosition = null;
      }

      if (respawnPosition == null) {
         respawnPosition = destinationWorld.getSharedSpawnPos();
      }

      sourceWorld.playSound(null, sourceX, sourceY, sourceZ, a, SoundSource.PLAYERS, 1.0F, 1.0F);
      sourceWorld.sendParticles(ParticleTypes.EXPLOSION_EMITTER, sourceX, sourceY + 0.5, sourceZ, 1, 0.0, 0.0, 0.0, 0.0);
      sourceWorld.sendParticles(b, sourceX, sourceY + 0.5, sourceZ, 48, 0.35, 0.45, 0.35, 0.08);
      double destinationX = (double)respawnPosition.getX() + 0.5;
      double destinationY = (double)respawnPosition.getY();
      double destinationZ = (double)respawnPosition.getZ() + 0.5;
      target.teleportTo(destinationWorld, destinationX, destinationY, destinationZ, target.getYRot(), target.getXRot());
      target.displayClientMessage(Component.literal("Has sido teletransportado a tu último punto de reaparición establecido.").withStyle(ChatFormatting.GREEN), true);
   }
}
