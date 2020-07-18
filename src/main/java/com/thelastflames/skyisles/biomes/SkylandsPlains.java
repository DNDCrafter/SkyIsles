package com.thelastflames.skyisles.biomes;

import com.thelastflames.skyisles.island_structures.SimpleTree;
import com.thelastflames.skyisles.island_structures.Structure;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;

import java.util.ArrayList;

public class SkylandsPlains extends BiomeBase {
	public SkylandsPlains() {
		super((new Biome.Builder()).surfaceBuilder(SurfaceBuilder.NOPE, SurfaceBuilder.STONE_STONE_GRAVEL_CONFIG).precipitation(Biome.RainType.NONE).category(Biome.Category.NONE).depth(0.1F).scale(0.2F).temperature(0.5F).downfall(0.5F).waterColor(4159204).waterFogColor(329011).parent((String)null));
		structures.add(new SimpleTree());
	}
	
	@Override
	public boolean showsFog(int x, int z) {
		return false;
	}
	
	private final ArrayList<Structure> structures=new ArrayList<>();
	
	@Override
	public ArrayList<Structure> getStructures() {
		return structures;
	}
}
