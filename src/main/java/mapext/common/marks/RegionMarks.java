package mapext.common.marks;

import org.jetbrains.annotations.Nullable;

public abstract class RegionMarks {
    protected final RegionMarkData markData;

    public RegionMarks(RegionMarkData markData) {
        this.markData = markData;
    }

    @Nullable
    public Integer getMarkColor(int xInRegion, int zInRegion) {
        return markData.getMarkColor(xInRegion, zInRegion);
    }

    protected void setMark(int xInRegion, int zInRegion, @Nullable Integer color) {
        markData.setMarkColor(xInRegion, zInRegion, color);
    }

    public boolean isEmpty() {
        return markData.isEmpty();
    }
}
