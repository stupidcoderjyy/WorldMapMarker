package mapext.client.gui;

import java.util.function.Function;

public enum EnumColorSpace {
    HSB(HsbAdapter::new),
    RGB(RgbAdapter::new)
    ;
    public final Function<AbstractColorAdapter, AbstractColorAdapter> adapterBuilder;


    EnumColorSpace(Function<AbstractColorAdapter, AbstractColorAdapter> adapterBuilder) {
        this.adapterBuilder = adapterBuilder;
    }
}
