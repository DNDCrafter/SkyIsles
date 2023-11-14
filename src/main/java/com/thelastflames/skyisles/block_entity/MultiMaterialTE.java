package com.thelastflames.skyisles.block_entity;

import com.thelastflames.skyisles.utils.client.multimat.MultiMatBakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Random;

public abstract class MultiMaterialTE extends BlockEntity {
    ModelData data;
    int layerCount;
    ArrayList<String> materials = new ArrayList<>();

    public MultiMaterialTE(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState, int layerCount) {
        super(pType, pPos, pBlockState);
        this.layerCount = layerCount;

//        for (int i = 0; i < layerCount; i++)
//            materials.add("minecraft:block/blue_ice");
//            materials.add("minecraft:block/obsidian");


        String[] bases = new String[]{
                "dark_prismarine",
                "prismarine_bricks",
                "gold_block",
                "diamond_block",
                "stone",
                "obsidian",
                "cobblestone",
                "sculk",
                "smooth_stone",
                "reinforced_deepslate_side"
        };

        String[] lamps = new String[]{
                "blue_ice",
                "sea_lantern",
                "redstone_block",
                "emerald_block",
                "sculk",
                "gold_block",
                "diamond_block",
                "amethyst_block"
        };

        materials.add("minecraft:block/" + lamps[new Random().nextInt(lamps.length)]);
        materials.add("minecraft:block/" + bases[new Random().nextInt(bases.length)]);

        if (FMLEnvironment.dist.isClient()) {
            updateData();
        }
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        super.onDataPacket(net, pkt);
        ListTag tag = pkt.getTag().getList("Materials", Tag.TAG_STRING);
        for (int i = 0; i < tag.size(); i++) {
            materials.set(i, tag.getString(i));
        }
        updateData();
    }

    protected void updateData() {
        ModelData.Builder builder = defaultBuilder();
        for (int i = 0; i < materials.size(); i++) {
            builder.with(
                    MultiMatBakedModel.getMat(i),
                    new ResourceLocation(materials.get(i))
            );
        }
        this.data = builder.build();
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return super.getUpdatePacket();
    }

    @Override
    public CompoundTag getUpdateTag() {
        return super.getUpdateTag();
    }

    protected abstract ModelData.Builder defaultBuilder();

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        super.handleUpdateTag(tag);
    }

    @Override
    public @NotNull ModelData getModelData() {
        return data;
    }
}
