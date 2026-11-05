package com.eric.revill.o;

/**
 * Registro del bloque e ítem del Pedestal de Desafíos Diarios.
 */

import com.eric.revill.ForgeRegistration;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntityType.Builder;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public final class e {
   public static a a;
   public static Item b;
   public static BlockEntityType<b> c;

   private e() {
   }

   public static void a() {
   }

   static {
      ForgeRegistration.registerLazy(
         Registries.BLOCK,
         ResourceLocation.fromNamespaceAndPath("dedsafio-4-mechanics", "daily_mission_pedestal"),
         () -> new a(Properties.ofFullCopy(Blocks.STONE).strength(2.0F).noOcclusion()),
         block -> a = block
      );
      ForgeRegistration.registerLazy(
         Registries.ITEM,
         ResourceLocation.fromNamespaceAndPath("dedsafio-4-mechanics", "daily_mission_pedestal"),
         () -> new BlockItem(a, new net.minecraft.world.item.Item.Properties().stacksTo(16)),
         item -> b = item
      );
      ForgeRegistration.registerLazy(
         Registries.BLOCK_ENTITY_TYPE,
         ResourceLocation.fromNamespaceAndPath("dedsafio-4-mechanics", "daily_mission_pedestal"),
         () -> Builder.of(b::new, new Block[]{a}).build(null),
         blockEntityType -> c = blockEntityType
      );
   }
}
