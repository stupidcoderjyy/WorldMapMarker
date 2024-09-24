package mapext.client.marks;

import mapext.common.packet.marks.CMarkUpdate;
import mapext.core.Mod;

public class MarkUpdateHandler {
    public static void onReceive(CMarkUpdate packet) {
        Mod.getMod().getClientData().clientMarksManager()
                .setMark(packet.dim(), packet.chunkX(), packet.chunkZ(), packet.color());
        WorldMapUpdate.onChunkChange(packet.dim(), packet.chunkX(), packet.chunkZ());
    }
}
