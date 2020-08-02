package com.thelastflames.skyisles.registry;

import com.thelastflames.skyisles.SkyIsles;
import com.thelastflames.skyisles.tile_entity.LampTE;
import com.thelastflames.skyisles.tile_entity.MultiMaterialContainerTE;
import com.thelastflames.skyisles.tile_entity.MultiMaterialTE;
import com.thelastflames.skyisles.tile_entity.SkyboxTileEntity;
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
			SkyBlocks.SKYBOX_BLOCK.getObject1().get(),
			SkyBlocks.SKYBOX_BLOCK_PURPLE.getObject1().get()
	).build(null));
}
