package com.thelastflames.skyisles.registry;

import com.thelastflames.skyisles.SkyIsles;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SkyItems {
	public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, SkyIsles.ModID);
	
	public static final RegistryObject<Item> TEXTURE_LOADER=ITEMS.register("texture_loader",()->new Item(new Item.Properties()));
}
