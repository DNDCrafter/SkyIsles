package com.thelastflames.skyisles.Registry;

import com.thelastflames.skyisles.SkyIsles;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SkyDimensions {
	public static final DeferredRegister<ModDimension> MOD_DIMENSIONS = new DeferredRegister<>(ForgeRegistries.MOD_DIMENSIONS, SkyIsles.ModID);
	
//	public static final RegistryObject<ModDimension> TESTDIM = MOD_DIMENSIONS.register("testdimension", testDim::new);
}
