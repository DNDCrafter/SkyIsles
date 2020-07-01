package com.thelastflames.skyisles.other;

import net.minecraft.stats.IStatFormatter;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

public class Stats {
	public static final ResourceLocation OPEN_DRAWER = registerCustom("open_chest", IStatFormatter.DEFAULT);
	
	private static ResourceLocation registerCustom(String key, IStatFormatter formatter) {
		ResourceLocation resourcelocation = new ResourceLocation(key);
		Registry.register(Registry.CUSTOM_STAT, key, resourcelocation);
		net.minecraft.stats.Stats.CUSTOM.get(resourcelocation, formatter);
		return resourcelocation;
	}
}
