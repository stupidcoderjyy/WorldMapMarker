package mapext.common.server;

import mapext.common.server.io.MarksIOManager;
import mapext.common.server.marks.ServerDimensionMarksManager;
import mapext.common.server.marks.ServerMarksManager;
import mapext.common.server.marks.ServerRegionMarks;
import net.minecraft.server.MinecraftServer;

public class ServerInit {
    public static void onServerAboutToStart(MinecraftServer server) {
        ((IModServer)server).set_server_data(buildServerData(server));
        ServerDimensionMarksManager.setServer(server);
    }

    private static ServerData buildServerData(MinecraftServer server) {
        var serverMarksManager = new ServerMarksManager();
        var marksIOManager = MarksIOManager.build(serverMarksManager, server);
        return new ServerData(server, serverMarksManager, marksIOManager);
    }
}
