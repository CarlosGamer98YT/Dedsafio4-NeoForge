package com.eric.revill.fabric.fabric.api.screenhandler.v1;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.client.extensions.IMenuProviderExtension;

public interface ExtendedScreenHandlerFactory extends MenuProvider, IMenuProviderExtension {
    void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf);

    @Override
    default void writeClientSideData(AbstractContainerMenu menu, RegistryFriendlyByteBuf buffer) {
        // Player is accessed from menu or thread
        if (menu != null) {
            // writeScreenOpeningData
            writeScreenOpeningData(null, buffer);
        }
    }
}
