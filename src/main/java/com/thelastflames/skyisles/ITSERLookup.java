package com.thelastflames.skyisles;

import com.thelastflames.skyisles.client.Item.PickaxeISTER;
import com.thelastflames.skyisles.client.block.IISter;
import com.thelastflames.skyisles.client.block.ISter;
import com.thelastflames.skyisles.client.block.MultiMaterialTERenderer;
import com.thelastflames.skyisles.items.DefaultNBTBlockItem;
import com.thelastflames.skyisles.registry.SkyBlocks;
import com.thelastflames.skyisles.tile_entity.MultiMaterialTE;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.nbt.CompoundNBT;

import java.util.HashMap;

public class ITSERLookup {
	static HashMap<String, ItemStackTileEntityRenderer> map=new HashMap<>();
	public static void setupLookup() {
		try {
			IISter multimaterialteiisterpillar=(itemStackIn, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn) -> {
				try {
					MultiMaterialTE te=new MultiMaterialTE() {
						@Override
						public BlockState mgetBlockState() {
							return SkyBlocks.PILLAR_BLOCK.getObject1().get().getDefaultState();
						}
					};
					CompoundNBT nbt=(CompoundNBT)itemStackIn.getOrCreateTag().get("BlockEntityTag");
					te.read(nbt);
					MultiMaterialTERenderer.getInstance().render(te,0,matrixStackIn,bufferIn,combinedLightIn,combinedOverlayIn);
				} catch (Exception err) {
					MultiMaterialTE te=new MultiMaterialTE() {
						@Override
						public BlockState mgetBlockState() {
							return SkyBlocks.PILLAR_BLOCK.getObject1().get().getDefaultState();
						}
					};
					te.read(((DefaultNBTBlockItem)itemStackIn.getItem()).getDefaultNBT().getCompound("BlockEntityTag"));
					MultiMaterialTERenderer.getInstance().render(te,0,matrixStackIn,bufferIn,combinedLightIn,combinedOverlayIn);
				}
			};
			IISter multimaterialteiisterlamp=(itemStackIn, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn) -> {
				try {
					MultiMaterialTE te=new MultiMaterialTE() {
						@Override
						public BlockState mgetBlockState() {
							return SkyBlocks.LAMP_BLOCK.getObject1().get().getDefaultState();
						}
					};
					CompoundNBT nbt=(CompoundNBT)itemStackIn.getOrCreateTag().get("BlockEntityTag");
					te.read(nbt);
					MultiMaterialTERenderer.getInstance().render(te,0,matrixStackIn,bufferIn,combinedLightIn,combinedOverlayIn);
				} catch (Exception err) {
					MultiMaterialTE te=new MultiMaterialTE() {
						@Override
						public BlockState mgetBlockState() {
							return SkyBlocks.LAMP_BLOCK.getObject1().get().getDefaultState();
						}
					};
					te.read(((DefaultNBTBlockItem)itemStackIn.getItem()).getDefaultNBT().getCompound("BlockEntityTag"));
					MultiMaterialTERenderer.getInstance().render(te,0,matrixStackIn,bufferIn,combinedLightIn,combinedOverlayIn);
				}
			};
			map.put("pillar",new ISter(multimaterialteiisterpillar));
			map.put("lamp_block",new ISter(multimaterialteiisterlamp));
			map.put("tool_metal_pickaxe", new PickaxeISTER());
		} catch (Exception ignored) {}
	}
	
	public static boolean contains(String name) {
		return map.containsKey(name);
	}
	
	public static ItemStackTileEntityRenderer get(String name) {
		return map.get(name);
	}
}
