package com.eric.revill.fabric.fabric.api.networking.v1;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

@FunctionalInterface
public interface PacketSender {
    void sendPacket(ResourceLocation id, FriendlyByteBuf buf);
}
