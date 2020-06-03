package com.thelastflames.skyisles.Biomes;

import net.minecraft.world.biome.Biome;

public class BiomeBase extends Biome {
	public BiomeBase(Builder biomeBuilder) {
		super(biomeBuilder);
	}
	
	public boolean showsFog(int x,int z) {
		return true;
	}
}
