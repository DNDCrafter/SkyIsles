package com.thelastflames.skyisles.utils.client.multimat;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.model.data.ModelData;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

public class MultiMatModel {
    int[] changeTextures(
            int[] verts,
            float[] newTex
    ) {
        int[] cpy = Arrays.copyOf(verts, verts.length);

        //@formatter:off
        cpy[(16     ) / 4] = Float.floatToRawIntBits(newTex[0]);
        cpy[(20     ) / 4] = Float.floatToRawIntBits(newTex[1]);
        cpy[(16 + 32) / 4] = Float.floatToRawIntBits(newTex[2]);
        cpy[(20 + 32) / 4] = Float.floatToRawIntBits(newTex[3]);
        cpy[(16 + 64) / 4] = Float.floatToRawIntBits(newTex[4]);
        cpy[(20 + 64) / 4] = Float.floatToRawIntBits(newTex[5]);
        cpy[(16 + 96) / 4] = Float.floatToRawIntBits(newTex[6]);
        cpy[(20 + 96) / 4] = Float.floatToRawIntBits(newTex[7]);
        //@formatter:on

        return cpy;
    }

    float[] texCoords(int[] data) {
        float[] coords = new float[8];

        coords[0] = Float.intBitsToFloat(data[(16 + 0) / 4]);
        coords[1] = Float.intBitsToFloat(data[(20 + 0) / 4]);
        coords[2] = Float.intBitsToFloat(data[(16 + 32) / 4]);
        coords[3] = Float.intBitsToFloat(data[(20 + 32) / 4]);
        coords[4] = Float.intBitsToFloat(data[(16 + 64) / 4]);
        coords[5] = Float.intBitsToFloat(data[(20 + 64) / 4]);
        coords[6] = Float.intBitsToFloat(data[(16 + 96) / 4]);
        coords[7] = Float.intBitsToFloat(data[(20 + 96) / 4]);

        return coords;
    }

    void mapTex(
            float[] src, float[] coords, TextureAtlasSprite sprite
    ) {
        for (int i = 0; i < src.length; i += 2) {
            coords[i] = src[i] * (sprite.getU(1) - sprite.getU(0)) + sprite.getU(0);
            coords[i + 1] = src[i + 1] * (sprite.getV(1) - sprite.getV(0)) + sprite.getV(0);
        }
    }

    float[] interp(float[] from, float[] to, float delta) {
        return new float[]{
                from[0] * delta + to[0] * (1 - delta),
                from[1] * delta + to[1] * (1 - delta),
                from[2] * delta + to[2] * (1 - delta)
        };
    }

    float[] interp(float[] norm, int[] vertices, float x, float y) {
        float[] v00 = new float[]{
                Float.intBitsToFloat(vertices[0]),
                Float.intBitsToFloat(vertices[1]),
                Float.intBitsToFloat(vertices[2])
        };
        float[] v01 = new float[]{
                Float.intBitsToFloat(vertices[0 + 8]),
                Float.intBitsToFloat(vertices[1 + 8]),
                Float.intBitsToFloat(vertices[2 + 8])
        };
        float[] v11 = new float[]{
                Float.intBitsToFloat(vertices[0 + 16]),
                Float.intBitsToFloat(vertices[1 + 16]),
                Float.intBitsToFloat(vertices[2 + 16])
        };
        float[] v10 = new float[]{
                Float.intBitsToFloat(vertices[0 + 24]),
                Float.intBitsToFloat(vertices[1 + 24]),
                Float.intBitsToFloat(vertices[2 + 24])
        };

        float[] res = interp(
                interp(v00, v01, y),
                interp(v10, v11, y),
                x
        );
        res[0] += norm[0];
        res[1] += norm[1];
        res[2] += norm[2];
        return res;
    }

    int[] interpQd(int layer, Direction dir, int[] vertices, float w, float h, float minX, float maxX, float y) {
        // TODO: calculate a normal vector for quads where a direction is not defined
        layer++;
        float[] nrm = new float[]{
                dir.getStepX() * (1 / 800f) * layer,
                dir.getStepY() * (1 / 800f) * layer,
                dir.getStepZ() * (1 / 800f) * layer,
        };

        minX /= w;
        maxX /= w;
        y /= h;
        float[] v00 = interp(nrm, vertices, minX, y);
        float[] v01 = interp(nrm, vertices, minX, y + 1 / h);
        float[] v11 = interp(nrm, vertices, maxX, y + 1 / h);
        float[] v10 = interp(nrm, vertices, maxX, y);
        vertices[0] = Float.floatToRawIntBits(v00[0]);
        vertices[1] = Float.floatToRawIntBits(v00[1]);
        vertices[2] = Float.floatToRawIntBits(v00[2]);
        vertices[8] = Float.floatToRawIntBits(v01[0]);
        vertices[9] = Float.floatToRawIntBits(v01[1]);
        vertices[10] = Float.floatToRawIntBits(v01[2]);
        vertices[16] = Float.floatToRawIntBits(v11[0]);
        vertices[17] = Float.floatToRawIntBits(v11[1]);
        vertices[18] = Float.floatToRawIntBits(v11[2]);
        vertices[24] = Float.floatToRawIntBits(v10[0]);
        vertices[25] = Float.floatToRawIntBits(v10[1]);
        vertices[26] = Float.floatToRawIntBits(v10[2]);
        return vertices;
    }

