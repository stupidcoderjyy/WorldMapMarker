package mapext.common.packet.receiver;

import mapext.common.packet.PacketHandler;
import mapext.common.packet.type.PacketType;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class ServerPacketReceiver extends PacketReceiver<ServerPlayerEntity> implements ServerPlayNetworking.PlayChannelHandler{
    public ServerPacketReceiver(PacketHandler packetHandler) {
        super(packetHandler);
    }

    @Override
    protected <T> boolean isCorrectSide(PacketType<T> packetType) {
        return packetType.serverHandler() != null;
    }

    @Override
    protected <T> Runnable getTask(PacketType<T> packetType, T packet, ServerPlayerEntity player) {
        return () -> packetType.serverHandler().accept(packet, player);
    }

    @Override
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        receive(server, buf, player);
    }
}
