package com.eric.revill.fabric.fabric.api.blockrenderlayer.v1;

/**
 * Adaptador de Fabric API para capas de renderizado de bloques en NeoForge.
 * 
 * En Minecraft 1.21.1 / NeoForge, el renderizado de bloques translúcidos se gestiona mediante modelos
 * de bloque JSON (render_type). Esta clase mantiene compatibilidad binaria con las llamadas de Fabric.
 */

import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;

public final class BlockRenderLayerMap {
    public static final BlockRenderLayerMap INSTANCE = new BlockRenderLayerMap();

    public void putBlock(Block block, RenderType renderType) {
        // Handled in NeoForge via Block models or ItemBlockRenderTypes
    }
}