    BakedModel srcModel;
    HashMap<Direction, List<Pair<Integer, BakedQuad>>> quadsBase = new HashMap<>();
    HashMap<Direction, List<Pair<Integer, BakedQuad>>> quadsOverlay = new HashMap<>();

    private Function<ResourceLocation, TextureAtlasSprite> spriteLookup;

    public MultiMatModel(
            Minecraft mc,
            BakedModel srcModel,
            ResourceLocation... layers
    ) {
        this(mc, srcModel, (res) -> mc.getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(res), layers);
    }

    public MultiMatModel(
            Minecraft mc,
            BakedModel srcModel,
            Function<ResourceLocation, TextureAtlasSprite> spriteLookup,
            ResourceLocation... layers
    ) {
        this.spriteLookup = spriteLookup;
        this.srcModel = srcModel;

        for (Direction value : Direction.values()) {
            quadsBase.put(value, new ArrayList<>());
            quadsOverlay.put(value, new ArrayList<>());

            for (BakedQuad quad : srcModel.getQuads(
                    Blocks.STONE.defaultBlockState(),
                    value, new LegacyRandomSource(0),
                    ModelData.EMPTY, RenderType.solid()
            )) {
                Pair<ResourceLocation, ResourceLocation>[] olays = new Pair[layers.length - 1];
                for (int i = 0; i < olays.length; i++) {
                    olays[i] = Pair.of(new ResourceLocation("minecraft:block/stone"), layers[i + 1]);
                }

                List<Triplet<Integer, Boolean, BakedQuad>> qds = bake(
                        mc, quad,
                        new ResourceLocation("minecraft:block/stone"),
                        layers[0],
                        olays
                );

                for (Triplet<Integer, Boolean, BakedQuad> qd : qds) {
                    if (qd.getSecond()) quadsOverlay.get(value).add(Pair.of(qd.getFirst(), qd.getThird()));
                    else quadsBase.get(value).add(Pair.of(qd.getFirst(), qd.getThird()));
                }
            }
        }
    }

