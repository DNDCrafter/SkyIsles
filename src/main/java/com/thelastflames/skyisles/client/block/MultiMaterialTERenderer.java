package com.thelastflames.skyisles.client.block;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.thelastflames.skyisles.blocks.bases.DynamicModelBlock;
import com.thelastflames.skyisles.tile_entity.IMultiMaterialTE;
import com.thelastflames.skyisles.tile_entity.MultiMaterialTE;
import net.minecraft.block.DispenserBlock;
import net.minecraft.client.Minecraft;
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
	private static MultiMaterialTERenderer INSTANCE=null;
	public MultiMaterialTERenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);
		INSTANCE=this;
	}
	
	public static MultiMaterialTERenderer getInstance() {
		return INSTANCE;
	}
	
	public void render(IMultiMaterialTE tileEntityIn, float partialTicks, @Nonnull IRenderTypeBuffer bufferIn, MatrixStack matrixStackIn, int combinedLightIn, int combinedOverlayIn) {
		//		Date startTime=new Date();
		matrixStackIn.push();
		
		try {
			if (!((DynamicModelBlock)tileEntityIn.mgetBlockState().getBlock()).preRender(matrixStackIn,tileEntityIn.mgetPos())) {
				if (tileEntityIn!=null) {
					if (tileEntityIn.mgetBlockState().has(DispenserBlock.FACING)) {
						Direction dir=tileEntityIn.mgetBlockState().get(DispenserBlock.FACING);
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
				Minecraft.getInstance().getProfiler().startSection("render_multi_material_te");
				try {
					PreppedModel mdl=((DynamicModelBlock)tileEntityIn.mgetBlockState().getBlock()).getModel(((IMultiMaterialTE)tileEntityIn).getMaterialList(),tileEntityIn.mgetPos(),tileEntityIn.mgetWorld());
					matrixStackIn.push();
//				matrixStackIn.rotate(new Quaternion(0,180,0,true));
//				matrixStackIn.translate(-16,0,-16);
//				matrixStackIn.scale(1,1,0.01f);
					Renderer.renderPreparedModel(mdl,bufferIn,matrixStackIn,combinedLightIn,combinedOverlayIn);
					matrixStackIn.pop();
//				matrixStackIn.push();
//				matrixStackIn.rotate(new Quaternion(0,90,0,true));
//				matrixStackIn.translate(-16,0,0);
//				matrixStackIn.scale(1,1,0.01f);
//				Renderer.renderPreparedModel(mdl,bufferIn,matrixStackIn,combinedLightIn,combinedOverlayIn,16777215);
//				matrixStackIn.pop();
//				matrixStackIn.push();
//				matrixStackIn.rotate(new Quaternion(0,-90,0,true));
//				matrixStackIn.translate(0,0,-16);
//				matrixStackIn.scale(1,1,0.01f);
//				Renderer.renderPreparedModel(mdl,bufferIn,matrixStackIn,combinedLightIn,combinedOverlayIn,16777215);
//				matrixStackIn.pop();
//				matrixStackIn.push();
//				matrixStackIn.rotate(new Quaternion(0,0,0,true));
//				matrixStackIn.translate(0,0,0);
//				matrixStackIn.scale(1,1,0.01f);
//				Renderer.renderPreparedModel(mdl,bufferIn,matrixStackIn,combinedLightIn,combinedOverlayIn,16777215);
//				matrixStackIn.pop();
				} catch (Exception ignored) {
				}
				matrixStackIn.pop();
			} else {
				((DynamicModelBlock)tileEntityIn.mgetBlockState().getBlock()).render(matrixStackIn,bufferIn,null,((MultiMaterialTE)tileEntityIn));
			}
		} catch (Throwable err) {}
		Minecraft.getInstance().getProfiler().endSection();
		((DynamicModelBlock)tileEntityIn.mgetBlockState().getBlock()).postRender(matrixStackIn,tileEntityIn.mgetPos());
		matrixStackIn.pop();
//		System.out.println("timeSpent"+((new Date().getTime())-startTime.getTime()));
	}
	
	@Override
	public void render(@Nonnull TileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, @Nonnull IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
		render((IMultiMaterialTE)tileEntityIn,partialTicks,bufferIn,matrixStackIn,combinedLightIn,combinedLightIn);
	}
}
