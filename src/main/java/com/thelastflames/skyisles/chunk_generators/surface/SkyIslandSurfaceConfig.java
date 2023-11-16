package com.thelastflames.skyisles.chunk_generators.surface;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.thelastflames.skyisles.chunk_generators.noise.SINoiseSettings;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import com.thelastflames.skyisles.codec.CustomCodecs;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public record SkyIslandSurfaceConfig(
        BlockState baseState,
        BlockState dirtState,
        BlockState surfaceState,
        Optional<BlockState> overhangState,
        List<BlockState> edgeStates
) {
    public static final Codec<SkyIslandSurfaceConfig> CODEC = RecordCodecBuilder.create((p_64475_) -> {
        return p_64475_.group(
                CustomCodecs.SIMPLE_STATE.fieldOf("base_block").orElse(Blocks.STONE.defaultBlockState()).forGetter(SkyIslandSurfaceConfig::baseState),
                CustomCodecs.SIMPLE_STATE.fieldOf("dirt_block").orElse(Blocks.DIRT.defaultBlockState()).forGetter(SkyIslandSurfaceConfig::dirtState),
                CustomCodecs.SIMPLE_STATE.fieldOf("surface_block").orElse(Blocks.GRASS_BLOCK.defaultBlockState()).forGetter(SkyIslandSurfaceConfig::surfaceState),
                CustomCodecs.OPTIONAL_BLOCKSTATE.fieldOf("overhang_state").orElse(Optional.empty()).forGetter(SkyIslandSurfaceConfig::overhangState),
                CustomCodecs.BLOCKSTATE_LIST.fieldOf("edge_states").orElse(Arrays.asList(Blocks.GRASS_BLOCK.defaultBlockState())).forGetter(SkyIslandSurfaceConfig::edgeStates)
        ).apply(p_64475_, SkyIslandSurfaceConfig::new);
    });
}
