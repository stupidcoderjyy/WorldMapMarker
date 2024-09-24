package mapext.common.packet;

import mapext.common.packet.marks.*;
import mapext.core.Mod;

public class PacketRegistry {
    public static void register() {
        var handler = Mod.getMod().getPacketHandler();
        handler.register(0,
                SMarkActionRequest.class,
                new SMarkActionRequest.Codec(),
                new SMarkActionRequest.ServerHandler(),
                null);
        handler.register(1,
                CMarkUpdate.class,
                new CMarkUpdate.Codec(),
                null,
                new CMarkUpdate.ClientHandler());
        handler.register(2,
                CDimensionSync.class,
                new CDimensionSync.Codec(),
                null,
                new CDimensionSync.ClientHandler());
        handler.register(3,
                SLogIn.class,
                new SLogIn.Codec(),
                null,
                new SLogIn.ClientHandler());
        handler.register(4,
                CMarkNoPermission.class,
                new CMarkNoPermission.Codec(),
                null,
                new CMarkNoPermission.ClientHandler());
    }
}
