package com.thelastflames.skyisles.registry;

import com.thelastflames.skyisles.SkyIsles;
import com.thelastflames.skyisles.client.Item.PickaxeISTER;
import net.minecraft.item.Item;
import net.minecraft.item.ItemTier;
import net.minecraft.item.PickaxeItem;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SkyItems {
	public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, SkyIsles.ModID);
	
	public static final RegistryObject<Item> TEXTURE_LOADER=ITEMS.register("texture_loader",()->new Item(new Item.Properties()));
	public static final RegistryObject<Item> METAL_PICKAXE=ITEMS.register("metal_tool",()->new PickaxeItem(ItemTier.DIAMOND,1,1, new Item.Properties().setISTER(()->()->new PickaxeISTER())));
}
