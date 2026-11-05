package com.eric.revill.fabric.fabric.api.networking.v1;

/**
 * Adaptador de red en el servidor de Fabric API a NeoForge.
 * 
 * Permite registrar manejadores de canales y emitir paquetes de red hacia jugadores conectados
 * utilizando la infraestructura de {@link net.neoforged.neoforge.network.PacketDistributor}.
 */

import io.netty.buffer.Unpooled;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.neoforged.neoforge.network.PacketDistributor;

public final class ServerPlayNetworking {
    @FunctionalInterface
    public interface PlayChannelHandler {
        void receive(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender);
    }

    private static final Map<ResourceLocation, PlayChannelHandler> HANDLERS = new ConcurrentHashMap<>();

    private ServerPlayNetworking() {}

    public static void registerGlobalReceiver(ResourceLocation channel, PlayChannelHandler handler) {
        HANDLERS.put(channel, handler);
    }

    public static void send(ServerPlayer player, ResourceLocation channel, FriendlyByteBuf buf) {
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        PacketDistributor.sendToPlayer(player, new ModChannelPayload(channel, bytes));
    }

    public static void sendToAll(MinecraftServer server, ResourceLocation channel, FriendlyByteBuf buf) {
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        PacketDistributor.sendToAllPlayers(new ModChannelPayload(channel, bytes));
    }

    public static void handle(MinecraftServer server, ServerPlayer player, ResourceLocation channel, byte[] data) {
        PlayChannelHandler handler = HANDLERS.get(channel);
        if (handler != null) {
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.wrappedBuffer(data));
            PacketSender sender = (id, outBuf) -> send(player, id, outBuf);
            handler.receive(server, player, player.connection, buf, sender);
        }
    }
}
