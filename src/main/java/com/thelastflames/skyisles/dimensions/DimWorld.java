//package com.thelastflames.skyisles.dimensions;
//
//import com.google.common.collect.ImmutableSet;
//import com.thelastflames.skyisles.biomes.BiomeBase;
//import com.thelastflames.skyisles.chunk_generators.SecondTestGenerator;
//import com.thelastflames.skyisles.registry.SkyBiomes;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.Vector3f;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.util.ResourceLocation;
//import net.minecraft.util.SharedSeedRandom;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.ChunkPos;
//import net.minecraft.util.math.MathHelper;
//import net.minecraft.util.math.Vec3d;
//import net.minecraft.world.World;
//import net.minecraft.world.biome.Biome;
//import net.minecraft.world.biome.provider.BiomeProvider;
//import net.minecraft.world.chunk.Chunk;
//import net.minecraft.world.dimension.Dimension;
//import net.minecraft.world.dimension.DimensionType;
//import net.minecraft.world.gen.ChunkGenerator;
//import net.minecraft.world.gen.EndGenerationSettings;
//import net.minecraft.world.gen.PerlinNoiseGenerator;
//import net.minecraftforge.client.IRenderHandler;
//
//import javax.annotation.Nonnull;
//import javax.annotation.Nullable;
//import java.util.Objects;
//import java.util.Set;
//
//import static com.thelastflames.skyisles.SkyIsles.ModID;
//
//public class DimWorld extends Dimension {
//	public DimWorld(World worldIn, DimensionType typeIn) {
//		super(worldIn, typeIn, 0);
//	}
//
//	private final Set<Biome> midLandsBiomes = ImmutableSet.of(
//			SkyBiomes.PLAINS.get(),
//			SkyBiomes.FOGGY_PLAINS.get()
//	);
//	private final Set<Biome> highLandsBiomes = ImmutableSet.of(
//			SkyBiomes.MOUNTAINS.get(),
//			SkyBiomes.MOUNTAINS_ROCKY.get()
//	);
//	private final Set<Biome> lowLandsBiomes = ImmutableSet.of(
//			SkyBiomes.PLAINS.get()
//	);
//
//	private ChunkGenerator<?> generator = null;
//
//	@Nonnull
//	@Override
//	public ChunkGenerator<?> createChunkGenerator() {
//		if (generator == null) {
//			generator = new SecondTestGenerator(this.world, new BiomeProvider(midLandsBiomes) {
//				private final PerlinNoiseGenerator generator2 = new PerlinNoiseGenerator(new SharedSeedRandom(world.getSeed()), 1, 4);
//				private final PerlinNoiseGenerator generator1 = new PerlinNoiseGenerator(new SharedSeedRandom(world.getSeed() * 2), 2, 3);
//
//				@Nonnull
//				@Override
//				public Biome getNoiseBiome(int x, int y, int z) {
//					double value = generator2.noiseAt(x / 32f, z / 32f, true) + generator1.noiseAt(x / 32f, z / 32f, true);
//
////					int height=((SecondTestGenerator)generator).getGenerationHeight(x,z);
////					int depth=((SecondTestGenerator)generator).getGenerationDepth(x,z);
////					if (height>depth) {
////						return SkyBiomes.VOID.get();
////					} else if (height>=100) {
////						int number=(int)(value*highLandsBiomes.size());
////						number=Math.max(0,Math.min(highLandsBiomes.size(),Math.abs(number)));
////						return (Biome)(highLandsBiomes.toArray()[number]);
////					} else if (height<=64) {
////						int number=(int)(value*lowLandsBiomes.size());
////						number=Math.max(0,Math.min(lowLandsBiomes.size(),Math.abs(number)));
////						return (Biome)(lowLandsBiomes.toArray()[number]);
////					} else {
//					int number = (int) (value * midLandsBiomes.size());
//					number = Math.max(0, Math.min(midLandsBiomes.size() - 1, Math.abs(number)));
//					return (Biome) (midLandsBiomes.toArray()[number]);
////					}
////					return SkyBiomes.PLAINS.get();
//				}
//			}, new EndGenerationSettings());
//		}
//		return generator;
//	}
//
//	@Nullable
//	@Override
//	public BlockPos findSpawn(@Nonnull ChunkPos chunkPosIn, boolean checkValid) {
//		return null;
//	}
//
//	@Override
//	public void getLightmapColors(float partialTicks, float sunBrightness, float skyLight, float blockLight, Vector3f colors) {
//		float value = Math.max(colors.getX(), skyLight * sunBrightness * 0.25f);
//		colors.set(value, value, value);
//	}
//
//	@Override
//	public float getLightBrightness(int p_227174_1_) {
//		return super.getLightBrightness(p_227174_1_);
//	}
//
//	@Override
//	public double getVoidFogYFactor() {
//		return 0;
//	}
//
//	@Nonnull
//	@Override
//	public DimensionType getType() {
//		return Objects.requireNonNull(DimensionType.byName(new ResourceLocation(ModID, "testdimension")));
//	}
//
//	@Nullable
//	@Override
//	public IRenderHandler getCloudRenderer() {
//		return super.getCloudRenderer();
//	}
//
//	@Nullable
//	@Override
//	public BlockPos findSpawn(int posX, int posZ, boolean checkValid) {
//		return null;
//	}
//
//	@Override
//	public float calculateCelestialAngle(long worldTime, float partialTicks) {
//		double d0 = MathHelper.frac((double) worldTime / 24000.0D - 0.25D);
//		double d1 = 0.5D - Math.cos(d0 * Math.PI) / 2.0D;
//		return (float) (d0 * 2.0D + d1) / 3.0F;
//	}
//
//	@Override
//	public boolean isSurfaceWorld() {
//		return true;
//	}
//
//	@Nonnull
//	@Override
//	public Vec3d getFogColor(float celestialAngle, float partialTicks) {
//		int x = Minecraft.getInstance().player.getPosition().getX();
//		int y = Minecraft.getInstance().player.getPosition().getY();
//		int z = Minecraft.getInstance().player.getPosition().getZ();
//		if (this.getWorld().getBiome(new BlockPos(x, y, z)) instanceof BiomeBase) {
//			Vec3d color = ((BiomeBase) this.getWorld().getBiome(new BlockPos(x, y, z))).getFogColor();
//			if (color != null) {
//				return color;
//			}
//		}
//		float f = MathHelper.cos(celestialAngle * ((float) Math.PI * 2F)) * 2.0F + 0.5F;
//		f = MathHelper.clamp(f, 0.0F, 1.0F);
//		float f1 = 0.7529412F;
//		float f2 = 0.84705883F;
//		float f3 = 1.0F;
//		f1 = f1 * (f * 0.94F + 0.06F);
//		f2 = f2 * (f * 0.94F + 0.06F);
//		f3 = f3 * (f * 0.91F + 0.09F);
//		return new Vec3d(f1, f2, f3);
//	}
//
//	@Override
//	public boolean canRespawnHere() {
//		return false;
//	}
//
//	//Apparently this is passed x,y isntead of x,z for some stupid reason.
//	@Override
//	public boolean doesXZShowFog(int x, int z) {
//		try {
//			z = Minecraft.getInstance().player.getPosition().getZ();
//			Biome b = Minecraft.getInstance().player.world.getBiome(new BlockPos(x, 0, z));
//////			Biome b=this.getWorld().getWorld().getBiome(new BlockPos(x,0,z));
//////			BiomeProvider provider=this.createChunkGenerator().getBiomeProvider();
//////			return ((BiomeBase)provider.getNoiseBiome(x,0,z)).showsFog(x,z);
////			System.out.println(b);
//			return ((BiomeBase) b).showsFog(x, z);
////			return false;
//		} catch (Exception err) {
//			return false;
//		}
//	}
//
//	@Override
//	public float getCloudHeight() {
//		return -23;
//	}
//
//	@Nullable
//	@Override
//	public float[] calcSunriseSunsetColors(float celestialAngle, float partialTicks) {
//		return super.calcSunriseSunsetColors(celestialAngle, partialTicks);
//	}
//
//	@Override
//	public boolean canDoLightning(Chunk chunk) {
//		return super.canDoLightning(chunk);
//	}
//
//	@Override
//	public boolean canDoRainSnowIce(Chunk chunk) {
//		return false;
//	}
//
//	@Override
//	public boolean canMineBlock(PlayerEntity player, BlockPos pos) {
//		return super.canMineBlock(player, pos);
//	}
//
//	@Override
//	public boolean shouldMapSpin(String entity, double x, double z, double rotation) {
//		return super.shouldMapSpin(entity, x, z, rotation);
//	}
//}
