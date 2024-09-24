package mapext.common.packet.receiver;

import mapext.common.packet.PacketHandler;
import mapext.common.packet.type.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.thread.ReentrantThreadExecutor;

public abstract class PacketReceiver<C> {
	private final PacketHandler packetHandler;

	public PacketReceiver(PacketHandler packetHandler) {
		this.packetHandler = packetHandler;
	}

    protected abstract <T> boolean isCorrectSide(PacketType<T> packetType);

    protected abstract <T> Runnable getTask(PacketType<T> packetType, T packet, C context);

	protected void receive(ReentrantThreadExecutor<?> executor, PacketByteBuf buf, C context){
		receive(executor, getPacketType(buf), buf, context);
	}

    private PacketType<?> getPacketType(PacketByteBuf buf) {
        if(buf.readableBytes() <= 0)
            return null;
        int index = buf.readByte();
        return packetHandler.getPacketTypeByIndex(index);
    }

	private <T> void receive(ReentrantThreadExecutor<?> executor, PacketType<T> packetType, PacketByteBuf buf, C context){
		if(packetType == null)
			return;
		if(!isCorrectSide(packetType))
			return;
		T packet = packetType.codec().apply(buf);
		if(executor.isOnThread()) {
			getTask(packetType, packet, context).run();
			return;
		}
		executor.execute(getTask(packetType, packet, context));
	}
}
