package mapext.common.packet.marks;

import mapext.common.marks.MarkActionRequestHandler;
import mapext.common.packet.ICodec;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;

public record SMarkActionRequest(int left, int top, int right, int bottom, @Nullable Integer color) {

    public static class Codec implements ICodec<SMarkActionRequest> {

        @Override
        public void accept(SMarkActionRequest pack, PacketByteBuf buf) {
            var nbt = new NbtCompound();
            nbt.putInt("l", pack.left);
            nbt.putInt("r", pack.right);
            nbt.putInt("t", pack.top);
            nbt.putInt("b", pack.bottom);
            nbt.putInt("c", pack.color == null ? 0 : pack.color);
            buf.writeNbt(nbt);
        }

        @Override
        public SMarkActionRequest apply(PacketByteBuf buf) {
            try {
                var nbt = buf.readNbt();
                int left = nbt.getInt("l");
                int right = nbt.getInt("r");
                int top = nbt.getInt("t");
                int bottom = nbt.getInt("b");
                int color = nbt.getInt("c");
                return new SMarkActionRequest(left, top, right, bottom, color == 0 ? null : color);
            } catch (Exception e) {
                return null;
            }
        }
    }

    public static class ServerHandler implements BiConsumer<SMarkActionRequest, ServerPlayerEntity> {

        @Override
        public void accept(SMarkActionRequest packet, ServerPlayerEntity player) {
            MarkActionRequestHandler.onReceive(player, packet);
        }
    }
}
