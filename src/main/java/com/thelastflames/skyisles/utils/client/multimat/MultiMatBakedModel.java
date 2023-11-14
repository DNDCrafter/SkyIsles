package com.thelastflames.skyisles.utils.client.multimat;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class MultiMatBakedModel implements BakedModel {
    BakedModel src;
    MultiMatModel model;
    TextureAtlasSprite particles;
    int layerCount;
    private static final ArrayList<ModelProperty<ResourceLocation>> matProps = new ArrayList<>();
    private static final ArrayList<ModelProperty<ResourceLocation>> layerProps = new ArrayList<>();

    Minecraft mc;

    public static final ModelProperty<ResourceLocation> getMat(int index) {
        return matProps.get(index);
    }

    public static final ModelProperty<ResourceLocation> getLayer(int index) {
        return layerProps.get(index);
    }

    public static final RenderType MULTIPLICATIVE = new RenderType(
            "multimat_multiplicative_solid",
            DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS,
            64, false, false,
            () -> {
                RenderType.solid().setupRenderState();
                RenderSystem.depthFunc(GL11.GL_LEQUAL);
                RenderSystem.enableBlend();
                // multiply
                RenderSystem.blendFunc(
                        GL11.GL_DST_COLOR,
                        GL11.GL_ZERO
                );
            },
            () -> {
                RenderSystem.defaultBlendFunc();
                RenderSystem.disableBlend();
                RenderSystem.depthFunc(GL11.GL_LESS);
                RenderType.solid().clearRenderState();
            }
    ) {
    };

    public MultiMatBakedModel(Function<ResourceLocation, TextureAtlasSprite> lookup, TextureAtlasSprite particles, Minecraft mc, BakedModel src, ResourceLocation... layers) {
        this.particles = particles;
        this.src = src;
        this.mc = mc;
        model = new MultiMatModel(
                mc, src, lookup, layers
        );
        layerCount = layers.length;

        while (matProps.size() <= layerCount) matProps.add(new ModelProperty<>());
        while (layerProps.size() <= layerCount) layerProps.add(new ModelProperty<>());
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData data, @Nullable RenderType renderType) {
        if (renderType == RenderType.solid()) {
            ResourceLocation[] layers = new ResourceLocation[layerCount - 1];
            for (int i = 0; i < layerCount - 1; i++) {
                layers[i] = data.get(getMat(i + 1));
            }
            return model.getBase(
                    mc, side,
                    data.get(getMat(0)),
                    layers
            );
        } else if (renderType == MULTIPLICATIVE) {
            ResourceLocation[] layers = new ResourceLocation[layerCount - 1];
            for (int i = 0; i < layerCount - 1; i++) {
                layers[i] = data.get(getLayer(i + 1));
            }
            return model.getOverlay(
                    mc, side,
                    data.get(getLayer(0)),
                    layers
            );
        } else {
                return List.of();
        }
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState pState, @Nullable Direction pDirection, RandomSource pRandom) {
//        return BakedModel.super.getQuads(pState, pDirection, pRandom, ModelData.EMPTY, RenderType.solid());
        return List.of();
    }

    @Override
    public TextureAtlasSprite getParticleIcon(@NotNull ModelData data) {
        return BakedModel.super.getParticleIcon(data);
    }

    @Override
    public boolean useAmbientOcclusion() {
        return src.useAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return src.isGui3d();
    }

    @Override
    public boolean usesBlockLight() {
        return src.usesBlockLight();
    }

    @Override
    public boolean isCustomRenderer() {
        return src.isCustomRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return particles;
    }

    @Override
    public ItemOverrides getOverrides() {
        return src.getOverrides();
    }
}
