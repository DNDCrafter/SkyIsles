package com.thelastflames.skyisles;

import com.thelastflames.skyisles.Registry.SkyBlocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class CreativeTabs {
	public static final ItemGroup BUILDING_BLOCKS = new ItemGroup("skyisles_blocks") {
		@Override
		public ItemStack createIcon() {
			return new ItemStack(SkyBlocks.PILLAR_BLOCK.getObject2().get());
		}
	};
}
