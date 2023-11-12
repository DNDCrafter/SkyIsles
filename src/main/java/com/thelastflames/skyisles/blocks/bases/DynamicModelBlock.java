//package com.thelastflames.skyisles.blocks.bases;
//
//import com.mojang.blaze3d.matrix.MatrixStack;
//import com.thelastflames.skyisles.tile_entity.MultiMaterialTE;
//import com.thelastflames.skyisles.utils.MaterialList;
//import net.minecraft.block.Block;
//import net.minecraft.block.BlockState;
//import net.minecraft.block.ITileEntityProvider;
//import net.minecraft.block.SoundType;
//import net.minecraft.block.material.Material;
//import net.minecraft.client.renderer.IRenderTypeBuffer;
//import net.minecraft.tileentity.TileEntity;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.IBlockReader;
//import net.minecraft.world.World;
//import net.minecraftforge.common.ToolType;
//import tfc.dynamic_rendering.API.PreppedModel;
//
//import javax.annotation.Nonnull;
//import javax.annotation.Nullable;
//
//public abstract class DynamicModelBlock extends Block implements ITileEntityProvider {
//	public DynamicModelBlock(Properties properties) {
//		super(properties);
//	}
//
//	public DynamicModelBlock() {
//		super(Properties.create(Material.ROCK)
//				.hardnessAndResistance(3.0F, 40.0F)
//				.sound(SoundType.STONE)
//				.harvestLevel(0)
//				.harvestTool(ToolType.PICKAXE)
//		);
//	}
//
//	public DynamicModelBlock(Material mat, SoundType soundType, int harvestLevel, ToolType... toolTypes) {
//		super(Properties.create(mat)
//				.hardnessAndResistance(3.0F, 40.0F)
//				.sound(SoundType.STONE)
//				.harvestLevel(harvestLevel)
//				.harvestTool(toolTypes[0])
//		);
//	}
//
//	@Nullable
//	@Override
//	public TileEntity createNewTileEntity(@Nonnull IBlockReader worldIn) {
//		return new MultiMaterialTE();
//	}
//
//	@Nullable
//	@Override
//	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
//		return new MultiMaterialTE();
//	}
//
//	//REQUIRED:Get model for render
//	@Nonnull
//	public abstract PreppedModel getModel(MaterialList listIn, BlockPos posIn, World worldIn);
//
//	//return true to cancel normal render
//	public abstract boolean preRender(MatrixStack stack, BlockPos pos);
//
//	//handle custom render
//	public abstract void render(MatrixStack stack, IRenderTypeBuffer buffer, PreppedModel mdl, MultiMaterialTE te);
//
//	//fix matrix if you used preRender
//	public abstract void postRender(MatrixStack stack, BlockPos pos);
//}
