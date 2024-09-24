package mapext.common.marks;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntStack;
import net.minecraft.util.collection.PackedIntegerArray;
import net.minecraft.util.collection.PaletteStorage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.jetbrains.annotations.Nullable;

public class RegionMarkData {
    private final List<Integer> idxToColor = new ArrayList<>();
    private final IntStack unusedIdx = new IntArrayList();
    private final PaletteStorage chunkPosToIdx = new PackedIntegerArray(11, 1024);

    public RegionMarkData() {
        idxToColor.add(null);
    }

    public void setMarkColor(int chunkX, int chunkZ, @Nullable Integer color) {
        int pos = getLongChunkPos(chunkX, chunkZ);
        int idx = chunkPosToIdx.get(pos);
        if (color == null) {
            if (idx > 0) {
                removeElement(pos, idx);
            }
            return;
        }
        Integer oldColor = idxToColor.get(idx);
        if (Objects.equals(oldColor, color)) {
            return;
        }
        int newId = offerNewIdx();
        chunkPosToIdx.set(pos, newId);
        if (newId < idxToColor.size()) {
            idxToColor.set(newId, color);
        } else {
            idxToColor.add(color);
        }
    }

    private void removeElement(int pos, int idx) {
        chunkPosToIdx.set(pos, 0);
        idxToColor.set(idx, null);
        if (idx != idxToColor.size() - 1) {
            unusedIdx.push(idx);
            return;
        }
        while (idxToColor.size() > 1 && idxToColor.get(idx) == null) {
            idxToColor.remove(idx--);
        }
        while (!unusedIdx.isEmpty() && unusedIdx.topInt() > idx) {
            unusedIdx.popInt();
        }
    }

    private int offerNewIdx() {
        if (unusedIdx.isEmpty()) {
            return idxToColor.size();
        }
        return unusedIdx.popInt();
    }

    public boolean isEmpty() {
        return idxToColor.size() == 1;
    }

    @Nullable
    public Integer getMarkColor(int xInRegion, int zInRegion) {
        int idx = chunkPosToIdx.get(getLongChunkPos(xInRegion, zInRegion));
        if (idx >= idxToColor.size()) {
            LogManager.getLogger().error("idx = {}, idxToColor = {}", idx, idxToColor.toString());
            return null;
        }
        return idxToColor.get(idx);
    }

    public static int getLongChunkPos(int x, int z) {
        return (x << 5) | z;
    }
}
