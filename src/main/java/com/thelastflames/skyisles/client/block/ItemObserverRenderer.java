package com.thelastflames.skyisles.client.block;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.thelastflames.skyisles.blocks.ItemObserver;
import com.thelastflames.skyisles.tile_entity.ItemObserverTE;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;

import javax.annotation.Nonnull;

public class ItemObserverRenderer extends TileEntityRenderer<ItemObserverTE> {
	public ItemObserverRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);
	}
	
	@Override
	public void render(@Nonnull ItemObserverTE tileEntityIn, float partialTicks, @Nonnull MatrixStack matrixStackIn, @Nonnull IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
		if (!tileEntityIn.getBlockState().get(ItemObserver.CLOSED)) {
			matrixStackIn.push();
			if (tileEntityIn.getBlockState().get(ItemObserver.FACING) == Direction.UP) {
				matrixStackIn.translate(0.5, 0.5, 0.95);
				matrixStackIn.scale(0.4f, 0.4f, 0);
			} else if (tileEntityIn.getBlockState().get(ItemObserver.FACING) == Direction.DOWN) {
				matrixStackIn.translate(0.5, 0.5, 0.05);
				matrixStackIn.scale(0.4f, 0.4f, 0);
			} else {
				matrixStackIn.translate(0.5, 0.95, 0.5);
				matrixStackIn.scale(0.4f, 0, 0.4f);
				matrixStackIn.rotate(new Quaternion(0, 180, 0, true));
				matrixStackIn.rotate(tileEntityIn.getBlockState().get(ItemObserver.FACING).getRotation());
			}
			Minecraft.getInstance().getItemRenderer().renderItem(tileEntityIn.stack, ItemCameraTransforms.TransformType.GUI, combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn);
			matrixStackIn.pop();
			matrixStackIn.push();
			if (tileEntityIn.getBlockState().get(ItemObserver.FACING) == Direction.UP) {
				matrixStackIn.translate(0.5, 0.5, 0.95);
				matrixStackIn.translate(0, 0, 0.01f);
				matrixStackIn.rotate(new Quaternion(0, 180, 0, true));
				matrixStackIn.scale(0.4f, 0.4f, 0);
			} else if (tileEntityIn.getBlockState().get(ItemObserver.FACING) == Direction.DOWN) {
				matrixStackIn.translate(0.5, 0.5, 0.05);
				matrixStackIn.translate(0, 0, -0.01f);
				matrixStackIn.scale(0.4f, 0.4f, 0);
			} else {
				matrixStackIn.translate(0.5, 0.95, 0.5);
				matrixStackIn.translate(0, 0.01f, 0);
				matrixStackIn.scale(0.4f, 0, 0.4f);
				matrixStackIn.rotate(new Quaternion(0, 180, 0, true));
				matrixStackIn.rotate(tileEntityIn.getBlockState().get(ItemObserver.FACING).getRotation());
			}
			matrixStackIn.scale(0.05f, 0.05f, 0.05f);
			matrixStackIn.rotate(new Quaternion(0, 0, 180, true));
			matrixStackIn.translate(3, 3, 0);
			Minecraft.getInstance().fontRenderer.renderString("" + tileEntityIn.stack.getCount(), 0, 0, 16777215, true, matrixStackIn.getLast().getMatrix(), bufferIn, false, 0, combinedLightIn);
			matrixStackIn.pop();
		}
	}
}
