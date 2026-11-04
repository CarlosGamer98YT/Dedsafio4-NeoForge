package com.eric.revill.m;

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

public final class h {
   public static Block a;
   public static Item b;
   public static BlockEntityType<g> c;

   private h() {
   }

   public static void a() {
   }

   static {
      ForgeRegistration.registerLazy(
         Registries.BLOCK,
         ResourceLocation.fromNamespaceAndPath("dedsafio-4-mechanics", "mailbox"),
         () -> new f(Properties.ofFullCopy(Blocks.OAK_PLANKS).noOcclusion()),
         block -> a = block
      );
      ForgeRegistration.registerLazy(
         Registries.ITEM,
         ResourceLocation.fromNamespaceAndPath("dedsafio-4-mechanics", "mailbox"),
         () -> new BlockItem(a, new net.minecraft.world.item.Item.Properties()),
         item -> b = item
      );
      ForgeRegistration.registerLazy(
         Registries.BLOCK_ENTITY_TYPE,
         ResourceLocation.fromNamespaceAndPath("dedsafio-4-mechanics", "mailbox"),
         () -> Builder.of(g::new, new Block[]{a}).build(null),
         blockEntityType -> c = blockEntityType
      );
   }
}
