package com.thelastflames.skyisles.blocks;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.thelastflames.skyisles.blocks.bases.DynamicModelBlock;
import com.thelastflames.skyisles.registry.SkyBlocks;
import com.thelastflames.skyisles.tile_entity.LampTE;
import com.thelastflames.skyisles.tile_entity.MultiMaterialTE;
import com.thelastflames.skyisles.utils.MaterialList;
import com.thelastflames.skyisles.utils.NBTUtil;
import com.thelastflames.skyisles.utils.StringyHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import tfc.dynamic_rendering.API.ExtrudedTexture;
import tfc.dynamic_rendering.API.PreppedModel;
import tfc.dynamic_rendering.API.Renderer;
import tfc.dynamic_rendering.API.TexturedModel;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class LampBlock extends DynamicModelBlock {
	public LampBlock() {
		super(Properties.create(Material.ROCK)
				.hardnessAndResistance(3.0F, 40.0F)
				.sound(SoundType.STONE)
				.harvestLevel(0)
				.harvestTool(ToolType.PICKAXE)
		);
	}
	
	@Override
	public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
		TileEntity te=world.getTileEntity(pos);
		if (te!=null&&te instanceof LampTE) {
			Item light=Registry.ITEM.getOrDefault(new ResourceLocation(((LampTE) te).light));
			Block block=Block.getBlockFromItem(light);
			if (block.getLightValue(block.getDefaultState())>=1) {
				return block.getLightValue(block.getDefaultState());
			}
		}
		return 0;
	}
	
	@Nullable
	@Override
	public TileEntity createNewTileEntity(@Nonnull IBlockReader worldIn) {
		return new LampTE();
	}
	
	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new LampTE();
	}
	
	static StringyHashMap<MaterialList,PreppedModel> modelHashMap=new StringyHashMap<>();
	
	@Override
	public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
		NBTUtil.NBTObjectHolder obj=new NBTUtil.NBTObjectHolder("BlockEntityTag",player.world.getTileEntity(pos).write(new CompoundNBT()));
		ItemStack stack=new ItemStack(SkyBlocks.LAMP_BLOCK.getObject2().get());
		CompoundNBT nbt=obj.Package();
		nbt.remove("x");
		nbt.remove("y");
		nbt.remove("z");
		nbt.remove("id");
		stack.setTag(nbt);
		return stack;
	}
	
	@Nonnull
	@Override
	public PreppedModel getModel(MaterialList listIn, BlockPos posIn, World worldIn) {
		modelHashMap.objects.clear();
		modelHashMap.keys.clear();
		if (!modelHashMap.containsKey(listIn)) {
			ExtrudedTexture texture1=new ExtrudedTexture(
					new ResourceLocation("skyisles:block/lamp_overlay"),
					new ResourceLocation(listIn.names.get(1)),
					1,false
			);
			ExtrudedTexture texture2=new ExtrudedTexture(
					new ResourceLocation("skyisles:block/lamp_base"),
					new ResourceLocation(listIn.names.get(0)),
					1,false
			);
			TexturedModel txmdl=
					Renderer.createExtrudedTextureNoTexCorrection(true,texture1)
							.merge(
//									Renderer.createFlatTexturedModel(texture2.base,true),
									Renderer.createFlatTexturedModel(texture2.mask,true)
							).scale(1,1,0.01f).translate(0,0,0.005f);
			TexturedModel txmdl2=txmdl.translate(0,0,0).merge(
					txmdl.rotate((float) Math.toRadians(90),0).translate(0,0,16),
					txmdl.rotate((float) Math.toRadians(180),0).translate(16,0,16),
					txmdl.rotate((float) Math.toRadians(-90),0).translate(16,0,0),
					txmdl.rotate(0,(float) Math.toRadians(-90)).translate(0,16,0),
					txmdl.rotate(0,(float) Math.toRadians(90)).translate(0,0,16)
			);
			PreppedModel mdl=Renderer.prepModel(txmdl2,false);
			if (posIn.getY()!=-9999) modelHashMap.add(listIn,mdl); else return mdl;
		}
		return modelHashMap.get(listIn);
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
