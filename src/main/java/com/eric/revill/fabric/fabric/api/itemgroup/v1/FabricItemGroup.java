package com.eric.revill.fabric.fabric.api.itemgroup.v1;

/**
 * Adaptador de Fabric API para la construcción de pestañas creativas (CreativeModeTabs) en NeoForge.
 */

import net.minecraft.world.item.CreativeModeTab;

public final class FabricItemGroup {
    public static CreativeModeTab.Builder builder() {
        return CreativeModeTab.builder();
    }
}
