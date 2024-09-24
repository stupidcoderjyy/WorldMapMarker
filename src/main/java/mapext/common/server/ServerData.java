package mapext.common.server;

import mapext.common.server.io.MarksIOManager;
import mapext.common.server.marks.ServerMarksManager;
import net.minecraft.server.MinecraftServer;

public record ServerData(
        MinecraftServer server, ServerMarksManager serverMarksManager, MarksIOManager marksIOManager) {

    public static ServerData fromServer(MinecraftServer server) {
        return ((IModServer) server).get_server_data();
    }
}
