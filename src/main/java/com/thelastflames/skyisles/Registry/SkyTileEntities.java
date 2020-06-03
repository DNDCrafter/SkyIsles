package com.thelastflames.skyisles.Registry;

import com.thelastflames.skyisles.Blocks.MultiMaterialTE;
import com.thelastflames.skyisles.SkyIsles;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SkyTileEntities {
	public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, SkyIsles.ModID);
	
	public static final RegistryObject<TileEntityType<MultiMaterialTE>> MULTIMATERIALTE = TILE_ENTITIES.register("multimaterial_block", () -> TileEntityType.Builder.create(MultiMaterialTE::new,
		SkyBlocks.PILLAR_BLOCK.getObject1().get()
	).build(null));
}
