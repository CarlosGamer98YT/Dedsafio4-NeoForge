package com.eric.revill.fabric.fabric.api.networking.v1;

/**
 * Fábrica de buffers de red para emular la API de Fabric sobre {@link net.minecraft.network.FriendlyByteBuf}.
 */

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;

public final class PacketByteBufs {
    private PacketByteBufs() {}

    public static FriendlyByteBuf create() {
        return new FriendlyByteBuf(Unpooled.buffer());
    }

    public static FriendlyByteBuf empty() {
        return new FriendlyByteBuf(Unpooled.EMPTY_BUFFER);
    }

    public static FriendlyByteBuf copy(ByteBuf buf) {
        return new FriendlyByteBuf(buf.copy());
    }
}
