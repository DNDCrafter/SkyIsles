package com.thelastflames.skyisles.registry;

import com.thelastflames.skyisles.API.SkyIslesAPI;
import com.thelastflames.skyisles.API.events.blocks.GetChestBlocksEvent;
import com.thelastflames.skyisles.SkyIsles;
import com.thelastflames.skyisles.blocks.ChestBlock;
import com.thelastflames.skyisles.tile_entity.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SkyTileEntities {
	public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, SkyIsles.ModID);


//	public static final RegistryObject<TileEntityType<MultiMaterialTE>> MULTIMATERIALTE = TILE_ENTITIES.register("multimaterial_block", () -> TileEntityType.Builder.create(MultiMaterialTE::new,
//			SkyBlocks.PILLAR_BLOCK.getObject1().get()
//	).build(null));
//	public static final RegistryObject<TileEntityType<LampTE>> LAMPTE = TILE_ENTITIES.register("lamp_block", () -> TileEntityType.Builder.create(LampTE::new,
//			SkyBlocks.LAMP_BLOCK.getObject1().get()
//	).build(null));
//	public static final RegistryObject<TileEntityType<MultiMaterialContainerTE>> DRAWER_TE = TILE_ENTITIES.register("drawer_te", () -> TileEntityType.Builder.create(MultiMaterialContainerTE::new,
//			SkyBlocks.DRAWER_BLOCK.getObject1().get()
//	).build(null));
//
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
		for (int i=0;i<event.blocks.size();i++) {
			blocks[i] = event.blocks.get(i);
		}
		return blocks;
	}
}
