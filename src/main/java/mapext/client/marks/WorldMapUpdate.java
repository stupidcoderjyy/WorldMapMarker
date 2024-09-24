package mapext.client.marks;

import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import xaero.map.WorldMapSession;
import xaero.map.world.MapDimension;

public class WorldMapUpdate {
    public static void onChunkChange(Identifier dimension, int chunkX, int chunkZ) {
        WorldMapSession session = WorldMapSession.getCurrentSession();
        MapDimension mapDim = session.getMapProcessor().getMapWorld().getDimension(RegistryKey.of(RegistryKeys.WORLD, dimension));
        if (mapDim != null) {
            for(int i = -1; i < 2; ++i) {
                for(int j = -1; j < 2; ++j) {
                    if (i == 0 && j == 0 || i * i != j * j) {
                        mapDim.getHighlightHandler().clearCachedHash(chunkX + i >> 5, chunkZ + j >> 5);
                    }
                }
            }
        }
    }
}
