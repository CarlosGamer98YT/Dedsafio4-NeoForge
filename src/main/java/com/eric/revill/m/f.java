package com.eric.revill.m;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class f extends BaseEntityBlock {
   public static final com.mojang.serialization.MapCodec<f> CODEC = simpleCodec(f::new);
   public static final DirectionProperty a = BlockStateProperties.HORIZONTAL_FACING;

   @Override
   protected com.mojang.serialization.MapCodec<? extends BaseEntityBlock> codec() {
      return CODEC;
   }
   private static final VoxelShape b = a(Direction.NORTH);
   private static final VoxelShape c = a(Direction.EAST);
   private static final VoxelShape d = a(Direction.SOUTH);
   private static final VoxelShape e = a(Direction.WEST);

   public f(Properties settings) {
      super(settings);
      this.registerDefaultState((BlockState)((BlockState)this.getStateDefinition().any()).setValue(a, Direction.NORTH));
   }

   public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
      return new g(pos, state);
   }

   public RenderShape getRenderShape(BlockState state) {
      return RenderShape.MODEL;
   }

   protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
      builder.add(new Property[]{a});
   }

   public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
      return this.a(state);
   }

   public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
      return this.a(state);
   }

   private VoxelShape a(BlockState state) {
      return switch ((Direction)state.getValue(a)) {
         case EAST -> c;
         case SOUTH -> d;
         case WEST -> e;
         default -> b;
      };
   }

   private static VoxelShape a(Direction facing) {
      return Shapes.or(
         Shapes.or(a(2.0, 0.0, 11.0, 14.0, 9.0, 16.0, facing), a(2.0, 7.0, 10.0, 14.0, 12.0, 16.0, facing)), a(4.0, 5.0, 10.5, 12.0, 8.0, 11.0, facing)
      );
   }

   private static VoxelShape a(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, Direction facing) {
      return switch (facing) {
         case EAST -> Block.box(16.0 - maxZ, minY, minX, 16.0 - minZ, maxY, maxX);
         case SOUTH -> Block.box(16.0 - maxX, minY, 16.0 - maxZ, 16.0 - minX, maxY, 16.0 - minZ);
         case WEST -> Block.box(minZ, minY, 16.0 - maxX, maxZ, maxY, 16.0 - minX);
         default -> Block.box(minX, minY, minZ, maxX, maxY, maxZ);
      };
   }

   public BlockState getStateForPlacement(BlockPlaceContext ctx) {
      return (BlockState)this.defaultBlockState().setValue(a, ctx.getPlayer().getDirection().getOpposite());
   }

   public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
      if (world.isClientSide) {
         return InteractionResult.SUCCESS;
      } else {
         if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.openMenu(com.eric.revill.m.c.a());
         }

         return InteractionResult.SUCCESS;
      }
   }
}
