package com.eric.revill.o;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import com.eric.revill.fabric.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class a extends BaseEntityBlock implements EntityBlock {
   public static final com.mojang.serialization.MapCodec<a> CODEC = simpleCodec(a::new);
   public static final BooleanProperty a = BooleanProperty.create("glow_model");

   @Override
   protected com.mojang.serialization.MapCodec<? extends BaseEntityBlock> codec() {
      return CODEC;
   }
   private static final int b = 60;
   private static final int[] c = new int[]{-50347, -25294, -187, -12060806, -13179137, -11172609, -5088001};
   private static final List<a.PendingDrop> d = new ArrayList<>();
   private static final VoxelShape e = Shapes.or(
      Block.box(1.0, 0.0, 1.0, 15.0, 1.0, 15.0),
      new VoxelShape[]{
         Block.box(2.0, 1.0, 2.0, 14.0, 2.0, 14.0),
         Block.box(2.0, 20.0, 2.0, 14.0, 21.0, 14.0),
         Block.box(1.0, 21.0, 1.0, 15.0, 22.0, 15.0),
         Block.box(3.0, 2.0, 3.0, 11.0, 4.0, 13.0),
         Block.box(3.0, 4.0, 3.0, 5.0, 18.0, 13.0),
         Block.box(5.0, 4.0, 4.0, 11.0, 18.0, 12.0),
         Block.box(11.0, 2.0, 3.0, 13.0, 18.0, 13.0),
         Block.box(3.0, 18.0, 3.0, 13.0, 20.0, 13.0)
      }
   );
   private static final UUID f = new UUID(0L, 0L);

   public a(Properties settings) {
      super(settings);
      this.registerDefaultState((BlockState)((BlockState)this.getStateDefinition().any()).setValue(a, false));
   }

   protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
      builder.add(new Property[]{a});
   }

   public b a(BlockPos pos, BlockState state) {
      return new b(pos, state);
   }

   public RenderShape getRenderShape(BlockState state) {
      return RenderShape.MODEL;
   }

   public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
      return e;
   }

   public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
      return e;
   }

   public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
      super.setPlacedBy(world, pos, state, placer, itemStack);
      b pedestal;
      ServerPlayer player;
      if (!world.isClientSide
         && placer instanceof ServerPlayer
         && (player = (ServerPlayer)placer).isCreative()
         && player.hasPermissions(4)
         && (pedestal = (b)world.getBlockEntity(pos)) != null) {
         player.openMenu(com.eric.revill.o.d.a(pedestal));
      }
   }

   public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
      if (!(world.getBlockEntity(pos) instanceof b pedestal)) {
         return InteractionResult.PASS;
      } else {
         ItemStack held = player.getItemInHand(hand);
         if (!world.isClientSide && held.is(com.eric.revill.l.g.e.get())) {
            return this.a((ServerPlayer)player, pedestal, held);
         } else if (!player.isCreative() || !player.hasPermissions(4)) {
            return world.isClientSide ? InteractionResult.SUCCESS : InteractionResult.PASS;
         } else if (world.isClientSide) {
            return InteractionResult.SUCCESS;
         } else {
            ((ServerPlayer)player).openMenu(com.eric.revill.o.d.a(pedestal));
            return InteractionResult.CONSUME;
         }
      }
   }

   private InteractionResult a(ServerPlayer player, b pedestal, ItemStack token) {
      int tokenDay = g.a(token);
      if (tokenDay != pedestal.a()) {
         player.displayClientMessage(Component.translatable("block.dedsafio-4-mechanics.daily_pedestal.wrong_day", new Object[]{pedestal.a()}), true);
         return InteractionResult.CONSUME;
      } else {
         List<ItemStack> rewards = pedestal.b();
         if (rewards.isEmpty()) {
            player.displayClientMessage(Component.translatable("block.dedsafio-4-mechanics.daily_pedestal.no_reward"), true);
            return InteractionResult.CONSUME;
         } else {
            String message = "¡Gracias por completar la misión del día " + pedestal.a() + "! Aquí tienes tu recompensa.";
            token.shrink(1);
            a(player);
            ServerLevel world = (ServerLevel)player.level();
            BlockPos pos = pedestal.getBlockPos().immutable();
            world.playSound(null, pos, SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 1.0F, 0.85F);
            d.add(new a.PendingDrop(world, pos, player.getUUID(), rewards, message, 60));
            return InteractionResult.CONSUME;
         }
      }
   }

   private static void a(ServerPlayer player) {
      MutableComponent announcement = Component.literal("☺ ")
         .withStyle(ChatFormatting.BLUE)
         .append(Component.literal(player.getName().getString()).withStyle(new ChatFormatting[]{ChatFormatting.BLUE, ChatFormatting.BOLD}))
         .append(Component.literal(" ha completado la misión diaria.").withStyle(ChatFormatting.BLUE));
      player.getServer().getPlayerList().broadcastSystemMessage(announcement, false);
   }

   private static void a(MinecraftServer server) {
      Iterator<a.PendingDrop> iterator = d.iterator();

      while (iterator.hasNext()) {
         a.PendingDrop pending = iterator.next();
         if (pending.a.getServer() == server) {
            if (pending.a.hasChunkAt(pending.b)) {
               a(pending);
            }

            pending.f--;
            if (pending.f <= 0) {
               if (pending.a.hasChunkAt(pending.b)) {
                  a(pending.a, pending.b);
               }

               com.eric.revill.m.d.a.a(pending.c, f, "Eón", "Recompensa de misión diaria", pending.e, pending.d);
               ServerPlayer recipient = server.getPlayerList().getPlayer(pending.c);
               if (recipient != null) {
                  com.eric.revill.m.e.a(recipient);
                  com.eric.revill.m.d.a.a(recipient, "Eón", pending.e);
                  recipient.displayClientMessage(Component.translatable("block.dedsafio-4-mechanics.daily_pedestal.mail_sent"), true);
               }

               iterator.remove();
            }
         }
      }
   }

   private static void a(a.PendingDrop pending) {
      double centerX = (double)pending.b.getX() + 0.5;
      double centerY = (double)pending.b.getY() + 1.35;
      double centerZ = (double)pending.b.getZ() + 0.5;
      int elapsed = 60 - pending.f;

      for (int particle = 0; particle < 4; particle++) {
         double angle = (double)elapsed * 0.22 + (double)particle * Math.PI / 2.0;
         double x = centerX + Math.cos(angle) * 0.75;
         double y = centerY + Math.sin(angle * 2.0) * 0.16;
         double z = centerZ + Math.sin(angle) * 0.75;
         int color = c[(elapsed / 3 + particle) % c.length];
         pending.a.sendParticles(new DustParticleOptions(Vec3.fromRGB24(color & 16777215).toVector3f(), 0.9F), x, y, z, 1, 0.0, 0.0, 0.0, 0.0);
      }
   }

   private static void a(ServerLevel world, BlockPos pos) {
      double x = (double)pos.getX() + 0.5;
      double y = (double)pos.getY() + 1.5;
      double z = (double)pos.getZ() + 0.5;
      world.playSound(null, pos, SoundEvents.FIREWORK_ROCKET_LAUNCH, SoundSource.PLAYERS, 1.0F, 1.0F);
      world.sendParticles(ParticleTypes.FIREWORK, x, y, z, 32, 0.4, 0.45, 0.4, 0.12);
      world.sendParticles(ParticleTypes.EXPLOSION_EMITTER, x, y, z, 1, 0.0, 0.0, 0.0, 0.0);
      world.playSound(null, pos, SoundEvents.FIREWORK_ROCKET_LARGE_BLAST, SoundSource.PLAYERS, 1.0F, 1.0F);
   }

   public BlockState getStateForPlacement(BlockPlaceContext ctx) {
      return this.defaultBlockState();
   }

   public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
      return this.a(pos, state);
   }

   static {
      ServerTickEvents.END_SERVER_TICK.register(com.eric.revill.o.a::a);
   }

   private static final class PendingDrop {
      private final ServerLevel a;
      private final BlockPos b;
      private final UUID c;
      private final List<ItemStack> d;
      private final String e;
      private int f;

      private PendingDrop(ServerLevel world, BlockPos pos, UUID recipientId, List<ItemStack> rewards, String message, int ticksRemaining) {
         this.a = world;
         this.b = pos;
         this.c = recipientId;
         this.d = rewards;
         this.e = message;
         this.f = ticksRemaining;
      }
   }
}
