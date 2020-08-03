package com.thelastflames.skyisles.API.events.recipe;

import com.thelastflames.skyisles.API.utils.ForgeRecipeRegistry;
import com.thelastflames.skyisles.API.utils.ToolForgeRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.Event;

import java.util.HashMap;

public class ForgeRecipesEvent extends Event {
	private final HashMap<ResourceLocation, ToolForgeRecipe> recipeHashMap = new HashMap<>();
	
	public ForgeRecipesEvent() {
	}
	
	public void register(ResourceLocation location, ToolForgeRecipe recipe) {
		recipeHashMap.put(location, recipe);
	}
	
	public void finish() {
		recipeHashMap.forEach(ForgeRecipeRegistry::registerRecipe);
	}
	
	@Override
	public boolean isCancelable() {
		return false;
	}
}
