package com.thelastflames.skyisles.registry;

import com.thelastflames.skyisles.CreativeTabs;
import com.thelastflames.skyisles.ITSERLookup;
import com.thelastflames.skyisles.SkyIsles;
import com.thelastflames.skyisles.blocks.*;
import com.thelastflames.skyisles.island_structures.SimpleTree;
import com.thelastflames.skyisles.items.DefaultNBTBlockItem;
import com.thelastflames.skyisles.utils.BiRegistry;
import com.thelastflames.skyisles.utils.NBTUtil;
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
	
	public static final BiRegistry<RegistryObject<Block>, RegistryObject<Item>> PILLAR_BLOCK = registerBlockWithItem("pillar", new PillarBlock(), 64, CreativeTabs.BUILDING_BLOCKS,null, NBTUtil.createNBT(new NBTUtil.NBTObjectHolder<>("BlockEntityTag",new NBTUtil.NBTObjectHolder<>("materials","minecraft:iron_ingot;minecraft:gold_ingot").Package())));
	public static final BiRegistry<RegistryObject<Block>, RegistryObject<Item>> LAMP_BLOCK = registerBlockWithItem("lamp_block", new LampBlock(), 64, CreativeTabs.BUILDING_BLOCKS,null, NBTUtil.createNBT(new NBTUtil.NBTObjectHolder<>("BlockEntityTag",new NBTUtil.NBTObjectHolder<>("materials","minecraft:glowstone;minecraft:black_concrete").Package())));
	public static final BiRegistry<RegistryObject<Block>, RegistryObject<Item>> DRAWER_BLOCK = registerBlockWithItem("drawer_block", new DrawerBlock(), 64, CreativeTabs.BUILDING_BLOCKS,null, NBTUtil.createNBT(new NBTUtil.NBTObjectHolder<>("BlockEntityTag", new NBTUtil.NBTObjectHolder<>("materials", "minecraft:spruce_wood;minecraft:iron_block").Package())));
	public static final BiRegistry<RegistryObject<Block>, RegistryObject<Item>> FORGE_BLOCK = registerBlockWithItem("forge_block", new ForgeBlock(), 64, CreativeTabs.BUILDING_BLOCKS,null, null);
	public static final BiRegistry<RegistryObject<Block>, RegistryObject<Item>> TREE_SPAWNER_BLOCK = registerBlockWithItem("tree_spawner", new StructureSpawnerBlock(new SimpleTree()), 64, CreativeTabs.BUILDING_BLOCKS,null, null);
	
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
