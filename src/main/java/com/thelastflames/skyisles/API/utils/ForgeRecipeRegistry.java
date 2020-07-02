package com.thelastflames.skyisles.API.utils;

import com.thelastflames.skyisles.utils.StringyHashMap;
import net.minecraft.util.ResourceLocation;

import java.util.function.BiConsumer;

public class ForgeRecipeRegistry {
	protected static final StringyHashMap<ResourceLocation, ToolForgeRecipe> recipeHashMap=new StringyHashMap<>();
	
	public static void registerRecipe(ResourceLocation location, ToolForgeRecipe recipe) {
		recipeHashMap.add(location,recipe);
	}
	
	public static void forEach(BiConsumer<ResourceLocation,ToolForgeRecipe> consumer) {
		System.out.println("h");
		System.out.println(recipeHashMap.keys.size());
		recipeHashMap.forEach(consumer);
	}
}
