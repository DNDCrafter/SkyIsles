//package com.thelastflames.skyisles.blocks;
//
//import com.thelastflames.skyisles.containers.ForgeContainer;
//import net.minecraft.block.Block;
//import net.minecraft.block.BlockState;
//import net.minecraft.block.SoundType;
//import net.minecraft.block.material.Material;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.entity.player.PlayerInventory;
//import net.minecraft.inventory.Inventory;
//import net.minecraft.inventory.container.Container;
//import net.minecraft.inventory.container.INamedContainerProvider;
//import net.minecraft.util.ActionResultType;
//import net.minecraft.util.Hand;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.BlockRayTraceResult;
//import net.minecraft.util.text.ITextComponent;
//import net.minecraft.util.text.StringTextComponent;
//import net.minecraft.world.World;
//import net.minecraftforge.common.ToolType;
//
//import javax.annotation.Nonnull;
//
//public class ForgeBlock extends Block {
//	public ForgeBlock() {
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
//		if (!worldIn.isRemote) {
//			player.openContainer(inamedcontainerprovider);
//		}
//		return ActionResultType.SUCCESS;
//	}
//
//	INamedContainerProvider inamedcontainerprovider = new INamedContainerProvider() {
//		@Override
//		public ITextComponent getDisplayName() {
//			return new StringTextComponent("Forge");
//		}
//
//		@Nonnull
//		@Override
//		public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
//			return new ForgeContainer(ForgeContainer.TYPE, p_createMenu_1_, p_createMenu_2_, new Inventory(9), 9, p_createMenu_3_);
//		}
//	};
//}
