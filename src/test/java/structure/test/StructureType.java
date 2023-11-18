package structure.test;

import net.minecraft.world.level.levelgen.PositionalRandomFactory;

public abstract class StructureType {
    public double maxSize;

    public StructureType(double maxSize) {
        this.maxSize = maxSize;
    }

    public abstract boolean validate(PositionalRandomFactory factory, StructurePotential potential, int chunkX, int chunkZ);

    public abstract StructureType decay(PositionalRandomFactory factory);
}
