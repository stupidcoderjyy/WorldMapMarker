package mapext.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import mapext.client.highlight.Highlighter;
import mapext.core.Mod;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xaero.map.WorldMapSession;
import xaero.map.highlight.HighlighterRegistry;

@Mixin(WorldMapSession.class)
public class MixinWorldMapSession {

    @Inject(method = "init", at = @At(value = "INVOKE", target = "Lxaero/map/highlight/HighlighterRegistry;end()V"), remap = false)
    private void hookRegisterHighlighter(CallbackInfo ci, @Local HighlighterRegistry hr) {
        var manager = Mod.getMod().getClientData().clientMarksManager();
        hr.register(new Highlighter(manager));
    }
}
