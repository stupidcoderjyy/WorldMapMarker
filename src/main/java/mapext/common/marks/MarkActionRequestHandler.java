package mapext.common.marks;

import mapext.common.packet.marks.CMarkNoPermission;
import mapext.common.packet.marks.SMarkActionRequest;
import mapext.common.server.ServerData;
import mapext.core.Mod;
import net.minecraft.server.network.ServerPlayerEntity;

public class MarkActionRequestHandler {

    public static void onReceive(ServerPlayerEntity player, SMarkActionRequest packet) {
        if (!player.hasPermissionLevel(2)) {
            Mod.getMod().getPacketHandler().sendToPlayer(player, new CMarkNoPermission());
            return;
        }
        var manager = ServerData.fromServer(player.server).serverMarksManager();
        for (int x = packet.left() ; x <= packet.right() ; x++) {
            for (int z = packet.top() ; z <= packet.bottom() ; z++) {
                manager.setMark(player.getWorld().getRegistryKey().getValue(), x, z, packet.color());
            }
        }
    }
}
