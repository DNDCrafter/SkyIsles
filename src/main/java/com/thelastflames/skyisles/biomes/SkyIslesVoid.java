package com.thelastflames.skyisles.biomes;

import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;

public class SkyIslesVoid extends BiomeBase {
	public SkyIslesVoid() {
		super((new Biome.Builder()).surfaceBuilder(SurfaceBuilder.NOPE, SurfaceBuilder.STONE_STONE_GRAVEL_CONFIG).precipitation(Biome.RainType.NONE).category(Biome.Category.NONE).depth(0.1F).scale(0.2F).temperature(0.5F).downfall(0.5F).waterColor(4159204).waterFogColor(329011).parent((String) null));
	}
	
	@Override
	public boolean showsFog(int x, int z) {
		return true;
	}
	
	@Override
	public Vec3d getFogColor() {
		return new Vec3d(0, 0, 0);
	}
}
