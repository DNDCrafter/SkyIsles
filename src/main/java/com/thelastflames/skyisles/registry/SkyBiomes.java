package com.thelastflames.skyisles.registry;

import com.thelastflames.skyisles.SkyIsles;
import com.thelastflames.skyisles.biomes.*;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SkyBiomes {
	public static final DeferredRegister<Biome> BIOMES = new DeferredRegister<>(ForgeRegistries.BIOMES, SkyIsles.ModID);
	
	public static final RegistryObject<Biome> PLAINS = BIOMES.register("sky_plains", SkylandsPlains::new);
	public static final RegistryObject<Biome> FOGGY_PLAINS = BIOMES.register("foggy_sky_plains", SkylandsFoggyPlains::new);
	public static final RegistryObject<Biome> MOUNTAINS = BIOMES.register("sky_highlands", SkylandsMountains::new);
	public static final RegistryObject<Biome> MOUNTAINS_ROCKY = BIOMES.register("sky_highlands_rocky", SkylandsMountainsRocky::new);
	public static final RegistryObject<Biome> VOID = BIOMES.register("sky_void", SkyIslesVoid::new);
	
}
