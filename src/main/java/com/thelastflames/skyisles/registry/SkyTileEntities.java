package com.thelastflames.skyisles.registry;

import com.thelastflames.skyisles.API.SkyIslesAPI;
import com.thelastflames.skyisles.API.events.blocks.GetChestBlocksEvent;
import com.thelastflames.skyisles.API.events.blocks.GetSkyboxBlocksEvent;
import com.thelastflames.skyisles.SkyIsles;
import com.thelastflames.skyisles.blocks.ChestBlock;
import com.thelastflames.skyisles.blocks.bases.SkyboxBlock;
import com.thelastflames.skyisles.tile_entity.*;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SkyTileEntities {
	public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, SkyIsles.ModID);
	
	public static final RegistryObject<TileEntityType<MultiMaterialTE>> MULTIMATERIALTE = TILE_ENTITIES.register("multimaterial_block", () -> TileEntityType.Builder.create(MultiMaterialTE::new,
			SkyBlocks.PILLAR_BLOCK.getObject1().get()
	).build(null));
	public static final RegistryObject<TileEntityType<LampTE>> LAMPTE = TILE_ENTITIES.register("lamp_block", () -> TileEntityType.Builder.create(LampTE::new,
			SkyBlocks.LAMP_BLOCK.getObject1().get()
	).build(null));
	public static final RegistryObject<TileEntityType<MultiMaterialContainerTE>> DRAWER_TE = TILE_ENTITIES.register("drawer_te", () -> TileEntityType.Builder.create(MultiMaterialContainerTE::new,
			SkyBlocks.DRAWER_BLOCK.getObject1().get()
	).build(null));
	
	public static final RegistryObject<TileEntityType<SkyboxTileEntity>> SKYBOX = TILE_ENTITIES.register("skybox_tile_entity", () -> TileEntityType.Builder.create(SkyboxTileEntity::new,
			getSkyboxBlocks()
	).build(null));
	
	public static final RegistryObject<TileEntityType<ItemObserverTE>> ITEM_OBSERVER = TILE_ENTITIES.register("item_observer", () -> TileEntityType.Builder.create(ItemObserverTE::new,
			SkyBlocks.ITEM_OBSERVER.getObject1().get()
	).build(null));
	
	public static final RegistryObject<TileEntityType<ChestTE>> CHEST_TE = TILE_ENTITIES.register("chest", () -> TileEntityType.Builder.create(ChestTE::new,
			getChestBlocks()
	).build(null));
	
	public static SkyboxBlock[] getSkyboxBlocks() {
		GetSkyboxBlocksEvent event = new GetSkyboxBlocksEvent();
		SkyIslesAPI.INSTANCE.post(event);
		SkyboxBlock[] blocks = new SkyboxBlock[event.blocks.size()];
		for (int i=0;i<event.blocks.size();i++) {
			blocks[i] = event.blocks.get(i);
		}
		return blocks;
	}
	
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
