package com.thelastflames.skyisles.Dimensions;

import com.google.common.collect.ImmutableSet;
import com.thelastflames.skyisles.Biomes.BiomeBase;
import com.thelastflames.skyisles.ChunkGenerators.TestGenerator;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.EndGenerationSettings;
import net.minecraftforge.client.IRenderHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Set;

import static com.thelastflames.skyisles.SkyIsles.ModID;

public class DimWorld extends Dimension {
	public DimWorld(World worldIn, DimensionType typeIn) {
		super(worldIn, typeIn, 1);
	}
	
	@Nonnull
	@Override
	public ChunkGenerator<?> createChunkGenerator() {
		Set<Biome> biomes= ImmutableSet.of(
				Biomes.THE_VOID
		);
		return new TestGenerator(this.world, new BiomeProvider(biomes) {
			@Nonnull
			@Override
			public Biome getNoiseBiome(int x, int y, int z) {
				return (Biome)biomes.toArray()[0];
			}
		}, new EndGenerationSettings());
	}
	
	@Nullable
	@Override
	public BlockPos findSpawn(@Nonnull ChunkPos chunkPosIn, boolean checkValid) {
		return null;
	}
	
	@Override
	public void getLightmapColors(float partialTicks, float sunBrightness, float skyLight, float blockLight, Vector3f colors) {
//		colors=new Vector3f(sunBrightness,skyLight,blockLight);
	}
	
	@Override
	public float getLightBrightness(int p_227174_1_) {
		return super.getLightBrightness(p_227174_1_);
	}
	
	@Override
	public double getVoidFogYFactor() {
		return 20;
	}
	
	@Nonnull
	@Override
	public DimensionType getType() {
		return Objects.requireNonNull(DimensionType.byName(new ResourceLocation(ModID, "testdimension")));
	}
	
	@Nullable
	@Override
	public IRenderHandler getCloudRenderer() {
		return super.getCloudRenderer();
	}
	
	@Nullable
	@Override
	public BlockPos findSpawn(int posX, int posZ, boolean checkValid) {
		return null;
	}
	
	@Override
	public float calculateCelestialAngle(long worldTime, float partialTicks) {
		double d0 = MathHelper.frac((double)worldTime / 24000.0D - 0.25D);
		double d1 = 0.5D - Math.cos(d0 * Math.PI) / 2.0D;
		return (float)(d0 * 2.0D + d1) / 3.0F;
	}
	
	@Override
	public boolean isSurfaceWorld() {
		return false;
	}
	
	@Nonnull
	@Override
	public Vec3d getFogColor(float celestialAngle, float partialTicks) {
		float f = MathHelper.cos(celestialAngle * ((float)Math.PI * 2F)) * 2.0F + 0.5F;
		f = MathHelper.clamp(f, 0.0F, 1.0F);
		float f1 = 0.7529412F;
		float f2 = 0.84705883F;
		float f3 = 1.0F;
		f1 = f1 * (f * 0.94F + 0.06F);
		f2 = f2 * (f * 0.94F + 0.06F);
		f3 = f3 * (f * 0.91F + 0.09F);
		return new Vec3d(f1, f2, f3);
	}
	
	@Override
	public boolean canRespawnHere() {
		return false;
	}
	
	@Override
	public boolean doesXZShowFog(int x, int z) {
		try {
			return ((BiomeBase)this.createChunkGenerator().getBiomeProvider().getNoiseBiome(x,0,z)).showsFog(x,z);
		} catch (Exception err) {
			return false;
		}
	}
	
	@Nullable
	@Override
	public float[] calcSunriseSunsetColors(float celestialAngle, float partialTicks) {
		return super.calcSunriseSunsetColors(celestialAngle, partialTicks);
	}
	
	@Override
	public boolean canDoLightning(Chunk chunk) {
		return super.canDoLightning(chunk);
	}
	
	@Override
	public boolean canDoRainSnowIce(Chunk chunk) {
		return false;
	}
	
	@Override
	public boolean canMineBlock(PlayerEntity player, BlockPos pos) {
		return super.canMineBlock(player,pos);
	}
	
	@Override
	public boolean shouldMapSpin(String entity, double x, double z, double rotation) {
		return super.shouldMapSpin(entity,x,z,rotation);
	}
}
