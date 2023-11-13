package com.thelastflames.skyisles.blocks;

import com.thelastflames.skyisles.blocks.bases.Chest;
import com.thelastflames.skyisles.block_entity.ChestTE;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;

import java.util.function.Supplier;

public class ChestBlock extends Chest<ChestTE> {
    public ChestBlock(Supplier<BlockEntityType<? extends ChestTE>> tileEntityTypeIn) {
        super(Block.Properties.copy(Blocks.CHEST), tileEntityTypeIn);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_51562_) {
        super.createBlockStateDefinition(p_51562_);
        p_51562_.add(MATERIAL);
    }

    private static final EnumProperty<ChestType> MATERIAL = EnumProperty.create("material_type", ChestType.class);

    public BlockEntity newBlockEntity(BlockPos p_153064_, BlockState p_153065_) {
        return new ChestTE(blockEntityType.get(), p_153064_, p_153065_);
    }

    public static ChestType getType(BlockState state) {
        if (state.hasProperty(MATERIAL)) {
            return state.getValue(MATERIAL);
        } else {
            return ChestType.REDWOOD;
        }
    }

    @Override
    public BlockState stateFor(ItemStack itemInHand) {
        BlockState state = defaultBlockState();
        if (itemInHand.hasTag()) {
            CompoundTag tg = itemInHand.getTag();
            if (tg.contains("material")) {
                String mat = tg.getString("material");
                ChestType typ = ChestType.byName(mat);
                if (typ != null)
                    state = state.setValue(MATERIAL, typ);
            }
        }
        return state;
    }

    @Override
    public boolean canMerge(BlockState state0, BlockState state1) {
        boolean isMergeable = super.canMerge(state0, state1);
        if (!isMergeable) return false;
        return state0.getValue(MATERIAL).equals(state1.getValue(MATERIAL));
    }

    public enum ChestType implements StringRepresentable {
        REDWOOD("redwood"),
        REDWOOD_BARK("redwoodbark"),
        ;

        public String name;

        ChestType(String name) {
            this.name = name;
        }

        private static final ChestType[] TYPES = values();

        public static ChestType byName(String mat) {
            for (ChestType value : TYPES)
                if (value.name.equals(mat))
                    return value;
            return null;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }
}
