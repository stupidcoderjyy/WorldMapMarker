package mapext.common.marks;

import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.util.Identifier;

public abstract class MarksManager<
        RM extends RegionMarks,
        DMM extends DimensionMarksManager<RM>> {
    protected final Map<Identifier, DMM> dimensions = new HashMap<>();

    @Nullable
    public Integer getMarkColor(Identifier dim, int chunkX, int chunkZ) {
        return ensureDimension(dim).getMarkColor(chunkX, chunkZ);
    }

    public void setMark(Identifier dim, int chunkX, int chunkZ, @Nullable Integer color) {
        ensureDimension(dim).setMark(chunkX, chunkZ, color);
    }

    public DMM ensureDimension(Identifier dim) {
        return dimensions.computeIfAbsent(dim, this::create);
    }

    public DMM getDimension(Identifier dim) {
        return dimensions.get(dim);
    }

    public void reset() {
        dimensions.clear();
        LogManager.getLogger().info("Client reset");
    }

    protected abstract DMM create(Identifier dimension);

    public static long getRegionLongCoordinates(int chunkX, int chunkZ) {
        return ((long) chunkX << 32) | (chunkZ & 0xFFFFFFFFL);
    }
}
