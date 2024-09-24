package mapext.client.highlight;

import mapext.client.marks.ClientMarksManager;
import mapext.client.marks.ClientRegionMarks;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.VisibleForTesting;
import xaero.map.highlight.ChunkHighlighter;

import java.util.Arrays;
import java.util.List;

public class Highlighter extends ChunkHighlighter {
    private final ClientMarksManager manager;

    public Highlighter(ClientMarksManager manager) {
        super(false);
        this.manager = manager;
    }

    @Override
    protected int[] getColors(RegistryKey<World> dim, int chunkX, int chunkZ) {
        Integer center = manager.getMarkColor(dim.getValue(), chunkX, chunkZ);
        if (center == null) {
            return null;
        }
        // 这里传入的颜色是bgra，而不是argb
        Arrays.fill(resultStore, 0, 5, processColor(center));
        return resultStore;
    }

    private static int processColor(int color) {
        int a = (color >> 24) & 0xFF;
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;
        return (b << 24) | (g << 16) | (r << 8) | a;
    }

    @Override
    public boolean regionHasHighlights(RegistryKey<World> dimension, int regionX, int regionZ) {
        var dmm = manager.getDimension(dimension.getValue());
        return dmm != null && dmm.getRegion(regionX, regionZ) != null;
    }

    @Override
    public int calculateRegionHash(RegistryKey<World> dimension, int regionX, int regionZ) {
        var dim = manager.getDimension(dimension.getValue());
        if (dim == null) {
            return 0;
        }
        var region = dim.getRegion(regionX, regionZ);
        if (region == null) {
            return 0;
        }
        long ac = 0;
        var top = dim.getRegion(regionX, regionZ - 1);
        var right = dim.getRegion(regionX + 1, regionZ);
        var bottom = dim.getRegion(regionX, regionZ + 1);
        var left = dim.getRegion(regionX - 1, regionZ);
        for (int i = 0 ; i < 32 ; i++) {
            ac = accountMark(ac, top == null ? null : top.getMarkColor(i, 31));
            ac = accountMark(ac, right == null ? null : right.getMarkColor(0, i));
            ac = accountMark(ac, left == null ? null : left.getMarkColor(31, i));
            ac = accountMark(ac, bottom == null ? null : bottom.getMarkColor(i, 0));
            for (int j = 0 ; j < 32 ; j ++) {
                ac = accountMark(ac, region.getMarkColor(i, j));
            }
        }
        return (int)(ac >> 32) * 37 + (int)ac;
    }

    private static long accountMark(long ac, @Nullable Integer color) {
        return color == null ? ac : (ac + color) * 37;
    }

    @Override
    public boolean chunkIsHighlit(RegistryKey<World> dim, int chunkX, int chunkZ) {
        return manager.getMarkColor(dim.getValue(), chunkX, chunkZ) != null;
    }

    @Override
    public Text getChunkHighlightSubtleTooltip(RegistryKey<World> dimension, int chunkX, int chunkZ) {
        return null;
    }

    @Override
    public Text getChunkHighlightBluntTooltip(RegistryKey<World> dimension, int chunkX, int chunkZ) {
        return null;
    }

    @Override
    public void addMinimapBlockHighlightTooltips(List<Text> list, RegistryKey<World> dimension, int blockX, int blockZ, int width) {
    }
}
