package com.thelastflames.skyisles.registry;

import com.thelastflames.skyisles.API.SkyIslesAPI;
import com.thelastflames.skyisles.API.events.blocks.GetChestBlocksEvent;
import com.thelastflames.skyisles.SkyIsles;
import com.thelastflames.skyisles.blocks.ChestBlock;
import com.thelastflames.skyisles.block_entity.*;
import com.thelastflames.skyisles.blocks.DrawerBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SkyTileEntities {
    public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, SkyIsles.ModID);


	public static final RegistryObject<BlockEntityType<LampBE>> LAMP_BE = TILE_ENTITIES.register("lamp", () -> BlockEntityType.Builder.of(LampBE::new,
			SkyBlocks.LAMP_BLOCK.getObject1().get()
	).build(null));

    public static final RegistryObject<BlockEntityType<ContainerBE>> DRAWER_TE = TILE_ENTITIES.register("drawer_te", () -> BlockEntityType.Builder.of(DrawerBlock::createBE,
            SkyBlocks.DRAWER_BLOCK.getObject1().get()
    ).build(null));

//	public static final RegistryObject<TileEntityType<SkyboxTileEntity>> SKYBOX = TILE_ENTITIES.register("skybox_tile_entity", () -> TileEntityType.Builder.create(SkyboxTileEntity::new,
//			getSkyboxBlocks()
//	).build(null));
//
//	public static final RegistryObject<TileEntityType<ItemObserverTE>> ITEM_OBSERVER = TILE_ENTITIES.register("item_observer", () -> TileEntityType.Builder.create(ItemObserverTE::new,
//			SkyBlocks.ITEM_OBSERVER.getObject1().get()
//	).build(null));

    public static final RegistryObject<BlockEntityType<ChestTE>> CHEST_TE = TILE_ENTITIES.register("chest", () -> BlockEntityType.Builder.of(ChestTE::new,
            getChestBlocks()
    ).build(null));

//	public static SkyboxBlock[] getSkyboxBlocks() {
//		GetSkyboxBlocksEvent event = new GetSkyboxBlocksEvent();
//		SkyIslesAPI.INSTANCE.post(event);
//		SkyboxBlock[] blocks = new SkyboxBlock[event.blocks.size()];
//		for (int i=0;i<event.blocks.size();i++) {
//			blocks[i] = event.blocks.get(i);
//		}
//		return blocks;
//	}

    public static ChestBlock[] getChestBlocks() {
        GetChestBlocksEvent event = new GetChestBlocksEvent();
        SkyIslesAPI.INSTANCE.post(event);
        ChestBlock[] blocks = new ChestBlock[event.blocks.size()];
        for (int i = 0; i < event.blocks.size(); i++) {
            blocks[i] = event.blocks.get(i);
        }
        return blocks;
    }
}
