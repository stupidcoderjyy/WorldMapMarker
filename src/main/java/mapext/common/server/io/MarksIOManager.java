package mapext.common.server.io;

import mapext.common.server.marks.DimensionMarksInfo;
import mapext.common.server.marks.ServerDimensionMarksManager;
import mapext.common.server.marks.ServerMarksManager;
import mapext.core.Mod;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.util.WorldSavePath;
import org.apache.logging.log4j.LogManager;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class MarksIOManager {
    private final ServerMarksManager marksManager;
    private final Path marksFolder;

    private MarksIOManager(ServerMarksManager marksManager, Path marksFolder) {
        this.marksManager = marksManager;
        this.marksFolder = marksFolder;
    }

    public void save() {
        long before = System.currentTimeMillis();
        for (var e : marksManager.getDimensions().entrySet()) {
            if (System.currentTimeMillis() - before > 10) {
                break;
            }
            var dimManager = e.getValue();
            if (!dimManager.isDirty()) {
                break;
            }
            saveFile(dimManager);
            dimManager.setDirty(false);
        }
    }

    private void saveFile(ServerDimensionMarksManager manager) {
        var file = getFilePath(manager);
        try (var out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file.toFile())))) {
            var nbt = DimensionMarksInfo.serialize(manager.getMarksInfo());
            NbtIo.write(nbt, out);
        } catch (Exception e) {
            LogManager.getLogger().error(e);
        }
    }

    public void load() {
        try {
            Files.createDirectories(marksFolder);
            try (var paths = Files.list(marksFolder)) {
                paths.forEach(f -> {
                    if (Files.isDirectory(f)) {
                        return;
                    }
                    loadFile(f);
                });
            }
        } catch (Exception e) {
            LogManager.getLogger().error(e);
        }
    }

    private void loadFile(Path f) {
        String fileName = f.getFileName().toString();
        if (!fileName.endsWith(".dat")) {
            return;
        }
        Identifier dim = Mod.vanillaLoc(fileName.substring(0, fileName.lastIndexOf('.')));
        var dimManager = marksManager.ensureDimension(dim);
        dimManager.setLoaded(false);
        try {
            var nbt = NbtIo.read(f.toFile());
            var list = nbt.getList("pos", NbtElement.COMPOUND_TYPE);
            list.forEach(e ->  {
                var infoTag = (NbtCompound) e;
                int x = infoTag.getInt("x");
                int z = infoTag.getInt("z");
                int c = infoTag.getInt("c");
                dimManager.setMark(x, z, c);
            });
        } catch (Exception e) {
            LogManager.getLogger().error(e);
        }
        dimManager.setLoaded(true);
    }

    private Path getFilePath(ServerDimensionMarksManager manager) {
        return marksFolder.resolve(manager.dim.getPath() + ".dat");
    }

    public static MarksIOManager build(ServerMarksManager manager, MinecraftServer server) {
        Path marksFolder = server.getSavePath(WorldSavePath.ROOT).resolve("data")
                .resolve(Mod.MOD_ID).resolve("marks");
        return new MarksIOManager(manager, marksFolder);
    }
}
