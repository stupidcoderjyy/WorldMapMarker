package mapext.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import mapext.client.gui.ScreenColorSelector;
import mapext.core.Mod;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xaero.map.gui.GuiMap;
import xaero.map.gui.IRightClickableElement;
import xaero.map.gui.MapTileSelection;
import xaero.map.gui.ScreenBase;
import xaero.map.gui.dropdown.rightclick.RightClickOption;

import java.util.ArrayList;

@Mixin(value = GuiMap.class, remap = false)
public abstract class MixinGuiMap extends ScreenBase implements IRightClickableElement {
    @Shadow private MapTileSelection mapTileSelection;

    protected MixinGuiMap(Screen parent, Screen escape, Text titleIn) {
        super(parent, escape, titleIn);
    }

    @Inject(method = "getRightClickOptions", at = @At(value = "RETURN"))
    private void hookAddOption(CallbackInfoReturnable<ArrayList<RightClickOption>> cir, @Local ArrayList<RightClickOption> options) {
        int idx = options.size() - 1;
        int left = mapTileSelection.getLeft();
        int top = mapTileSelection.getTop();
        int right = mapTileSelection.getRight();
        int bottom = mapTileSelection.getBottom();
        boolean hasMarks = false;
        var marksManager = Mod.getMod().getClientData().clientMarksManager();
        var dim = client.world.getRegistryKey().getValue();
        LOOP:
        for (int x = left ; x <= right ; x++) {
            for (int z = top ; z <= bottom ; z++) {
                if (marksManager.getMarkColor(dim, x, z) != null) {
                    hasMarks = true;
                    break LOOP;
                }
            }
        }
        options.add(idx++, new RightClickOption("gui.mapext.mark", idx, this) {
            @Override
            public void onAction(Screen screen) {
                int color = Mod.getMod().getClientData().colorManager().getRgba();
                marksManager.requestAreaMark(left, top, right, bottom, color);
            }
        });
        if (hasMarks) {
            options.add(idx++, new RightClickOption("gui.mapext.unmark", idx, this) {
                @Override
                public void onAction(Screen screen) {
                    marksManager.requestAreaMark(left, top, right, bottom, null);
                }
            });
        }
        var guiMap = this;
        options.add(idx++, new RightClickOption("gui.mapext.setcolor", idx, this) {
            @Override
            public void onAction(Screen screen) {
                client.setScreen(new ScreenColorSelector(guiMap, guiMap));
            }
        });
    }
}
