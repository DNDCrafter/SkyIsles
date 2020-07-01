package com.thelastflames.skyisles.biomes;

import com.thelastflames.skyisles.chunk_generators.SurfaceBuilder;
import com.thelastflames.skyisles.chunk_generators.surface_builders.DefaultSurfaceBuilder;
import net.minecraft.world.biome.Biome;

public class BiomeBase extends Biome {
	private static final SurfaceBuilder defaultSurfaceBuilder=new DefaultSurfaceBuilder();
	
	public BiomeBase(Builder biomeBuilder) {
		super(biomeBuilder);
	}
	
	public boolean showsFog(int x,int z) {
		return true;
	}
	
	public SurfaceBuilder getBiomeSurfaceBuilder() {
		return defaultSurfaceBuilder;
	}
	
	public static SurfaceBuilder getDefaultSurfaceBuilder() {
		return defaultSurfaceBuilder;
	}
}
