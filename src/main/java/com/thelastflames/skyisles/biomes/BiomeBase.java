//package com.thelastflames.skyisles.biomes;
//
//import com.thelastflames.skyisles.chunk_generators.SurfaceBuilder;
//import com.thelastflames.skyisles.chunk_generators.surface_builders.DefaultSurfaceBuilder;
//import com.thelastflames.skyisles.island_structures.Structure;
//import net.minecraft.block.BlockState;
//import net.minecraft.block.Blocks;
//import net.minecraft.util.math.Vec3d;
//import net.minecraft.world.biome.Biome;
//
//import java.util.ArrayList;
//
//public class BiomeBase extends Biome {
//	private static final SurfaceBuilder defaultSurfaceBuilder = new DefaultSurfaceBuilder();
//
//	public BiomeBase(Builder biomeBuilder) {
//		super(biomeBuilder);
//	}
//
//	public boolean showsFog(int x, int z) {
//		return true;
//	}
//
//	public SurfaceBuilder getBiomeSurfaceBuilder() {
//		return defaultSurfaceBuilder;
//	}
//
//	public static SurfaceBuilder getDefaultSurfaceBuilder() {
//		return defaultSurfaceBuilder;
//	}
//
//	public BlockState getTopBlock() {
//		return Blocks.GRASS_BLOCK.getDefaultState();
//	}
//
//	public BlockState getMiddleBlock() {
//		return Blocks.STONE.getDefaultState();
//	}
//
//	public BlockState getBottomBlock() {
//		return Blocks.WHITE_STAINED_GLASS.getDefaultState();
//	}
//
//	public ArrayList<Structure> getStructures() {
//		return new ArrayList<>();
//	}
//
//	public Vec3d getFogColor() {
//		return null;
//	}
//}
