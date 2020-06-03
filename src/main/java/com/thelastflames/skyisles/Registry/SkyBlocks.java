package com.thelastflames.skyisles.Registry;

import com.thelastflames.skyisles.Blocks.PillarBlock;
import com.thelastflames.skyisles.CreativeTabs;
import com.thelastflames.skyisles.ITSERLookup;
import com.thelastflames.skyisles.Items.DefaultNBTBlockItem;
import com.thelastflames.skyisles.SkyIsles;
import com.thelastflames.skyisles.Utils.BiRegistry;
import com.thelastflames.skyisles.Utils.NBTUtil;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Rarity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public class SkyBlocks {
	public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, SkyIsles.ModID);
	
	public static final BiRegistry<RegistryObject<Block>, RegistryObject<Item>> PILLAR_BLOCK = registerBlockWithItem("pillar", new PillarBlock(), 64, CreativeTabs.BUILDING_BLOCKS,null, NBTUtil.createNBT(new NBTUtil.NBTObjectHolder<CompoundNBT>("BlockEntityTag",new NBTUtil.NBTObjectHolder<String>("materials","minecraft:iron_ingot;minecraft:gold_ingot").Package())));
	
	public static BiRegistry<RegistryObject<Block>, RegistryObject<Item>> registerBlockWithItem(String name, Block block, int maxStack, @Nullable ItemGroup group, @Nullable Rarity rarity, @Nullable CompoundNBT defaultNBT) {
		Item.Properties properties = new Item.Properties();
		if (group!=null) properties.group(group);
		if (rarity!=null) properties.rarity(rarity);
		if (ITSERLookup.contains(name)) properties.setISTER(()->()->ITSERLookup.get(name));
		properties.maxStackSize(maxStack);
		return new BiRegistry<>(
				BLOCKS.register(name,()->block),
				SkyItems.ITEMS.register(name,()->(defaultNBT!=null?new DefaultNBTBlockItem(block,properties,defaultNBT):new BlockItem(block, properties)))
		);
	}
}
