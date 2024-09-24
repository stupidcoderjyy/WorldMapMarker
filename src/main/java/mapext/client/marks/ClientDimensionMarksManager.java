package mapext.client.marks;

import mapext.common.marks.DimensionMarksManager;
import mapext.common.marks.RegionMarkData;
import net.minecraft.util.Identifier;

public class ClientDimensionMarksManager extends DimensionMarksManager<ClientRegionMarks> {
    public ClientDimensionMarksManager(Identifier dim) {
        super(dim);
    }

    @Override
    protected ClientRegionMarks createRegion() {
        return new ClientRegionMarks(new RegionMarkData());
    }
}
