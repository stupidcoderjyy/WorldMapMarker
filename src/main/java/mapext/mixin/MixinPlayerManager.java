package mapext.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import mapext.common.packet.marks.SLogIn;
import mapext.common.server.ServerData;
import mapext.core.Mod;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public class MixinPlayerManager {
    @Shadow @Final private MinecraftServer server;

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;onSpawn()V"), method = "onPlayerConnect")
    public void syncMarksOnPlayerConnect(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
        Mod.getMod().getPacketHandler().sendToPlayer(player, new SLogIn());
        ServerData.fromServer(server).serverMarksManager().syncAll(player);
    }
}
