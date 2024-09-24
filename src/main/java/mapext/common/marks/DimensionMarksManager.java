package mapext.common.marks;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public abstract class DimensionMarksManager<RM extends RegionMarks> {
    private final Long2ObjectMap<RM> regions = new Long2ObjectOpenHashMap<>();
    public final Identifier dim;

    public DimensionMarksManager(Identifier dim) {
        this.dim = dim;
    }

    @Nullable
    public Integer getMarkColor(int chunkX, int chunkZ) {
        var rm = getRegion(chunkX >> 5, chunkZ >> 5);
        if (rm == null) {
            return null;
        }
        return rm.getMarkColor(chunkX & 31, chunkZ & 31);
    }

    public void setMark(int chunkX, int chunkZ, @Nullable Integer color) {
        int regionX = chunkX >> 5;
        int regionZ = chunkZ >> 5;
        long key = getRegionKey(regionX, regionZ);
        RM region = regions.get(key);
        if (region == null) {
            if (color == null) {
                return;
            }
            region = createRegion();
            setRegion(regionX, regionZ, region);
        }
        region.setMark(chunkX & 31, chunkZ & 31, color);
        if (region.isEmpty()) {
            regions.remove(key);
        }
    }

    protected abstract RM createRegion();

    public RM getRegion(int regionX, int regionZ) {
        return regions.get(getRegionKey(regionX, regionZ));
    }

    public long getRegionKey(int regionX, int regionZ) {
        return MarksManager.getRegionLongCoordinates(regionX, regionZ);
    }

    protected void setRegion(int regionX, int regionZ, RM region) {
        regions.put(getRegionKey(regionX, regionZ), region);
    }
}
