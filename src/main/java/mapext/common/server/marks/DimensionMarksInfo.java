package mapext.common.server.marks;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ChunkPos;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DimensionMarksInfo {
    public final Identifier dim;
    private final Map<ChunkPos, Integer> marks = new HashMap<>();

    public DimensionMarksInfo(Identifier dim) {
        this.dim = dim;
    }

    public boolean put(int x, int z, @Nullable Integer color) {
        var pos = new ChunkPos(x, z);
        if (color == null) {
            return marks.remove(pos) != null;
        }
        return !Objects.equals(marks.put(pos, color), color);
    }

    public Map<ChunkPos, Integer> getMarks() {
        return marks;
    }

    public static NbtCompound serialize(DimensionMarksInfo info) {
        var list = new NbtList();
        info.marks.forEach((pos, color) -> {
            var infoTag = new NbtCompound();
            infoTag.putInt("x", pos.x);
            infoTag.putInt("z", pos.z);
            infoTag.putInt("c", color);
            list.add(infoTag);
        });
        var nbt = new NbtCompound();
        nbt.put("pos", list);
        return nbt;
    }

    public static DimensionMarksInfo deserialize(Identifier dim, NbtCompound nbt) {
        var list = nbt.getList("pos", NbtElement.COMPOUND_TYPE);
        var dimInfo = new DimensionMarksInfo(dim);
        list.forEach(e ->  {
            var infoTag = (NbtCompound) e;
            int x = infoTag.getInt("x");
            int z = infoTag.getInt("z");
            int c = infoTag.getInt("c");
            dimInfo.marks.put(new ChunkPos(x, z), c);
        });
        return dimInfo;
    }
}
