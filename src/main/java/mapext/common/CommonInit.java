package mapext.common;

import mapext.common.packet.PacketRegistry;
import mapext.common.packet.receiver.ServerPacketReceiver;
import mapext.core.Mod;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class CommonInit {

    public static void initCommon() {
        registerFabricEvents();
        registerServerPacketReceiver();
        PacketRegistry.register();
    }

    private static void registerFabricEvents() {
        ServerLifecycleEvents.SERVER_STARTED.register(CommonEvents::onServerStarted);
        ServerLifecycleEvents.SERVER_STOPPED.register(CommonEvents::onServerStopped);
    }

    private static void registerServerPacketReceiver() {
        ServerPlayNetworking.registerGlobalReceiver(Mod.MAIN_CHANNEL,
                new ServerPacketReceiver(Mod.getMod().getPacketHandler()));
    }

}
