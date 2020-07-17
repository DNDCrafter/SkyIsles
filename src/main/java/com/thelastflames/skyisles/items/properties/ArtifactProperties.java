package com.thelastflames.skyisles.items.properties;

import com.thelastflames.skyisles.CreativeTabs;
import net.minecraft.item.Item;

public class ArtifactProperties extends Item.Properties {
	public ArtifactProperties() {
		this.group(CreativeTabs.TOOLS).maxStackSize(1);
	}
}
