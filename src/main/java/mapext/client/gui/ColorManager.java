package mapext.client.gui;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ColorManager {
    private AbstractColorAdapter adapter;
    private EnumColorSpace colorSpace;
    private final List<ISwitchColorSpaceCallback> colorSpaceCallbacks = new ArrayList<>();
    private final List<IFieldColorChangeCallback> fieldColorChangeCallbacks = new ArrayList<>();
    private final List<IColorChangeCallback> colorChangeCallbacks = new ArrayList<>();

    public ColorManager(EnumColorSpace defaultSpace) {
        switchColorSpace(defaultSpace);
    }

    public void switchColorSpace(@NotNull EnumColorSpace colorSpace) {
        if (this.colorSpace == colorSpace) {
            return;
        }
        adapter = colorSpace.adapterBuilder.apply(adapter);
        this.colorSpace = colorSpace;
        colorSpaceCallbacks.forEach(ISwitchColorSpaceCallback::onSwitchColorSpace);
    }

    public void updateColorFromField(int rgba) {
        adapter.setRgba(rgba);
        fieldColorChangeCallbacks.forEach(c -> c.onSetColorFromField(rgba));
    }

    public void updateField() {
        int color = adapter.getRgba();
        colorChangeCallbacks.forEach(c -> c.onColorChanged(color));
    }

    public void addSwitchCallback(ISwitchColorSpaceCallback callback) {
        colorSpaceCallbacks.add(callback);
    }

    public void addColorChangeCallback(IColorChangeCallback callback) {
        colorChangeCallbacks.add(callback);
    }

    public void addFieldColorChangeCallback(IFieldColorChangeCallback callback) {
        fieldColorChangeCallbacks.add(callback);
    }

    public AbstractColorAdapter getAdapter() {
        return adapter;
    }

    public interface ISwitchColorSpaceCallback {
        void onSwitchColorSpace();
    }

    public interface IFieldColorChangeCallback {
        void onSetColorFromField(int color);
    }

    public interface IColorChangeCallback {
        void onColorChanged(int color);
    }
}
