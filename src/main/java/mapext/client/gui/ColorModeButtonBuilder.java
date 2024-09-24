package mapext.client.gui;

import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.text.Text;

import java.util.function.Function;

public class ColorModeButtonBuilder {
    private final ColorManager manager;

    public ColorModeButtonBuilder(ColorManager manager) {
        this.manager = manager;
    }

    public ClickableWidget build(int x, int y, int width) {
        Function<Boolean, Text> textGetter =
                b -> Text.translatable("gui.mapext.colorspace." + (b ? "rgb" : "hsb"));
        return CyclingButtonWidget.builder(textGetter)
                .values(true, false)
                .initially(true)
                .build(x, y, width, 20, Text.translatable("gui.mapext.colorspace"), this::onClicked);
    }


    private void onClicked(CyclingButtonWidget<Boolean> button, Boolean b) {
        manager.switchColorSpace(b ? EnumColorSpace.RGB : EnumColorSpace.HSB);
    }
}
