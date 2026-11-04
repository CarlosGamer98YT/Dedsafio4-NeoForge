package com.eric.revill.fabric.fabric.api.screenhandler.v1;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.network.IContainerFactory;

public class ExtendedScreenHandlerType<T extends AbstractContainerMenu> extends MenuType<T> {
    public interface ExtendedFactory<T extends AbstractContainerMenu> {
        T create(int syncId, Inventory inventory, FriendlyByteBuf buf);
    }

    public ExtendedScreenHandlerType(ExtendedFactory<T> factory) {
        super((IContainerFactory<T>) (syncId, inv, buf) -> factory.create(syncId, inv, buf), FeatureFlags.DEFAULT_FLAGS);
    }
}
