package com.thelastflames.skyisles.block_entity;

import com.thelastflames.skyisles.registry.SkyTileEntities;
import com.thelastflames.skyisles.utils.client.multimat.MultiMatBakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;

import java.util.Random;

public class LampBE extends MultiMaterialTE {
    public LampBE(BlockPos pPos, BlockState pBlockState) {
        super(SkyTileEntities.LAMP_BE.get(), pPos, pBlockState, 2);
    }

    @Override
    protected ModelData.Builder defaultBuilder() {
        ModelData.Builder builder = ModelData.builder();
        if (new Random().nextBoolean()) {
            builder.with(MultiMatBakedModel.getLayer(0), new ResourceLocation("skyisles:block/lamp_base_off"));
        } else {
            builder.with(MultiMatBakedModel.getLayer(0), new ResourceLocation("skyisles:block/lamp_base_on"));
        }
        builder.with(MultiMatBakedModel.getLayer(1), new ResourceLocation("skyisles:block/lamp_overlay"));
        return builder;
    }
}
