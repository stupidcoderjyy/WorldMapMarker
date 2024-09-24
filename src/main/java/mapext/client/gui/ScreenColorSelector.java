package mapext.client.gui;

import mapext.core.Mod;
import mapext.util.gui.GraphicsUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import xaero.map.gui.ScreenBase;

public class ScreenColorSelector extends ScreenBase {
    private static final Identifier LOC_BG = Mod.modLoc("gui/bg.png");
    private final ColorManager colorManager = new ColorManager(EnumColorSpace.RGB);
    private final AbstractColorAdapter globalColor = Mod.getMod().getClientData().colorManager();
    private TextFieldWidget colorHexField;

    public ScreenColorSelector(Screen parent, Screen escape) {
        super(parent, escape, Text.literal("title"));
    }

    @Override
    protected void init() {
        super.init();
        colorManager.getAdapter().setColor(globalColor);
        int sliderX = width / 2 - 120, sliderY = height / 2 - 95;
        var hueSlider = new ColorSliderButton(colorManager, 0, sliderX, sliderY, 100, 20);
        var satSlider = new ColorSliderButton(colorManager, 1, sliderX, sliderY + 25, 100, 20);
        var valSlider = new ColorSliderButton(colorManager, 2, sliderX, sliderY + 50, 100, 20);
        var alphaSlider = new ColorSliderButton(colorManager, 3, sliderX, sliderY + 75, 100, 20);
        var cancelButton = ButtonWidget.builder(
                Text.translatable("gui.mapext.cancel"), b -> goBack())
                .dimensions(this.width / 2 - 130, this.height / 2 + 60, 120, 20)
                .build();
        var confirmButton = ButtonWidget.builder(
                        Text.translatable("gui.mapext.confirm"), b -> confirmSetColor())
                .dimensions(this.width / 2 + 10, this.height / 2 + 60, 120, 20)
                .build();
        var colorSpaceButton = new ColorModeButtonBuilder(colorManager).
                build(width / 2 + 90, height / 2 - 95, 70);
        colorHexField = new TextFieldWidget(client.textRenderer,
                width / 2 + 20, height / 2 - 20,
                70, 20, Text.empty());
        colorHexField.setChangedListener(this::onFieldTextChange);
        colorHexField.setMaxLength(8);
        colorManager.addColorChangeCallback(this::setFieldText);
        colorManager.updateField();
        addDrawableChild(hueSlider);
        addDrawableChild(satSlider);
        addDrawableChild(valSlider);
        addDrawableChild(alphaSlider);
        addDrawableChild(cancelButton);
        addDrawableChild(confirmButton);
        addDrawableChild(colorSpaceButton);
        addDrawableChild(colorHexField);
    }

    @Override
    public void render(DrawContext graphics, int mouseX, int mouseY, float partial) {
        renderEscapeScreen(graphics, mouseX, mouseY, partial); //地图背景
        renderBackground(graphics); //灰色背景
        int rectX = width / 2 + 20;
        int rectY = height / 2 - 95;
        GraphicsUtil.renderImg(graphics, LOC_BG, rectX, rectY, 64, 64, 0, 0);
        GraphicsUtil.fill(graphics, rectX, rectY, 64, 64, colorManager.getAdapter().getRgba());
        super.render(graphics, mouseX, mouseY, partial);
    }

    private void confirmSetColor() {
        globalColor.setColor(colorManager.getAdapter());
        goBack();
    }

    private void onFieldTextChange(String text) {
        if (!colorHexField.isFocused()) {
            return;
        }
        try {
            int rgba = Integer.parseUnsignedInt(text, 16);
            colorManager.updateColorFromField(rgba);
        } catch (Exception ignored) {
        }
    }

    private void setFieldText(int rgba) {
        if (colorHexField.isFocused()) {
            return;
        }
        String text = Integer.toHexString(rgba);
        text = "0".repeat(8 - text.length()) + text;
        colorHexField.setText(text);
    }
}
