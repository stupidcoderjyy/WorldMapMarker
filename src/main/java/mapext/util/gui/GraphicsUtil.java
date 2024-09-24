package mapext.util.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public class GraphicsUtil {

    public static void renderImg(DrawContext g, Identifier loc,
                                 int x, int y, int z, int w, int h,
                                 float textX, float textY) {
        g.drawTexture(loc, x, y, z, textX, textY, w, h, 256, 256);
    }

    public static void renderImg(DrawContext g, Identifier loc,
                                 int x, int y, int w, int h,
                                 float textX, float textY) {
        g.drawTexture(loc, x, y, 0, textX, textY, w, h, 256, 256);
    }

    public static void renderImg(DrawContext g, Identifier loc,
                                 int x, int y, int w, int h,
                                 int textX, int textY, int textW, int textH) {
        g.drawTexture(loc, x, y, w, h, textX, textY, textW, textH, 256, 256);
    }

    public static void fill(DrawContext g, int x, int y, int w, int h, int rgba) {
        g.fill(x, y, x + w, y + h, rgba);
    }
}
