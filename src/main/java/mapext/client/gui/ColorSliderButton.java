package mapext.client.gui;

import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

public class ColorSliderButton extends SliderWidget {
    private final ColorManager manager;
    private final int colorIdx;

    public ColorSliderButton(ColorManager manager, int i, int x, int y, int w, int h) {
        super(x, y, w, h, Text.empty(), 0);
        manager.addSwitchCallback(this::onAdapterChange);
        manager.addFieldColorChangeCallback(c -> onAdapterChange());
        this.manager = manager;
        this.colorIdx = i;
        onAdapterChange();
    }

    private void onAdapterChange() {
        this.value = manager.getAdapter().getColorDenormalized(colorIdx);
        updateMessage();
    }

    @Override
    protected void updateMessage() {
        setMessage(manager.getAdapter().getDisplayComponent(colorIdx));
    }

    @Override
    protected void applyValue() {
        manager.getAdapter().setColorDenormalized(colorIdx,
                MathHelper.clamp(this.value, 0.0, 1.0));
        manager.updateField();
    }
}
