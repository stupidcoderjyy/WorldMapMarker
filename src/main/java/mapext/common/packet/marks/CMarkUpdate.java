package mapext.common.packet.marks;

import mapext.client.marks.MarkUpdateHandler;
import mapext.common.packet.ICodec;
import mapext.core.Mod;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public record CMarkUpdate(Identifier dim, int chunkX, int chunkZ, @Nullable Integer color) {

    public static class Codec implements ICodec<CMarkUpdate> {

        @Override
        public void accept(CMarkUpdate packet, PacketByteBuf buf) {
            var nbt = new NbtCompound();
            nbt.putString("dim", packet.dim.getPath());
            nbt.putInt("x", packet.chunkX);
            nbt.putInt("z", packet.chunkZ);
            nbt.putInt("c", packet.color == null ? 0 : packet.color);
            buf.writeNbt(nbt);
        }

        @Override
        public CMarkUpdate apply(PacketByteBuf buf) {
            try {
                var nbt = buf.readUnlimitedNbt();
                int x = nbt.getInt("x");
                int z = nbt.getInt("z");
                int c = nbt.getInt("c");
                Identifier dim = Mod.vanillaLoc(nbt.getString("dim"));
                return new CMarkUpdate(dim, x, z, c == 0 ? null : c);
            } catch (Exception e) {
                return null;
            }
        }
    }

    public static class ClientHandler implements Consumer<CMarkUpdate> {

        @Override
        public void accept(CMarkUpdate packet) {
            MarkUpdateHandler.onReceive(packet);
        }
    }
}
