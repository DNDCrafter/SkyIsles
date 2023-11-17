package com.thelastflames.skyisles.registry;

import com.mojang.serialization.Codec;
import com.thelastflames.skyisles.SkyIsles;
import com.thelastflames.skyisles.chunk_generators.SkyIslesChunkGenerator;
import com.thelastflames.skyisles.chunk_generators.SkyIslesChunkGenerator2;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class SkyChunkGenerators {
    // thank you fabric wiki https://fabricmc.net/wiki/tutorial:chunkgenerator
    public static final DeferredRegister<Codec<? extends ChunkGenerator>> GENERATORS = DeferredRegister.create(Registries.CHUNK_GENERATOR, SkyIsles.ModID);

    public static final RegistryObject<Codec<? extends ChunkGenerator>> SKY_ISLES = GENERATORS.register("skyisles", () -> SkyIslesChunkGenerator2.CODEC);
}
