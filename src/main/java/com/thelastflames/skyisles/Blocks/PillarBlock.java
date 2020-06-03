package com.thelastflames.skyisles.Blocks;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.thelastflames.skyisles.Blocks.Bases.DynamicModelBlock;
import com.thelastflames.skyisles.Utils.MaterialList;
import com.thelastflames.skyisles.Utils.StringyHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import tfc.dynamic_rendering.API.ExtrudedTexture;
import tfc.dynamic_rendering.API.PreppedModel;
import tfc.dynamic_rendering.API.Renderer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PillarBlock extends DynamicModelBlock {
	public PillarBlock() {
		super(Properties.create(Material.ROCK)
				.hardnessAndResistance(3.0F, 40.0F)
				.sound(SoundType.STONE)
				.harvestLevel(0)
				.harvestTool(ToolType.PICKAXE)
		);
	}
	
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(DispenserBlock.FACING);
	}
	
	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.getDefaultState().with(DispenserBlock.FACING,context.getFace());
	}
	
	static StringyHashMap<BlockPos,PreppedModel> modelHashMap=new StringyHashMap<>();
	
	@Override
	public boolean removedByPlayer(BlockState state, World world, BlockPos pos, PlayerEntity player, boolean willHarvest, IFluidState fluid) {
		if (modelHashMap.containsKey(pos)) modelHashMap.remove(pos);
		return super.removedByPlayer(state,world,pos,player,willHarvest,fluid);
	}
	
	@Override
	public void onPlayerDestroy(IWorld worldIn, BlockPos pos, BlockState state) {
		if (modelHashMap.containsKey(pos)) modelHashMap.remove(pos);
		super.onPlayerDestroy(worldIn, pos, state);
	}
	
	@Override
	public void onExplosionDestroy(World worldIn, BlockPos pos, Explosion explosionIn) {
		if (modelHashMap.containsKey(pos)) modelHashMap.remove(pos);
		super.onExplosionDestroy(worldIn, pos, explosionIn);
	}
	
	@Override
	public void onReplaced(@Nonnull BlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
		if (modelHashMap.containsKey(pos)) modelHashMap.remove(pos);
		super.onReplaced(state, worldIn, pos, newState, isMoving);
	}
	
	@Nonnull
	@Override
	public PreppedModel getModel(MaterialList listIn, BlockPos posIn, World worldIn) {
		if (!modelHashMap.containsKey(posIn)||posIn.getY()==-9999) {
			ExtrudedTexture texture1=new ExtrudedTexture(
					new ResourceLocation("skyisles:block/pillar_base"),
					new ResourceLocation(listIn.names.get(0)),
					0,true
			);
			ExtrudedTexture texture2=new ExtrudedTexture(
					new ResourceLocation("skyisles:block/pillar_overlay"),
					new ResourceLocation(listIn.names.get(1)),
					0,true
			);
			if (posIn.getY()!=-9999) {
				modelHashMap.add(posIn,Renderer.prepExtrudedTexture(true,texture1,texture2));
			} else {
				return Renderer.prepExtrudedTexture(true,texture1,texture2);
			}
		}
		return modelHashMap.get(posIn);
	}
	
	@Override
	public boolean preRender(MatrixStack stack, BlockPos pos) {
		stack.push();
		if (pos.equals(new BlockPos(0,-9999,0))) {
			stack.rotate(new Quaternion(90,0,0,true));
			stack.translate(0,0,-1.f);
		}
		return false;
	}
	
	@Override
	public void render(MatrixStack stack, IRenderTypeBuffer buffer, PreppedModel mdl, MultiMaterialTE te) {
	
	}
	
	@Override
	public void postRender(MatrixStack stack, BlockPos pos) {
		stack.pop();
	}
}
