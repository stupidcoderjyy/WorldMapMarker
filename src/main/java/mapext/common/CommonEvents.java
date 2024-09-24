package mapext.common;

import mapext.common.server.ServerData;
import net.minecraft.server.MinecraftServer;

public class CommonEvents {

    public static void onServerStarted(MinecraftServer server) {
        ServerData.fromServer(server).marksIOManager().load();
    }

    public static void onServerStopped(MinecraftServer server) {
        ServerData.fromServer(server).marksIOManager().save();
    }
}
