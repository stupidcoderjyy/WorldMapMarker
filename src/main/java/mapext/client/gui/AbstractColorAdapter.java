package mapext.client.gui;

import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public abstract class AbstractColorAdapter {
    protected final float[] values = new float[4];
    protected Color renderColor;

    public AbstractColorAdapter(@Nullable AbstractColorAdapter other) {
        if (other == null) {
            for (int i = 0 ; i < 4 ; i ++) {
                values[i] = defaultValue(i);
            }
            updateColor();
        } else {
            setColor(other);
        }
    }

    public AbstractColorAdapter() {
        this(null);
    }

    public int getRgba() {
        return renderColor.getRGB();
    }

    public float getColorNormalized(int i) {
        return values[i];
    }

    public void setColorNormalized(int i, float value) {
        values[i] = value;
        updateColor();
    }

    public void setColor(AbstractColorAdapter other) {
        setRgba(other.getRgba());
    }

    public double denormalizeValue(int i, float val) {
        return val / maxValue(i);
    }

    public float normalizeValue(int i, double val) {
        return (float) val * maxValue(i);
    }

    public double getColorDenormalized(int i) {
        return denormalizeValue(i, getColorNormalized(i));
    }

    public void setColorDenormalized(int i, double value) {
        setColorNormalized(i, normalizeValue(i, value));
    }

    public abstract void setRgba(int rgba);
    protected abstract float defaultValue(int i);
    protected abstract float maxValue(int i);
    protected abstract void updateColor();
    public abstract Text getDisplayComponent(int i);
}