    protected List<Triplet<Integer, Boolean, BakedQuad>> bake(
            Minecraft mc,

            BakedQuad originalQuad,

            ResourceLocation base, ResourceLocation blend,
            Pair<ResourceLocation, ResourceLocation>... overlays
    ) {
        TextureAtlasSprite sprite = originalQuad.getSprite();

        float[] srcCoords = texCoords(originalQuad.getVertices());

        int w = sprite.contents().width();
        int h = sprite.contents().height();

        for (int i = 0; i < srcCoords.length; i += 2) {
            srcCoords[i] -= sprite.getU(0);
            srcCoords[i + 1] -= sprite.getV(0);
            srcCoords[i] /= (sprite.getU(1) - sprite.getU(0));
            srcCoords[i + 1] /= (sprite.getV(1) - sprite.getV(0));
        }

        ArrayList<Triplet<Integer, Boolean, BakedQuad>> quads = new ArrayList<>();

        sprite = spriteLookup.apply(base);
        float[] newTex = new float[8];
        mapTex(srcCoords, newTex, sprite);
        quads.add(Triplet.of(
                0, false, new BakedQuad(
                        originalQuad.getVertices(),
                        originalQuad.getTintIndex(), originalQuad.getDirection(),
                        originalQuad.getSprite(), originalQuad.isShade(),
                        originalQuad.hasAmbientOcclusion()
                )
        ));

        sprite = spriteLookup.apply(blend);
        newTex = new float[8];
        mapTex(srcCoords, newTex, sprite);
        quads.add(Triplet.of(
                0, true, new BakedQuad(
                        changeTextures(originalQuad.getVertices(), newTex),
                        0, originalQuad.getDirection(),
                        sprite, false,
                        false
                )
        ));

        for (int i = 0; i < overlays.length; i++) {
            Pair<ResourceLocation, ResourceLocation> olay = overlays[i];

            TextureAtlasSprite sprite1 = spriteLookup.apply(olay.getFirst());
            TextureAtlasSprite sprite2 = spriteLookup.apply(olay.getSecond());
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    int rgba = sprite2.getPixelRGBA(0, x, y);
                    int a = (rgba >> 24) & 0xFF;

                    if (a != 0) {
                        // find row length
                        int minX = x;
                        while (a != 0) {
                            x++;
                            if (x == w) {
                                break;
                            }
                            rgba = sprite2.getPixelRGBA(0, x, y);
                            a = (rgba >> 24) & 0xFF;
                        }
                        int maxX = x;
                        x--;

                        float[] from = new float[]{
                                minX, y,
                                minX, y + 1,
                                maxX, y + 1,
                                maxX, y
                        };
                        mapTex(from, newTex, sprite1);
                        quads.add(
                                Triplet.of(
                                        i + 1,
                                        false,
                                        new BakedQuad(
                                                interpQd(i, originalQuad.getDirection(), changeTextures(originalQuad.getVertices(), newTex), w, h, minX, maxX, y),
                                                0, originalQuad.getDirection(),
                                                sprite1, originalQuad.isShade(),
                                                originalQuad.hasAmbientOcclusion()
                                        )
                                )
                        );

                        from = new float[]{
                                minX, y,
                                minX, y + 1,
                                maxX, y + 1,
                                maxX, y
                        };
                        mapTex(from, newTex, sprite2);
                        quads.add(
                                Triplet.of(
                                        i + 1,
                                        true,
                                        new BakedQuad(
                                                interpQd(i, originalQuad.getDirection(), changeTextures(originalQuad.getVertices(), newTex), w, h, minX, maxX, y),
                                                0, originalQuad.getDirection(),
                                                sprite2, false,
                                                false
                                        )
                                )
                        );
                    }
                }
            }
        }

        return quads;
    }

    public List<BakedQuad> getBase(Minecraft mc, Direction value, ResourceLocation base, ResourceLocation... layers) {
        List<Pair<Integer, BakedQuad>> quadsIn = quadsBase.get(value);
        if (quadsIn == null) return List.of();

        List<BakedQuad> quads = new ArrayList<>();
        float[] newTex = new float[8];

        TextureAtlasSprite[] sprites = new TextureAtlasSprite[layers.length + 1];
        for (int i = 0; i < layers.length; i++)
            sprites[i + 1] = mc.getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(layers[i]);
        sprites[0] = mc.getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(base);

        for (Pair<Integer, BakedQuad> quadD : quadsIn) {
            int layer = quadD.getFirst();
            BakedQuad quad = quadD.getSecond();

            TextureAtlasSprite sprite = quad.getSprite();

            float[] srcCoords = texCoords(quad.getVertices());

            for (int i = 0; i < srcCoords.length; i += 2) {
                srcCoords[i] -= sprite.getU(0);
                srcCoords[i + 1] -= sprite.getV(0);
                srcCoords[i] /= (sprite.getU(1) - sprite.getU(0));
                srcCoords[i + 1] /= (sprite.getV(1) - sprite.getV(0));
            }

            sprite = sprites[layer];

            mapTex(srcCoords, newTex, sprite);

            quads.add(new BakedQuad(
                    changeTextures(Arrays.copyOf(quad.getVertices(), quad.getVertices().length), newTex),
                    quad.getTintIndex(), quad.getDirection(),
                    sprite, quad.isShade(),
                    quad.hasAmbientOcclusion()
            ));
        }

        return quads;
    }

    public List<BakedQuad> getOverlay(Minecraft mc, Direction value, ResourceLocation base, ResourceLocation... layers) {
        List<Pair<Integer, BakedQuad>> quadsIn = quadsOverlay.get(value);
        if (quadsIn == null) return List.of();

        List<BakedQuad> quads = new ArrayList<>();
        float[] newTex = new float[8];

        TextureAtlasSprite[] sprites = new TextureAtlasSprite[layers.length + 1];
        for (int i = 0; i < layers.length; i++)
            sprites[i + 1] = mc.getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(layers[i]);
        sprites[0] = mc.getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(base);

        for (Pair<Integer, BakedQuad> quadD : quadsIn) {
            int layer = quadD.getFirst();
            BakedQuad quad = quadD.getSecond();

            TextureAtlasSprite sprite = quad.getSprite();

            float[] srcCoords = texCoords(quad.getVertices());

            ResourceLocation mapTo;
            if (layer == 0)
                mapTo = base;
            else mapTo = layers[layer - 1];

            if (mapTo.equals(sprite.contents().name())) {
                quads.add(quad);
                continue;
            }

            for (int i = 0; i < srcCoords.length; i += 2) {
                srcCoords[i] -= sprite.getU(0);
                srcCoords[i + 1] -= sprite.getV(0);
                srcCoords[i] /= (sprite.getU(1) - sprite.getU(0));
                srcCoords[i + 1] /= (sprite.getV(1) - sprite.getV(0));
            }

            sprite = sprites[layer];

            mapTex(srcCoords, newTex, sprite);

            quads.add(new BakedQuad(
                    changeTextures(Arrays.copyOf(quad.getVertices(), quad.getVertices().length), newTex),
                    0, quad.getDirection(),
                    sprite, false, false
            ));
        }

        return quads;
    }
}
