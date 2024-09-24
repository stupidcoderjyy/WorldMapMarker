package mapext.client.marks;

import mapext.common.marks.MarksManager;
import mapext.common.packet.marks.SMarkActionRequest;
import mapext.core.Mod;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class ClientMarksManager extends MarksManager<ClientRegionMarks, ClientDimensionMarksManager> {

    @Override
    protected ClientDimensionMarksManager create(Identifier dimension) {
        return new ClientDimensionMarksManager(dimension);
    }

    public void requestAreaMark(int left, int top, int right, int bottom, @Nullable Integer color) {
        Mod.getMod().getPacketHandler().sendToServer(new SMarkActionRequest(left, top, right, bottom, color));
    }
}
