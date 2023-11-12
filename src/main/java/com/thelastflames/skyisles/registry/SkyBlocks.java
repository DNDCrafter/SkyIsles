package com.thelastflames.skyisles.registry;

import com.thelastflames.skyisles.CreativeTabs;
import com.thelastflames.skyisles.SkyIsles;
import com.thelastflames.skyisles.blocks.*;
import com.thelastflames.skyisles.items.DefaultNBTBlockItem;
import com.thelastflames.skyisles.utils.BiRegistry;
import net.minecraft.client.resources.PaintingTextureManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class SkyBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SkyIsles.ModID);

//	public static final BiRegistry<RegistryObject<Block>, RegistryObject<Item>> PILLAR_BLOCK = registerBlockWithItem("pillar", new PillarBlock(), 64, CreativeTabs.BUILDING_BLOCKS, null, NBTUtil.createNBT(new NBTUtil.NBTObjectHolder<>("BlockEntityTag", new NBTUtil.NBTObjectHolder<>("materials", "minecraft:iron_ingot;minecraft:gold_ingot").Package())));
//	public static final BiRegistry<RegistryObject<Block>, RegistryObject<Item>> LAMP_BLOCK = registerBlockWithItem("lamp_block", new LampBlock(), 64, CreativeTabs.BUILDING_BLOCKS, null, NBTUtil.createNBT(new NBTUtil.NBTObjectHolder<>("BlockEntityTag", new NBTUtil.NBTObjectHolder<>("materials", "minecraft:glowstone;minecraft:black_concrete").Package())));
//	public static final BiRegistry<RegistryObject<Block>, RegistryObject<Item>> DRAWER_BLOCK = registerBlockWithItem("drawer_block", new DrawerBlock(), 64, CreativeTabs.BUILDING_BLOCKS, null, NBTUtil.createNBT(new NBTUtil.NBTObjectHolder<>("BlockEntityTag", new NBTUtil.NBTObjectHolder<>("materials", "minecraft:spruce_wood;minecraft:iron_block").Package())));
//	public static final BiRegistry<RegistryObject<Block>, RegistryObject<Item>> SKYBOX_BLOCK = registerBlockWithItem("skybox_test", new SkyIslesSkyboxBlock(Block.Properties.from(Blocks.COBBLESTONE),
//			new ResourceLocation("skyisles:textures/block/skybox.png"),
//			new ResourceLocation("skyisles:textures/block/skybox_pass.png"),
//			new ResourceLocation("skyisles:textures/block/skybox_pass_stars.png")), 64, CreativeTabs.BUILDING_BLOCKS, null, null);
//	public static final BiRegistry<RegistryObject<Block>, RegistryObject<Item>> SKYBOX_BLOCK_PURPLE = registerBlockWithItem("skybox_test_purple", new SkyIslesSkyboxBlock(Block.Properties.from(Blocks.COBBLESTONE),
//			new ResourceLocation("skyisles:textures/block/skybox_purple.png"),
//			new ResourceLocation("skyisles:textures/block/skybox_pass_purple.png"),
//			new ResourceLocation("textures/entity/end_portal.png")), 64, CreativeTabs.BUILDING_BLOCKS, null, null);
//	public static final BiRegistry<RegistryObject<Block>, RegistryObject<Item>> FORGE_BLOCK = registerBlockWithItem("forge_block", new ForgeBlock(), 64, CreativeTabs.BUILDING_BLOCKS, null, null);
//	public static final BiRegistry<RegistryObject<Block>, RegistryObject<Item>> ITEM_OBSERVER = registerBlockWithItem("item_observer", new ItemObserver(Block.Properties.from(Blocks.OBSERVER)), 64, CreativeTabs.BUILDING_BLOCKS, null, null);
//	public static final BiRegistry<RegistryObject<Block>, RegistryObject<Item>> TREE_SPAWNER_BLOCK = registerBlockWithItem("tree_spawner", new StructureSpawnerBlock(new SimpleTree()), 64, CreativeTabs.BUILDING_BLOCKS, null, null);

    public static final RegistryObject<Block> CHEST_BLOCK = registerBlock("chest", () -> new ChestBlock(SkyTileEntities.CHEST_TE::get));

    public static RegistryObject<Block> registerBlock(String name, Supplier<Block> block) {
        return BLOCKS.register(name, block);
    }

    public static BiRegistry<RegistryObject<Block>, RegistryObject<Item>> registerBlockWithItem(String name, Supplier<Block> block, int maxStack, @Nullable CreativeModeTab group, @Nullable Rarity rarity, @Nullable CompoundTag defaultNBT) {
        Item.Properties properties = new Item.Properties();
//		if (group != null) properties.group(group);
        if (rarity != null) properties.rarity(rarity);
//		if (ITSERLookup.contains(name)) properties.setISTER(() -> () -> ITSERLookup.get(name));
        properties.stacksTo(maxStack);
        RegistryObject<Block> registryObject;
        return new BiRegistry<>(
                registryObject = BLOCKS.register(name, block),
                SkyItems.ITEMS.register(name, () -> (defaultNBT != null ? new DefaultNBTBlockItem(registryObject.get(), properties, defaultNBT) : new BlockItem(registryObject.get(), properties)))
        );
    }
}
