package mapext.client.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;

import java.awt.Color;

@Environment(EnvType.CLIENT)
public class HsbAdapter extends AbstractColorAdapter {
    public static final int HUE = 0;
    public static final int SATURATION = 1;
    public static final int BRIGHTNESS = 2;
    public static final int ALPHA = 3;

    public HsbAdapter(AbstractColorAdapter other) {
        super(other);
    }

    @Override
    public void setRgba(int rgba) {
        renderColor = new Color(rgba, true);
        Color.RGBtoHSB(renderColor.getRed(), renderColor.getGreen(), renderColor.getBlue(), values);
        values[ALPHA] = renderColor.getAlpha();
    }

    @Override
    public float defaultValue(int i) {
        return switch (i) {
            case HUE -> 0;
            case SATURATION, BRIGHTNESS -> 0.5f;
            case ALPHA -> 128;
            default -> -1;
        };
    }

    @Override
    public float maxValue(int i) {
        return switch (i) {
            case HUE, SATURATION, BRIGHTNESS -> 1.0f;
            case ALPHA -> 255;
            default -> -1;
        };
    }

    @Override
    protected void updateColor() {
        int value = Color.HSBtoRGB(values[HUE], values[SATURATION], values[BRIGHTNESS]);
        renderColor = new Color((value & 0xFFFFFF) | (Math.round(values[ALPHA]) << 24), true);
    }

    @Override
    public Text getDisplayComponent(int i) {
        String key = "gui.mapext." + switch (i) {
            case HUE -> "hue";
            case SATURATION -> "saturation";
            case BRIGHTNESS -> "brightness";
            case ALPHA -> "alpha";
            default -> "";
        };
        String color = String.valueOf(switch (i) {
            case HUE, SATURATION, BRIGHTNESS -> String.format("%.2f", values[i]);
            case ALPHA -> Math.round(values[i]);
            default -> "";
        });
        return Text.translatable(key).append(":" + color);
    }
}
