package com.thelastflames.skyisles;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.thelastflames.skyisles.Blocks.MultiMaterialTE;
import com.thelastflames.skyisles.Client.Block.MultiMaterialTERenderer;
import com.thelastflames.skyisles.Items.DefaultNBTBlockItem;
import com.thelastflames.skyisles.Registry.SkyBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

import java.util.HashMap;

public class ITSERLookup {
	static HashMap<String, ItemStackTileEntityRenderer> map=new HashMap<>();
	public static void setupLookup() {
		try {
			map.put("pillar",new ItemStackTileEntityRenderer() {
				@Override
				public void render(ItemStack itemStackIn, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
//					super.render(itemStackIn, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
					try {
						MultiMaterialTE te=new MultiMaterialTE() {
							@Override
							public BlockState getBlockState() {
								return SkyBlocks.PILLAR_BLOCK.getObject1().get().getDefaultState();
							}
						};
						CompoundNBT nbt=(CompoundNBT)itemStackIn.getOrCreateTag().get("BlockEntityTag");
						te.read(nbt);
						MultiMaterialTERenderer.getInstance().render(te,0,matrixStackIn,bufferIn,combinedLightIn,combinedOverlayIn);
					} catch (Exception err) {
						MultiMaterialTE te=new MultiMaterialTE() {
							@Override
							public BlockState getBlockState() {
								return SkyBlocks.PILLAR_BLOCK.getObject1().get().getDefaultState();
							}
						};
						te.read(((DefaultNBTBlockItem)itemStackIn.getItem()).getDefaultNBT().getCompound("BlockEntityTag"));
						MultiMaterialTERenderer.getInstance().render(te,0,matrixStackIn,bufferIn,combinedLightIn,combinedOverlayIn);
					}
				}
			});
		} catch (Exception ignored) {}
	}
	
	public static boolean contains(String name) {
		return map.containsKey(name);
	}
	
	public static ItemStackTileEntityRenderer get(String name) {
		return map.get(name);
	}
}
