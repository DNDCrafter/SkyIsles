package com.thelastflames.skyisles.blocks;

import com.thelastflames.skyisles.blocks.bases.Chest;
import com.thelastflames.skyisles.tile_entity.ChestTE;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class ChestBlock extends Chest<ChestTE> {
	public ChestBlock(Supplier<TileEntityType<? extends ChestTE>> tileEntityTypeIn) {
		super(Block.Properties.from(Blocks.CHEST), tileEntityTypeIn);
	}
	
	@Override
	public TileEntity createNewTileEntity(IBlockReader worldIn) {
		return super.createNewTileEntity(worldIn);
	}
	
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(typeEnumProperty);
	}
	
	private static final EnumProperty<ChestType> typeEnumProperty = EnumProperty.create("material_type",ChestType.class);
	
	public static ChestType getType(BlockState state) {
		if (state.has(typeEnumProperty)) {
			return state.get(typeEnumProperty);
		} else {
			return ChestType.REDWOOD;
		}
	}
	
	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new ChestTE();
	}
	
	public enum ChestType implements IStringSerializable {
		REDWOOD("redwood"),
		REDWOOD_BARK("redwoodbark"),
		;
		
		public String name;
		
		ChestType(String name) {
			this.name=name;
		}
		
		@Override
		public String getName() {
			return name;
		}
	}
}
