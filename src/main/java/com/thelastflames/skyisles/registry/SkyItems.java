package com.thelastflames.skyisles.registry;

import com.thelastflames.skyisles.CreativeTabs;
import com.thelastflames.skyisles.SkyIsles;
import com.thelastflames.skyisles.client.item.PickaxeISTER;
import com.thelastflames.skyisles.items.ArtifactItem;
import com.thelastflames.skyisles.items.ArtifactMethods;
import net.minecraft.item.Item;
import net.minecraft.item.ItemTier;
import net.minecraft.item.Items;
import net.minecraft.item.PickaxeItem;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;

public class SkyItems {
	public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, SkyIsles.ModID);
	
	public static final RegistryObject<Item> TEXTURE_LOADER=ITEMS.register("texture_loader",()->new Item(new Item.Properties()));
	public static final RegistryObject<Item> SKYISLES_ICON =ITEMS.register("skyisles_icon",()->new Item(new Item.Properties()));
	public static final RegistryObject<Item> METAL_PICKAXE=ITEMS.register("metal_tool",()->new PickaxeItem(ItemTier.DIAMOND,1,1, new Item.Properties().setISTER(()->()->new PickaxeISTER()).group(CreativeTabs.TOOLS)));
	
	public static final ArrayList<RegistryObject<Item>> ARTIFACTS = registerArtifacts();
	
	private static ArrayList<RegistryObject<Item>> registerArtifacts() {
		ArrayList<RegistryObject<Item>> items=new ArrayList<>();
		
		items.add(ITEMS.register("test_artifact",()->new ArtifactItem(Items.COAL, ArtifactMethods::useTest)));
		return items;
	}
}
