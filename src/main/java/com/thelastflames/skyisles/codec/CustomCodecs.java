package com.thelastflames.skyisles.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Optional;

public class CustomCodecs {
    public static final Codec<Optional<Long>> OPTIONAL_LONG = new OptionalPrimitiveCodec<>() {
        @Override
        protected <T> T writeValue(DynamicOps<T> ops, Long aLong) {
            return ops.createLong(aLong);
        }

        @Override
        protected Long getValue(Number num) {
            return num.longValue();
        }

        @Override
        public String toString() {
            return "Optional Long";
        }
    };

    public static final FallbackCodec<BlockState> SIMPLE_STATE = new FallbackCodec<>(
            BlockState.CODEC,
            (str) -> {
                return ForgeRegistries.BLOCKS.getValue(
                        new ResourceLocation(str)
                ).defaultBlockState();
            }
    );
    public static final OptionalCodec<BlockState> OPTIONAL_BLOCKSTATE = new OptionalCodec<>(SIMPLE_STATE);
    public static final ListCodec<BlockState> BLOCKSTATE_LIST = new ListCodec<>(SIMPLE_STATE);
}
