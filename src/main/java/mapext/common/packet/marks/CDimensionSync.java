package mapext.common.packet.marks;

import mapext.client.marks.ClientMarksManager;
import mapext.common.packet.ICodec;
import mapext.core.Mod;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ChunkPos;
import org.apache.logging.log4j.LogManager;

import java.util.Map;
import java.util.function.Consumer;

public class CDimensionSync {
    private final Identifier dim;
    private final int[] xArr, zArr, colors;

    public CDimensionSync(Identifier dim, Map<ChunkPos, Integer> marks) {
        this.dim = dim;
        xArr = new int[marks.size()];
        zArr = new int[marks.size()];
        colors = new int[marks.size()];
        var it = marks.entrySet().iterator();
        int i = 0;
        while (it.hasNext()) {
            var e = it.next();
            xArr[i] = e.getKey().x;
            zArr[i] = e.getKey().z;
            colors[i] = e.getValue() == null ? 0 : e.getValue();
            i++;
        }
    }

    public CDimensionSync(Identifier dim, int[] xArr, int[] zArr, int[] colors) {
        this.dim = dim;
        this.xArr = xArr;
        this.zArr = zArr;
        this.colors = colors;
    }

    public static class Codec implements ICodec<CDimensionSync> {
        @Override
        public void accept(CDimensionSync packet, PacketByteBuf buf) {
            var nbt = new NbtCompound();
            nbt.putString("dim", packet.dim.getPath());
            nbt.putIntArray("x", packet.xArr);
            nbt.putIntArray("z", packet.zArr);
            nbt.putIntArray("c", packet.colors);
            buf.writeNbt(nbt);
        }

        @Override
        public CDimensionSync apply(PacketByteBuf buf) {
            var nbt = buf.readUnlimitedNbt();
            int[] x = nbt.getIntArray("x");
            int[] z = nbt.getIntArray("z");
            int[] c = nbt.getIntArray("c");
            Identifier loc = Mod.vanillaLoc(nbt.getString("dim"));
            return new CDimensionSync(loc, x, z, c);
        }
    }

    public static class ClientHandler implements Consumer<CDimensionSync> {
        @Override
        public void accept(CDimensionSync packet) {
            ClientMarksManager manager = Mod.getMod().getClientData().clientMarksManager();
            for (int i = 0 ; i < packet.zArr.length ; i ++) {
                manager.setMark(packet.dim, packet.xArr[i], packet.zArr[i],
                        packet.colors[i] == 0 ? null : packet.colors[i]);
            }
            LogManager.getLogger().info("Marks initial sync");
        }
    }
}
