package structure;

import com.thelastflames.skyisles.chunk_generators.si.Point;
import com.thelastflames.skyisles.chunk_generators.structure.SIStructureStart;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;
import structure.test.StructurePotential;

import java.util.List;

public abstract class PlacementRules {
    public abstract List<StructurePotential> place(ChunkGenerator generator, PositionalRandomFactory factory, ChunkPos minCoord, ChunkPos maxCoord, ChunkPos active);

    public abstract List<SIStructureStart> prepare(ChunkGenerator generator, PositionalRandomFactory factory, List<Point> points);
}
