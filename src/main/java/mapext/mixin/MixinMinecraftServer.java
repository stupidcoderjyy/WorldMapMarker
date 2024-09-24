package mapext.mixin;

import mapext.common.server.IModServer;
import mapext.common.server.ServerData;
import mapext.common.server.ServerInit;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MixinMinecraftServer implements IModServer {
    @Unique
    private ServerData data;

    @Inject(at = @At("HEAD"), method = "loadWorld")
    public void onLoadLevel(CallbackInfo ci) {
        ServerInit.onServerAboutToStart((MinecraftServer)(Object)this);
    }

    @Override
    public ServerData get_server_data() {
        return data;
    }

    @Override
    public void set_server_data(ServerData data) {
        this.data = data;
    }
}
