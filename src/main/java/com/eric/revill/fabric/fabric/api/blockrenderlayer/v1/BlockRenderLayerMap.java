package com.eric.revill.fabric.fabric.api.blockrenderlayer.v1;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;

public final class BlockRenderLayerMap {
    public static final BlockRenderLayerMap INSTANCE = new BlockRenderLayerMap();

    public void putBlock(Block block, RenderType renderType) {
        // Handled in NeoForge via Block models or ItemBlockRenderTypes
    }
}
