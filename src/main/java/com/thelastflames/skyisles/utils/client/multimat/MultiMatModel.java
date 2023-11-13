package com.thelastflames.skyisles.utils.client.multimat;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class MultiMatModel {
    int[] changeTextures(
            int[] verts,
            float[] newTex
    ) {
        ByteBuffer buffer = ByteBuffer.allocate(DefaultVertexFormat.BLOCK.getVertexSize() * 4);
        for (int i = 0; i < verts.length; i++)
            buffer.putInt(i * 4, verts[i]);
        //@formatter:off
        buffer.putFloat(16     , newTex[0]);
        buffer.putFloat(20     , newTex[1]);
        buffer.putFloat(16 + 32, newTex[2]);
        buffer.putFloat(20 + 32, newTex[3]);
        buffer.putFloat(16 + 64, newTex[4]);
        buffer.putFloat(20 + 64, newTex[5]);
        buffer.putFloat(16 + 96, newTex[6]);
        buffer.putFloat(20 + 96, newTex[7]);
        //@formatter:on

        int[] cpy = new int[verts.length];
        for (int i = 0; i < cpy.length; i++)
            cpy[i] = buffer.getInt(i * 4);

        return cpy;
    }

    float[] texCoords(int[] data) {
        float[] coords = new float[8];

        ByteBuffer buffer = ByteBuffer.allocate(DefaultVertexFormat.BLOCK.getVertexSize() * 4);
        for (int i = 0; i < data.length; i++)
            buffer.putInt(i * 4, data[i]);

        coords[0] = buffer.getFloat(16);
        coords[1] = buffer.getFloat(20);
        coords[2] = buffer.getFloat(16 + 32);
        coords[3] = buffer.getFloat(20 + 32);
        coords[4] = buffer.getFloat(16 + 64);
        coords[5] = buffer.getFloat(20 + 64);
        coords[6] = buffer.getFloat(16 + 96);
        coords[7] = buffer.getFloat(20 + 96);

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

    int[] interpQd(int layer, Direction dir, int[] vertices, float w, float h, float x, float y) {
        // TODO: calculate a normal vector for quads where a direction is not defined
        layer++;
        float[] nrm = new float[]{
                dir.getStepX() * (1 / 800f) * layer,
                dir.getStepY() * (1 / 800f) * layer,
                dir.getStepZ() * (1 / 800f) * layer,
        };

        x /= w;
        y /= h;
        float[] v00 = interp(nrm, vertices, x, y);
        float[] v01 = interp(nrm, vertices, x, y + 1 / h);
        float[] v11 = interp(nrm, vertices, x + 1 / w, y + 1 / h);
        float[] v10 = interp(nrm, vertices, x + 1 / w, y);
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

    public List<Pair<Boolean, BakedQuad>> bake(
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

        ArrayList<Pair<Boolean, BakedQuad>> quads = new ArrayList<>();

        sprite = mc.getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(base);
        float[] newTex = new float[8];
        mapTex(srcCoords, newTex, sprite);
        quads.add(Pair.of(
                false, new BakedQuad(
                        changeTextures(originalQuad.getVertices(), newTex),
                        originalQuad.getTintIndex(), originalQuad.getDirection(),
                        originalQuad.getSprite(), originalQuad.isShade(),
                        originalQuad.hasAmbientOcclusion()
                )
        ));

        sprite = mc.getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(blend);
        newTex = new float[8];
        mapTex(srcCoords, newTex, sprite);
        quads.add(Pair.of(
                true, new BakedQuad(
                        changeTextures(originalQuad.getVertices(), newTex),
                        originalQuad.getTintIndex(), originalQuad.getDirection(),
                        originalQuad.getSprite(), originalQuad.isShade(),
                        originalQuad.hasAmbientOcclusion()
                )
        ));

        for (int i = 0; i < overlays.length; i++) {
            Pair<ResourceLocation, ResourceLocation> olay = overlays[i];

            TextureAtlasSprite sprite1 = mc.getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(olay.getFirst());
            TextureAtlasSprite sprite2 = mc.getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(olay.getSecond());
            for (int x = 0; x < w; x++) {
                for (int y = 0; y < h; y++) {
                    int rgba = sprite2.getPixelRGBA(0, x, y);
                    int a = (rgba >> 24) & 0xFF;
                    if (a != 0) {
                        float[] from = new float[]{
                                x, y,
                                x, y + 1,
                                x + 1, y + 1,
                                x + 1, y
                        };
                        mapTex(from, newTex, sprite1);
                        quads.add(
                                Pair.of(
                                        false,
                                        new BakedQuad(
                                                interpQd(i, originalQuad.getDirection(), changeTextures(originalQuad.getVertices(), newTex), w, h, x, y),
                                                0, originalQuad.getDirection(),
                                                sprite1, originalQuad.isShade(),
                                                originalQuad.hasAmbientOcclusion()
                                        )
                                )
                        );

                        from = new float[]{
                                x, y,
                                x, y + 1,
                                x + 1, y + 1,
                                x + 1, y
                        };
                        mapTex(from, newTex, sprite2);
                        quads.add(
                                Pair.of(
                                        true,
                                        new BakedQuad(
                                                interpQd(i, originalQuad.getDirection(), changeTextures(originalQuad.getVertices(), newTex), w, h, x, y),
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
}
