//package com.thelastflames.skyisles.blocks;
//
//import com.mojang.blaze3d.matrix.MatrixStack;
//import com.thelastflames.skyisles.blocks.bases.DynamicModelBlock;
//import com.thelastflames.skyisles.registry.SkyBlocks;
//import com.thelastflames.skyisles.tile_entity.MultiMaterialContainerTE;
//import com.thelastflames.skyisles.tile_entity.MultiMaterialTE;
//import com.thelastflames.skyisles.utils.MaterialList;
//import com.thelastflames.skyisles.utils.NBTUtil;
//import com.thelastflames.skyisles.utils.StringyHashMap;
//import net.minecraft.block.Block;
//import net.minecraft.block.BlockState;
//import net.minecraft.block.SoundType;
//import net.minecraft.block.material.Material;
//import net.minecraft.client.renderer.IRenderTypeBuffer;
//import net.minecraft.client.renderer.Quaternion;
//import net.minecraft.entity.Entity;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.item.ItemStack;
//import net.minecraft.nbt.CompoundNBT;
//import net.minecraft.tileentity.TileEntity;
//import net.minecraft.util.ActionResultType;
//import net.minecraft.util.Hand;
//import net.minecraft.util.ResourceLocation;
//import net.minecraft.util.math.*;
//import net.minecraft.util.math.shapes.ISelectionContext;
//import net.minecraft.util.math.shapes.VoxelShape;
//import net.minecraft.util.math.shapes.VoxelShapes;
//import net.minecraft.world.IBlockReader;
//import net.minecraft.world.World;
//import net.minecraftforge.common.ToolType;
//import tfc.dynamic_rendering.API.ExtrudedTexture;
//import tfc.dynamic_rendering.API.PreppedModel;
//import tfc.dynamic_rendering.API.Renderer;
//import tfc.dynamic_rendering.API.TexturedModel;
//
//import javax.annotation.Nonnull;
//import javax.annotation.Nullable;
//import java.util.Optional;
//
//public class DrawerBlock extends DynamicModelBlock {
//	public DrawerBlock() {
//		super(Properties.create(Material.ROCK)
//				.hardnessAndResistance(3.0F, 40.0F)
//				.sound(SoundType.STONE)
//				.harvestLevel(0)
//				.harvestTool(ToolType.PICKAXE)
//		);
//	}
//
//	@Override
//	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
//		return ((MultiMaterialContainerTE) worldIn.getTileEntity(pos)).onBlockActivated(state, worldIn, pos, player, handIn, hit);
//	}
//
//	@Override
//	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
//		if (state.getBlock() != newState.getBlock()) {
//			TileEntity tileentity = worldIn.getTileEntity(pos);
//			if (tileentity instanceof MultiMaterialContainerTE) {
//				((MultiMaterialContainerTE) tileentity).onRemove();
//			}
//
//			super.onReplaced(state, worldIn, pos, newState, isMoving);
//		}
//	}
//
//	public static final VoxelShape raytraceShapeTop = Block.makeCuboidShape(1, 9, 0, 15, 15, 15);
//	public static final VoxelShape raytraceShapeBottom = Block.makeCuboidShape(1, 1, 0, 15, 7, 15);
//	public static final VoxelShape raytraceShape = VoxelShapes.or(
//			Block.makeCuboidShape(0, 0, 0, 1, 16, 16),
//			Block.makeCuboidShape(15, 0, 0, 16, 16, 16),
//			Block.makeCuboidShape(0, 0, 0, 16, 1, 16),
//			Block.makeCuboidShape(0, 15, 0, 16, 16, 16),
//			Block.makeCuboidShape(0, 0, 15, 16, 16, 16),
//			Block.makeCuboidShape(0, 7, 0, 16, 9, 16)
//	);
//
//	@Override
//	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
//		Entity player = context.getEntity();
//		double distance = 999999;
//		VoxelShape part = raytraceShape;
//		if (player != null) {
//			Vec3d start = player.getEyePosition(0);
//			Vec3d stop = start.add(player.getLookVec().scale(9));
//			for (AxisAlignedBB bb : raytraceShapeTop.toBoundingBoxList()) {
//				Optional<Vec3d> vec3dOptional = bb.offset(pos).rayTrace(start, stop);
//				if (vec3dOptional.isPresent()) {
//					if (vec3dOptional.get().distanceTo(start) < distance) {
//						distance = vec3dOptional.get().distanceTo(start);
//						part = raytraceShapeTop;
//					}
//				}
//			}
//			for (AxisAlignedBB bb : raytraceShapeBottom.toBoundingBoxList()) {
//				Optional<Vec3d> vec3dOptional = bb.offset(pos).rayTrace(start, stop);
//				if (vec3dOptional.isPresent()) {
//					if (vec3dOptional.get().distanceTo(start) < distance) {
//						distance = vec3dOptional.get().distanceTo(start);
//						part = raytraceShapeBottom;
//					}
//				}
//			}
//			for (AxisAlignedBB bb : raytraceShape.toBoundingBoxList()) {
//				Optional<Vec3d> vec3dOptional = bb.offset(pos).rayTrace(start, stop);
//				if (vec3dOptional.isPresent()) {
//					if (vec3dOptional.get().distanceTo(start) < distance) {
//						distance = vec3dOptional.get().distanceTo(start);
//						part = raytraceShape;
//					}
//				}
//			}
//		}
//		return part;
//	}
//
//	@Override
//	public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
//		return raytraceShape;
//	}
//
//	@Override
//	public VoxelShape getRenderShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
//		return raytraceShape;
//	}
//
//	@Override
//	public VoxelShape getRaytraceShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
//		return raytraceShape;
//	}
//
//	@Nullable
//	@Override
//	public TileEntity createNewTileEntity(@Nonnull IBlockReader worldIn) {
//		return new MultiMaterialContainerTE();
//	}
//
//	@Nullable
//	@Override
//	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
//		return new MultiMaterialContainerTE();
//	}
//
//	static StringyHashMap<MaterialList, PreppedModel> modelHashMap = new StringyHashMap<>();
//
//	@Override
//	public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
//		NBTUtil.NBTObjectHolder obj = new NBTUtil.NBTObjectHolder("BlockEntityTag", player.world.getTileEntity(pos).write(new CompoundNBT()));
//		ItemStack stack = new ItemStack(SkyBlocks.DRAWER_BLOCK.getObject2().get());
//		CompoundNBT nbt = obj.Package();
//		nbt.remove("x");
//		nbt.remove("y");
//		nbt.remove("z");
//		nbt.remove("id");
//		stack.setTag(nbt);
//		return stack;
//	}
//
//	@Nonnull
//	@Override
//	public PreppedModel getModel(MaterialList listIn, BlockPos posIn, World worldIn) {
//		modelHashMap.objects.clear();
//		modelHashMap.keys.clear();
//		if (!modelHashMap.containsKey(listIn)) {
//			ExtrudedTexture texture1 = new ExtrudedTexture(
//					new ResourceLocation("skyisles:block/lamp_overlay"),
//					new ResourceLocation(listIn.names.get(1)),
//					1, false
//			);
//			ExtrudedTexture texture2 = new ExtrudedTexture(
//					new ResourceLocation("skyisles:block/lamp_base"),
//					new ResourceLocation(listIn.names.get(0)),
//					1, false
//			);
//			TexturedModel txmdl =
//					Renderer.createExtrudedTextureNoTexCorrection(true, texture1)
//							.merge(
////									Renderer.createFlatTexturedModel(texture2.base,true),
//									Renderer.createFlatTexturedModel(texture2.mask, true)
//							).scale(1, 1, 0.01f).translate(0, 0, 0.005f);
//			TexturedModel txmdl2 = txmdl.translate(0, 0, 0).merge(
//					txmdl.rotate((float) Math.toRadians(90), 0).translate(0, 0, 16),
//					txmdl.rotate((float) Math.toRadians(180), 0).translate(16, 0, 16),
//					txmdl.rotate((float) Math.toRadians(-90), 0).translate(16, 0, 0),
//					txmdl.rotate(0, (float) Math.toRadians(-90)).translate(0, 16, 0),
//					txmdl.rotate(0, (float) Math.toRadians(90)).translate(0, 0, 16)
//			);
//			PreppedModel mdl = Renderer.prepModel(txmdl2, false);
//			if (posIn.getY() != -9999) modelHashMap.add(listIn, mdl);
//			else return mdl;
//		}
//		return modelHashMap.get(listIn);
//	}
//
//	@Override
//	public boolean preRender(MatrixStack stack, BlockPos pos) {
//		stack.push();
//		if (pos.equals(new BlockPos(0, -9999, 0))) {
//			stack.rotate(new Quaternion(90, 0, 0, true));
//			stack.translate(0, 0, -1.f);
//		}
//		return false;
//	}
//
//	@Override
//	public void render(MatrixStack stack, IRenderTypeBuffer buffer, PreppedModel mdl, MultiMaterialTE te) {
//	}
//
//	@Override
//	public void postRender(MatrixStack stack, BlockPos pos) {
//		stack.pop();
//	}
//}
