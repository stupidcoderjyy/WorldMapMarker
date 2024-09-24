package mapext.client;

import mapext.client.gui.HsbAdapter;
import mapext.client.gui.RgbAdapter;
import mapext.client.marks.ClientMarksManager;
import mapext.common.packet.receiver.ClientPacketReceiver;
import mapext.core.Mod;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class ClientInit {

    public static void initClient() {
        var clientMarksManager = new ClientMarksManager();
        var colorManager = new RgbAdapter();
        Mod.getMod().setClientData(new ClientData(clientMarksManager, colorManager));
        registerClientPacketReceiver();
    }

    private static void registerClientPacketReceiver() {
        ClientPlayNetworking.registerGlobalReceiver(Mod.MAIN_CHANNEL,
                new ClientPacketReceiver(Mod.getMod().getPacketHandler()));
    }
}
