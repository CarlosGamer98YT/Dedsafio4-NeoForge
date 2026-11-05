package com.eric.revill.fabric.fabric.api.networking.v1;

/**
 * Contenedor de paquetes de red {@link net.minecraft.network.protocol.common.custom.CustomPacketPayload} para NeoForge.
 * 
 * Encapsula un canal identificado por {@link net.minecraft.resources.ResourceLocation} y un array de bytes
 * de carga útil para comunicar el cliente y el servidor con total fidelidad a la arquitectura de paquetes de Fabric.
 */

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ModChannelPayload(ResourceLocation channel, byte[] data) implements CustomPacketPayload {
    public static final Type<ModChannelPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath("dedsafio_4_mechanics", "channel_payload"));
    public static final StreamCodec<ByteBuf, ModChannelPayload> STREAM_CODEC = StreamCodec.of(
        (buf, payload) -> {
            ByteBufCodecs.STRING_UTF8.encode(buf, payload.channel.toString());
            ByteBufCodecs.BYTE_ARRAY.encode(buf, payload.data);
        },
        buf -> {
            String ch = ByteBufCodecs.STRING_UTF8.decode(buf);
            byte[] bytes = ByteBufCodecs.BYTE_ARRAY.decode(buf);
            return new ModChannelPayload(ResourceLocation.parse(ch), bytes);
        }
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
