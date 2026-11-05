package com.eric.revill.fabric.fabric.api.client.networking.v1;

/**
 * Adaptador de red de cliente de Fabric API a NeoForge.
 * 
 * Gestiona el envío y recepción de paquetes de red identificados por {@link net.minecraft.resources.ResourceLocation}
 * encapsulándolos a través de los canales de red de NeoForge.
 */

import io.netty.buffer.Unpooled;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.eric.revill.fabric.fabric.api.networking.v1.ModChannelPayload;
import com.eric.revill.fabric.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.PacketDistributor;

public final class ClientPlayNetworking {
    @FunctionalInterface
    public interface PlayChannelHandler {
        void receive(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender responseSender);
    }

    private static final Map<ResourceLocation, PlayChannelHandler> HANDLERS = new ConcurrentHashMap<>();

    private ClientPlayNetworking() {}

    public static void registerGlobalReceiver(ResourceLocation channel, PlayChannelHandler handler) {
        HANDLERS.put(channel, handler);
    }

    public static boolean canSend(ResourceLocation channel) {
        return true;
    }

    public static void send(ResourceLocation channel, FriendlyByteBuf buf) {
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        PacketDistributor.sendToServer(new ModChannelPayload(channel, bytes));
    }

    public static void handle(ResourceLocation channel, byte[] data) {
        PlayChannelHandler handler = HANDLERS.get(channel);
        if (handler != null) {
            Minecraft client = Minecraft.getInstance();
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.wrappedBuffer(data));
            PacketSender sender = (id, outBuf) -> send(id, outBuf);
            handler.receive(client, client.getConnection(), buf, sender);
        }
    }
}
