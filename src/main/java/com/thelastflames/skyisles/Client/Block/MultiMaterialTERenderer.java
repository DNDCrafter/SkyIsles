package com.thelastflames.skyisles.Client.Block;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.thelastflames.skyisles.Blocks.Bases.DynamicModelBlock;
import com.thelastflames.skyisles.Blocks.MultiMaterialTE;
import net.minecraft.block.DispenserBlock;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import tfc.dynamic_rendering.API.PreppedModel;
import tfc.dynamic_rendering.API.Renderer;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class MultiMaterialTERenderer extends TileEntityRenderer<MultiMaterialTE> {
	private static MultiMaterialTERenderer INSTANCE=null;
	public MultiMaterialTERenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);
		INSTANCE=this;
	}
	
	public static MultiMaterialTERenderer getInstance() {
		return INSTANCE;
	}
	
	@Override
	public void render(MultiMaterialTE tileEntityIn, float partialTicks, MatrixStack matrixStackIn, @Nonnull IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
		matrixStackIn.push();
		if (!((DynamicModelBlock)tileEntityIn.getBlockState().getBlock()).preRender(matrixStackIn,tileEntityIn.getPos())) {
			if (tileEntityIn!=null) {
				if (tileEntityIn.getBlockState().has(DispenserBlock.FACING)) {
					Direction dir=tileEntityIn.getBlockState().get(DispenserBlock.FACING);
					matrixStackIn.rotate(dir.getRotation());
					if (dir.equals(Direction.NORTH)) {
						matrixStackIn.translate(-1,-1,-1);
					} else if (dir.equals(Direction.EAST)) {
						matrixStackIn.translate(-1,0,-1);
					} else if (dir.equals(Direction.SOUTH)) {
						matrixStackIn.translate(0,0,-1);
					} else if (dir.equals(Direction.WEST)||dir.equals(Direction.DOWN)) {
						matrixStackIn.translate(0,-1,-1);
					}
				}
			}
			matrixStackIn.push();
			matrixStackIn.scale(1/16f,1/16f,1/16f);
//			System.out.println("start"+new Date().getTime());
			try {
				PreppedModel mdl=((DynamicModelBlock)tileEntityIn.getBlockState().getBlock()).getModel(tileEntityIn.materialList,tileEntityIn.getPos(),tileEntityIn.getWorld());
				matrixStackIn.push();
				matrixStackIn.rotate(new Quaternion(0,180,0,true));
				matrixStackIn.translate(-16,0,0);
				Renderer.renderPreparedModel(mdl,bufferIn,matrixStackIn,combinedLightIn,combinedOverlayIn,16777215);
				matrixStackIn.pop();
				matrixStackIn.push();
				matrixStackIn.translate(0,0,16);
				Renderer.renderPreparedModel(mdl,bufferIn,matrixStackIn,combinedLightIn,combinedOverlayIn,16777215);
				matrixStackIn.pop();
				matrixStackIn.push();
				matrixStackIn.rotate(new Quaternion(0,-90,0,true));
				Renderer.renderPreparedModel(mdl,bufferIn,matrixStackIn,combinedLightIn,combinedOverlayIn,16777215);
				matrixStackIn.translate(16,0,-16);
				matrixStackIn.rotate(new Quaternion(0,180,0,true));
				Renderer.renderPreparedModel(mdl,bufferIn,matrixStackIn,combinedLightIn,combinedOverlayIn,16777215);
				matrixStackIn.pop();
			} catch (Exception ignored) {
			}
//			System.out.println("stop"+new Date().getTime());
			matrixStackIn.pop();
		} else {
			((DynamicModelBlock)tileEntityIn.getBlockState().getBlock()).render(matrixStackIn,bufferIn,null,tileEntityIn);
		}
		((DynamicModelBlock)tileEntityIn.getBlockState().getBlock()).postRender(matrixStackIn,tileEntityIn.getPos());
		matrixStackIn.pop();
	}
}
