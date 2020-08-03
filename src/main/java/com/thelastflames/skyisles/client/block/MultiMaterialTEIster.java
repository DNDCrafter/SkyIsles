package com.thelastflames.skyisles.client.block;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.thelastflames.skyisles.tile_entity.IMultiMaterialTE;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class MultiMaterialTEIster implements IISter {
	public MultiMaterialTEIster() {
	}
	
	@Override
	public void renderStack(ItemStack itemStackIn, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
//		try {
//			MultiMaterialTE te=new MultiMaterialTE() {
//				@Override
//				public BlockState getBlockState() {
//					return SkyBlocks.PILLAR_BLOCK.getObject1().get().getDefaultState();
//				}
//			};
//			CompoundNBT nbt=(CompoundNBT)itemStackIn.getOrCreateTag().get("BlockEntityTag");
//			te.read(nbt);
//			MultiMaterialTERenderer.getInstance().render(te,0,matrixStackIn,bufferIn,combinedLightIn,combinedOverlayIn);
//		} catch (Exception err) {
//			MultiMaterialTE te=new MultiMaterialTE() {
//				@Override
//				public BlockState getBlockState() {
//					return SkyBlocks.PILLAR_BLOCK.getObject1().get().getDefaultState();
//				}
//			};
//			te.read(((DefaultNBTBlockItem)itemStackIn.getItem()).getDefaultNBT().getCompound("BlockEntityTag"));
//			MultiMaterialTERenderer.getInstance().render(te,0,matrixStackIn,bufferIn,combinedLightIn,combinedOverlayIn);
//		}
		BlockItem item = ((BlockItem) itemStackIn.getItem());
		IMultiMaterialTE te = (IMultiMaterialTE) item.getBlock().createTileEntity(item.getBlock().getDefaultState(), null);
		CompoundNBT nbt = (CompoundNBT) itemStackIn.getOrCreateTag().get("BlockEntityTag");
		te.read(nbt, itemStackIn, item.getBlock().getDefaultState());
		MultiMaterialTERenderer.getInstance().render(te, 0, bufferIn, matrixStackIn, combinedLightIn, combinedOverlayIn);
	}
}
