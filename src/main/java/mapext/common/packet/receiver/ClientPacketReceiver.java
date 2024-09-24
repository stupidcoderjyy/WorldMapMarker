package mapext.common.packet.receiver;

import mapext.common.packet.PacketHandler;
import mapext.common.packet.type.PacketType;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;

public class ClientPacketReceiver extends PacketReceiver<Object> implements ClientPlayNetworking.PlayChannelHandler  {
    public ClientPacketReceiver(PacketHandler packetHandler) {
        super(packetHandler);
    }

    @Override
    protected <T> boolean isCorrectSide(PacketType<T> packetType) {
        return packetType.clientHandler() != null;
    }

    @Override
    protected <T> Runnable getTask(PacketType<T> packetType, T packet, Object context) {
        return () -> packetType.clientHandler().accept(packet);
    }

    @Override
    public void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        receive(client, buf, null);
    }
}
