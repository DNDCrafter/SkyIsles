package com.thelastflames.skyisles.blocks;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.thelastflames.skyisles.blocks.bases.DynamicModelBlock;
import com.thelastflames.skyisles.registry.SkyBlocks;
import com.thelastflames.skyisles.tile_entity.MultiMaterialTE;
import com.thelastflames.skyisles.utils.MaterialList;
import com.thelastflames.skyisles.utils.NBTUtil;
import com.thelastflames.skyisles.utils.StringyHashMap;
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
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import tfc.dynamic_rendering.API.ExtrudedTexture;
import tfc.dynamic_rendering.API.PreppedModel;
import tfc.dynamic_rendering.API.Renderer;
import tfc.dynamic_rendering.API.TexturedModel;

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
	
	static StringyHashMap<MaterialList,PreppedModel> modelHashMap=new StringyHashMap<>();
	
	@Override
	public boolean removedByPlayer(BlockState state, World world, BlockPos pos, PlayerEntity player, boolean willHarvest, IFluidState fluid) {
		return super.removedByPlayer(state,world,pos,player,willHarvest,fluid);
	}
	
	@Override
	public void onPlayerDestroy(IWorld worldIn, BlockPos pos, BlockState state) {
		super.onPlayerDestroy(worldIn, pos, state);
	}
	
	@Override
	public void onExplosionDestroy(World worldIn, BlockPos pos, Explosion explosionIn) {
		super.onExplosionDestroy(worldIn, pos, explosionIn);
	}
	
	@Override
	public void onReplaced(@Nonnull BlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
		super.onReplaced(state, worldIn, pos, newState, isMoving);
	}
	
	@Nonnull
	@Override
	public PreppedModel getModel(MaterialList listIn, BlockPos posIn, World worldIn) {
		if (!modelHashMap.containsKey(listIn)) {
			ExtrudedTexture texture1=new ExtrudedTexture(
					new ResourceLocation("skyisles:block/pillar_base"),
					new ResourceLocation(listIn.names.get(0)),
					1,true
			);
			ExtrudedTexture texture2=new ExtrudedTexture(
					new ResourceLocation("skyisles:block/pillar_overlay"),
					new ResourceLocation(listIn.names.get(1)),
					1,true
			);
//			PreppedModel mdl=
//					PreppedModel.merge(
//							Renderer.prepExtrudedTextureNoTexCorrection(true,texture1),
//							PreppedModel.merge(
//									PreppedModel.merge (
//											Renderer.createFlatPreppedModel(texture2.mask,false),
//											Renderer.createFlatPreppedModel(texture2.base,true)
//									),
//									Renderer.createFlatPreppedModel(texture2.mask,true)
//							)
//					);
			TexturedModel txmdl=
					Renderer.createExtrudedTextureNoTexCorrection(true,texture1)
					.merge(
						Renderer.createFlatTexturedModel(texture2.base,false),
						Renderer.createFlatTexturedModel(texture2.mask,true)
					).scale(1,1,0.01f);
			TexturedModel txmdl2=txmdl.translate(0,0,0).merge(
					txmdl.rotate((float) Math.toRadians(90),0).translate(0,0,16),
					txmdl.rotate((float) Math.toRadians(180),0).translate(16,0,16),
					txmdl.rotate((float) Math.toRadians(-90),0).translate(16,0,0)
			);
			PreppedModel mdl=Renderer.prepModel(txmdl2,false);
			if (posIn.getY()!=-9999) modelHashMap.add(listIn,mdl); else return mdl;
		}
		return modelHashMap.get(listIn);
	}
	
	@Override
	public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
		NBTUtil.NBTObjectHolder obj=new NBTUtil.NBTObjectHolder("BlockEntityTag",player.world.getTileEntity(pos).write(new CompoundNBT()));
		ItemStack stack=new ItemStack(SkyBlocks.PILLAR_BLOCK.getObject2().get());
		CompoundNBT nbt=obj.Package();
		nbt.remove("x");
		nbt.remove("y");
		nbt.remove("z");
		nbt.remove("id");
		stack.setTag(nbt);
		return stack;
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
	public void render(MatrixStack stack, IRenderTypeBuffer buffer, PreppedModel mdl, MultiMaterialTE te) {}
	
	@Override
	public void postRender(MatrixStack stack, BlockPos pos) {
		stack.pop();
	}
}
