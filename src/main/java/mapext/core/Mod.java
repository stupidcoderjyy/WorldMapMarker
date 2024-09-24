package mapext.core;

import mapext.client.ClientData;
import mapext.client.ClientInit;
import mapext.common.CommonInit;
import mapext.common.packet.PacketHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Mod implements ClientModInitializer, DedicatedServerModInitializer {
    public static final String MOD_ID = "mapext";
    public static final Identifier MAIN_CHANNEL = Mod.modLoc("main");
    private static Mod instance;
    @Nullable
    private ClientData clientData;
    private final PacketHandler packetHandler = new PacketHandler();

    public Mod() {
        instance = this;
    }

    @Override
    public void onInitializeClient() {
        CommonInit.initCommon();
        ClientInit.initClient();
    }

    @Override
    public void onInitializeServer() {
        CommonInit.initCommon();
    }

    public void setClientData(ClientData clientData) {
        this.clientData = clientData;
    }

    @NotNull
    public ClientData getClientData() {
        if (clientData == null) {
            throw new NullPointerException("missing client or not initialized");
        }
        return clientData;
    }

    public PacketHandler getPacketHandler() {
        return packetHandler;
    }

    public static Mod getMod() {
        return instance;
    }

    public static Identifier modLoc(String path) {
        return new Identifier(MOD_ID, path);
    }

    public static Identifier vanillaLoc(String path) {
        return new Identifier(path);
    }

    public static Identifier expandLoc(String prefix, Identifier loc) {
        String path = loc.getPath();
        if (path.indexOf('/') > 0) {
            return loc;
        }
        return new Identifier(loc.getNamespace(), prefix + "/" + path);
    }
}
