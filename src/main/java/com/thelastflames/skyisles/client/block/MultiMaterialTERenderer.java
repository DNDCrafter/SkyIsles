package com.thelastflames.skyisles.client.block;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.thelastflames.skyisles.SkyIsles;
import com.thelastflames.skyisles.blocks.bases.DynamicModelBlock;
import com.thelastflames.skyisles.tile_entity.IMultiMaterialTE;
import com.thelastflames.skyisles.tile_entity.MultiMaterialTE;
import net.minecraft.block.DispenserBlock;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import tfc.dynamic_rendering.API.PreppedModel;
import tfc.dynamic_rendering.API.Renderer;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class MultiMaterialTERenderer extends TileEntityRenderer<TileEntity> {
	private static MultiMaterialTERenderer INSTANCE = null;
	
	public MultiMaterialTERenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);
		INSTANCE = this;
	}
	
	public static MultiMaterialTERenderer getInstance() {
		return INSTANCE;
	}
	
	public void render(IMultiMaterialTE tileEntityIn, float partialTicks, @Nonnull IRenderTypeBuffer bufferIn, MatrixStack matrixStackIn, int combinedLightIn, int combinedOverlayIn) {
		matrixStackIn.push();
		
		try {
			SkyIsles.createBFPSGraphSection("skyisles:Setup Matrix", 0.3d, 0.4d, 0.3d);
			if (!((DynamicModelBlock) tileEntityIn.mgetBlockState().getBlock()).preRender(matrixStackIn, tileEntityIn.mgetPos())) {
				if (tileEntityIn != null) {
					if (tileEntityIn.mgetBlockState().has(DispenserBlock.FACING)) {
						Direction dir = tileEntityIn.mgetBlockState().get(DispenserBlock.FACING);
						matrixStackIn.rotate(dir.getRotation());
						if (dir.equals(Direction.NORTH)) {
							matrixStackIn.translate(-1, -1, -1);
						} else if (dir.equals(Direction.EAST)) {
							matrixStackIn.translate(-1, 0, -1);
						} else if (dir.equals(Direction.SOUTH)) {
							matrixStackIn.translate(0, 0, -1);
						} else if (dir.equals(Direction.WEST) || dir.equals(Direction.DOWN)) {
							matrixStackIn.translate(0, -1, -1);
						}
					}
				}
				matrixStackIn.push();
				matrixStackIn.scale(1 / 16f, 1 / 16f, 1 / 16f);
				try {
					SkyIsles.createBFPSGraphSection("skyisles:Create Or Grab TE Model", 0.9d, 0.2d, 0.8525d);
					PreppedModel mdl = ((DynamicModelBlock) tileEntityIn.mgetBlockState().getBlock()).getModel(((IMultiMaterialTE) tileEntityIn).getMaterialList(), tileEntityIn.mgetPos(), tileEntityIn.mgetWorld());
					SkyIsles.createBFPSGraphSection("skyisles:Render Multi Material TE", 0.2d, 0.9d, 1d);
					matrixStackIn.push();
					Renderer.renderPreparedModel(mdl, bufferIn, matrixStackIn, combinedLightIn, combinedOverlayIn);
					matrixStackIn.pop();
				} catch (Exception ignored) {
				}
				matrixStackIn.pop();
			} else {
				((DynamicModelBlock) tileEntityIn.mgetBlockState().getBlock()).render(matrixStackIn, bufferIn, null, ((MultiMaterialTE) tileEntityIn));
			}
		} catch (Throwable ignored) {
		}
		SkyIsles.endBFPSGraphSection();
		((DynamicModelBlock) tileEntityIn.mgetBlockState().getBlock()).postRender(matrixStackIn, tileEntityIn.mgetPos());
		matrixStackIn.pop();
	}
	
	@Override
	public void render(@Nonnull TileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, @Nonnull IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
		render((IMultiMaterialTE) tileEntityIn, partialTicks, bufferIn, matrixStackIn, combinedLightIn, combinedLightIn);
	}
}
