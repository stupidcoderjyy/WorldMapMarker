package mapext.client.gui;

import net.minecraft.text.Text;

import java.awt.*;

public class RgbAdapter extends AbstractColorAdapter {
    public static final int RED = 0;
    public static final int GREEN = 1;
    public static final int BLUE = 2;
    public static final int ALPHA = 3;

    public RgbAdapter() {
    }

    public RgbAdapter(AbstractColorAdapter other) {
        super(other);
    }

    @Override
    public void setRgba(int rgba) {
        renderColor = new Color(rgba, true);
        values[RED] = renderColor.getRed();
        values[GREEN] = renderColor.getGreen();
        values[BLUE] = renderColor.getBlue();
        values[ALPHA] = renderColor.getAlpha();
    }

    @Override
    protected float defaultValue(int i) {
        return 127;
    }

    @Override
    protected float maxValue(int i) {
        return 255;
    }

    @Override
    protected void updateColor() {
        int r = Math.round(values[RED]);
        int g = Math.round(values[GREEN]);
        int b = Math.round(values[BLUE]);
        int a = Math.round(values[ALPHA]);
        renderColor = new Color(r, g, b, a);
    }

    @Override
    public Text getDisplayComponent(int i) {
        String key = "gui.mapext." + switch (i) {
            case RED -> "red";
            case GREEN -> "green";
            case BLUE -> "blue";
            case ALPHA -> "alpha";
            default -> "";
        };
        String color = String.valueOf(Math.round(values[i]));
        return Text.translatable(key).append(":" + color);
    }
}
