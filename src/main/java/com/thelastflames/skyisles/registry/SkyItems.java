package com.thelastflames.skyisles.registry;

import com.google.common.collect.ImmutableList;
import com.thelastflames.skyisles.SkyIsles;
import com.thelastflames.skyisles.blocks.ChestBlock;
import com.thelastflames.skyisles.items.DefaultNBTBlockItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;

public class SkyItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SkyIsles.ModID);

//    public static final RegistryObject<Item> TEXTURE_LOADER = ITEMS.register("texture_loader", () -> new Item(new Item.Properties()));
//    public static final RegistryObject<Item> SKYISLES_ICON = ITEMS.register("skyisles_icon", () -> new Item(new Item.Properties()));
//    public static final RegistryObject<Item> METAL_PICKAXE = ITEMS.register("metal_tool", () -> new PickaxeItem(ItemTier.DIAMOND, 1, 1, new Item.Properties().setISTER(() -> () -> new PickaxeISTER()).group(CreativeTabs.TOOLS)));

    public static final ImmutableList<RegistryObject<Item>> CHESTS = registerChests();

    private static ImmutableList<RegistryObject<Item>> registerChests() {
        ArrayList<RegistryObject<Item>> registryObjects = new ArrayList<>();
        for (ChestBlock.ChestType value : ChestBlock.ChestType.values()) {
            CompoundTag tg = new CompoundTag();
            tg.putString("material", value.name);

            Item.Properties properties = new Item.Properties();

            registryObjects.add(ITEMS.register(value.name + "_chest", () -> new DefaultNBTBlockItem(
                    SkyBlocks.CHEST_BLOCK.get(),
                    properties, tg
            )));
        }
        return ImmutableList.copyOf(registryObjects);
    }

//    public static final ArrayList<RegistryObject<Item>> ARTIFACTS = registerArtifacts();

//    private static ArrayList<RegistryObject<Item>> registerArtifacts() {
//        ArrayList<RegistryObject<Item>> items = new ArrayList<>();
//
//        items.add(ITEMS.register("test_artifact", () -> new ArtifactItem(Items.COAL, ArtifactMethods::useTest)));
//        return items;
//    }
}
