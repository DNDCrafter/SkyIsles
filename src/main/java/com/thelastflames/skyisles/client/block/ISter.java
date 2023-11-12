//package com.thelastflames.skyisles.client.block;
//
//import com.mojang.blaze3d.matrix.MatrixStack;
//import net.minecraft.client.renderer.IRenderTypeBuffer;
//import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
//import net.minecraft.item.ItemStack;
//
//public class ISter extends ItemStackTileEntityRenderer {
//	private final IISter iiSter;
//
//	public ISter(IISter iiSter) {
//		super();
//		this.iiSter = iiSter;
//	}
//
//	@Override
//	public final void render(ItemStack itemStackIn, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
//		super.render(itemStackIn, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
//		iiSter.renderStack(itemStackIn, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
//	}
//}
