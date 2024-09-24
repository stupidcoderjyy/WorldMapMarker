package mapext.common.server.marks;

import mapext.common.marks.DimensionMarksManager;
import mapext.common.marks.RegionMarkData;
import mapext.common.packet.marks.CDimensionSync;
import mapext.common.packet.marks.CMarkUpdate;
import mapext.core.Mod;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class ServerDimensionMarksManager extends DimensionMarksManager<ServerRegionMarks> {
    private static MinecraftServer server;
    private final DimensionMarksInfo marksInfo;
    private boolean isDirty;
    private boolean loaded = true;

    public ServerDimensionMarksManager(Identifier dim) {
        super(dim);
        this.marksInfo = new DimensionMarksInfo(dim);
    }

    @Override
    public void setMark(int chunkX, int chunkZ, @Nullable Integer color) {
        super.setMark(chunkX, chunkZ, color);
        if (marksInfo.put(chunkX, chunkZ, color) && loaded) {
            setDirty(true);
        }
        if (loaded) {
            server.getPlayerManager().getPlayerList().forEach(p ->
                    Mod.getMod().getPacketHandler().sendToPlayer(p, new CMarkUpdate(dim, chunkX, chunkZ, color)));
        }
     }

    public void setDirty(boolean dirty) {
        isDirty = dirty;
    }

    public boolean isDirty() {
        return isDirty;
    }

    public DimensionMarksInfo getMarksInfo() {
        return marksInfo;
    }

    public void syncDimensionMarks(ServerPlayerEntity player) {
        Mod.getMod().getPacketHandler().sendToPlayer(player, new CDimensionSync(dim, marksInfo.getMarks()));
    }

    @Override
    protected ServerRegionMarks createRegion() {
        return new ServerRegionMarks(new RegionMarkData());
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public static void setServer(MinecraftServer s) {
        server = s;
    }
}
