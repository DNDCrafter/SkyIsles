//package com.thelastflames.skyisles.client.block;
//
//import com.thelastflames.skyisles.tile_entity.SkyboxTileEntity;
//import net.minecraft.block.Block;
//import net.minecraft.block.BlockState;
//import net.minecraft.client.Minecraft;
//
//import java.util.function.Supplier;
//
//public class SkyboxIster extends ISter {
//	public SkyboxIster(Supplier<Block> blockSupplier) {
//		super((itemStackIn, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn) -> {
//			SkyboxTileEntity te = new SkyboxTileEntity() {
//				@Override
//				public BlockState getBlockState() {
//					return blockSupplier.get().getDefaultState();
//				}
//			};
//			te.setPos(Minecraft.getInstance().player.getPosition());
//			SkyboxRenderer.getInstance().render(te, 0, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
//		});
//	}
//}
