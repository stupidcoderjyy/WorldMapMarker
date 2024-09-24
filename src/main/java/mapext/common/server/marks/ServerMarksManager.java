package mapext.common.server.marks;

import mapext.common.marks.MarksManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.Map;

public class ServerMarksManager extends MarksManager<ServerRegionMarks, ServerDimensionMarksManager> {
    public Map<Identifier, ServerDimensionMarksManager> getDimensions() {
        return dimensions;
    }

    @Override
    protected ServerDimensionMarksManager create(Identifier dimension) {
        return new ServerDimensionMarksManager(dimension);
    }

    public void syncAll(ServerPlayerEntity player) {
        dimensions.forEach((dim, m) -> m.syncDimensionMarks(player));
    }
}
