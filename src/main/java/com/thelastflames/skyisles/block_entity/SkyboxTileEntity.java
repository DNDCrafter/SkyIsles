//package com.thelastflames.skyisles.tile_entity;
//
//import com.thelastflames.skyisles.blocks.bases.SkyboxBlock;
//import com.thelastflames.skyisles.registry.SkyTileEntities;
//import net.minecraft.block.Block;
//import net.minecraft.block.BlockState;
//import net.minecraft.tileentity.TileEntity;
//import net.minecraft.tileentity.TileEntityType;
//import net.minecraft.util.Direction;
//import net.minecraft.util.ResourceLocation;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.World;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//
//public class SkyboxTileEntity extends TileEntity {
//	public SkyboxTileEntity() {
//		super(SkyTileEntities.SKYBOX.get());
//	}
//
//	public SkyboxTileEntity(TileEntityType type) {
//		super(type);
//	}
//
//	public ResourceLocation getSkyTexture(Block blockIn) {
//		return ((SkyboxBlock) blockIn).getBackground(this.world);
//	}
//
//	public ResourceLocation getPassTexture(Block blockIn) {
//		return ((SkyboxBlock) blockIn).getSky(this.world);
//	}
//
//	public ResourceLocation getStarsPassTexture(Block blockIn) {
//		return ((SkyboxBlock) blockIn).getStars(this.world);
//	}
//
//	@OnlyIn(Dist.CLIENT)
//	public boolean shouldRenderFace(Direction face, World world, BlockPos pos) {
//		if (world == null || pos == null) {
//			return true;
//		}
//		BlockPos pos1 = pos.offset(face);
//		BlockState state = world.getBlockState(pos1);
//		return (!state.isSolidSide(world, pos1, face.getOpposite())) || (!state.isOpaqueCube(world, pos1));
//	}
//}
