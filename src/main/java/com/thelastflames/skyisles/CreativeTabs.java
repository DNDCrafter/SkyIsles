package com.thelastflames.skyisles;

import com.thelastflames.skyisles.registry.SkyBlocks;
import com.thelastflames.skyisles.registry.SkyItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class CreativeTabs {
	public static final ItemGroup BUILDING_BLOCKS = new ItemGroup("skyisles_decor") {
		@Override
		public ItemStack createIcon() {
			return new ItemStack(SkyBlocks.PILLAR_BLOCK.getObject2().get());
		}
	};
	
	public static final ItemGroup TOOLS = new ItemGroup("skyisles_tools") {
		@Override
		public ItemStack createIcon() {
			return new ItemStack(SkyItems.ARTIFACTS.get(SkyItems.ARTIFACTS.size() - 1).get());
		}
	};
}
