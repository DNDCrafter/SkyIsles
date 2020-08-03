package com.thelastflames.skyisles.biomes;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import tfc.dynamic_rendering.Color;

public class SkylandsMountains extends BiomeBase {
	public SkylandsMountains() {
		super((new Biome.Builder()).surfaceBuilder(SurfaceBuilder.NOPE, SurfaceBuilder.STONE_STONE_GRAVEL_CONFIG).precipitation(Biome.RainType.NONE).category(Biome.Category.NONE).depth(0.1F).scale(0.2F).temperature(0.5F).downfall(0.5F).waterColor(4159204).waterFogColor(329011).parent((String) null));
	}
	
	@Override
	public int getGrassColor(double posX, double posZ) {
		return new Color(32, 129, 32).getRGB();
	}
	
	@Override
	public int getFoliageColor() {
		return this.getGrassColor(0, 0);
	}
}
