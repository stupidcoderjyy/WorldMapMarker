package mapext.common.packet;

import net.minecraft.network.PacketByteBuf;

public interface ICodec<P> {
    void accept(P packet, PacketByteBuf buf);
    P apply(PacketByteBuf buf);
}
