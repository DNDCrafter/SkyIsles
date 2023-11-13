package com.thelastflames.skyisles.client.debug;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.datafixers.util.Pair;
import com.thelastflames.skyisles.utils.client.multimat.MultiMatModel;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class MultiMaterialTest {
    private static final ShaderInstance instance;

    static {
        try {
            PackResources src = new PackResources() {
                @Nullable
                @Override
                public IoSupplier<InputStream> getRootResource(String... pElements) {
                    return null;
                }

                @Nullable
                @Override
                public IoSupplier<InputStream> getResource(PackType pPackType, ResourceLocation pLocation) {
                    return null;
                }

                @Override
                public void listResources(PackType pPackType, String pNamespace, String pPath, ResourceOutput pResourceOutput) {

                }

                @Override
                public Set<String> getNamespaces(PackType pType) {
                    return null;
                }

                @Nullable
                @Override
                public <T> T getMetadataSection(MetadataSectionSerializer<T> pDeserializer) throws IOException {
                    return null;
                }

                @Override
                public String packId() {
                    return "debug";
                }

                @Override
                public void close() {

                }
            };
            instance = new ShaderInstance(
                    (loc) -> {
                        if (loc.toString().equals("minecraft:shaders/core/aaaa.json")) {
                            return java.util.Optional.of(new Resource(src, () -> {
                                return new ByteArrayInputStream("{\n    \"blend\": {\n        \"func\": \"add\",\n        \"srcrgb\": \"srcalpha\",\n        \"dstrgb\": \"1-srcalpha\"\n    },\n    \"vertex\": \"aaaa\",\n    \"fragment\": \"aaaa\",\n    \"attributes\": [\n        \"Position\",\n        \"Color\",\n        \"UV0\"\n    ],\n    \"samplers\": [\n        { \"name\": \"Sampler0\" }\n    ],\n    \"uniforms\": [\n        { \"name\": \"ModelViewMat\", \"type\": \"matrix4x4\", \"count\": 16, \"values\": [ 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0 ] },\n        { \"name\": \"ProjMat\", \"type\": \"matrix4x4\", \"count\": 16, \"values\": [ 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0 ] },\n        { \"name\": \"ColorModulator\", \"type\": \"float\", \"count\": 4, \"values\": [ 1.0, 1.0, 1.0, 1.0 ] }\n    ]\n}".getBytes(StandardCharsets.UTF_8));
                            }));
                        } else if (loc.toString().equals("minecraft:shaders/core/aaaa.fsh")) {
                            return java.util.Optional.of(new Resource(src, () -> {
                                return new ByteArrayInputStream(
                                        ("#version 150\n" +
                                                "\n" +
                                                "uniform sampler2D Sampler0;\n" +
                                                "\n" +
                                                "uniform vec4 ColorModulator;\n" +
                                                "\n" +
                                                "in vec4 vertexColor;\n" +
                                                "in vec2 texCoord0;\n" +
                                                "\n" +
                                                "out vec4 fragColor;\n" +
                                                "\n" +
                                                "void main() {\n" +
                                                "    vec4 color = texture(Sampler0, texCoord0) * vertexColor;\n" +
                                                "    fragColor = color.rgba * ColorModulator;\n" +
                                                "}\n").getBytes(StandardCharsets.UTF_8)
                                );
                            }));
                        } else if (loc.toString().equals("minecraft:shaders/core/aaaa.vsh")) {
                            return java.util.Optional.of(new Resource(src, () -> {
                                return new ByteArrayInputStream(
                                        ("#version 150\n" +
                                                "\n" +
                                                "in vec3 Position;\n" +
                                                "in vec4 Color;\n" +
                                                "in vec2 UV0;\n" +
                                                "\n" +
                                                "uniform mat4 ModelViewMat;\n" +
                                                "uniform mat4 ProjMat;\n" +
                                                "\n" +
                                                "out vec4 vertexColor;\n" +
                                                "out vec2 texCoord0;\n" +
                                                "\n" +
                                                "void main() {\n" +
                                                "    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);\n" +
                                                "\n" +
                                                "    vertexColor = Color;\n" +
                                                "    texCoord0 = UV0;\n" +
                                                "}\n").getBytes(StandardCharsets.UTF_8)
                                );
                            }));
                        }
                        return Optional.ofNullable(null);
                    },
                    "aaaa", DefaultVertexFormat.POSITION_COLOR_TEX
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected static void drawBlock(
            MultiMatModel model,
            Minecraft mc, PoseStack stk, MultiBufferSource.BufferSource src,
            BlockPos coord,
            ResourceLocation base, ResourceLocation blend,
            ResourceLocation frame, ResourceLocation frameShape,
            boolean fullbright
    ) {
        stk.pushPose();
        stk.translate(coord.getX(), coord.getY(), coord.getZ());

        VertexConsumer consumer = src.getBuffer(RenderType.solid());
        for (Direction value : Direction.values()) {
            for (BakedQuad quad : model.getBase(mc, value, base, frame)) {
                float shade = mc.level.getShade(
                        quad.getDirection(), quad.isShade()
                );
                consumer.putBulkData(
                        stk.last(), quad,
                        shade, shade, shade,
                        fullbright ? LightTexture.FULL_BRIGHT : LightTexture.FULL_SKY, OverlayTexture.NO_OVERLAY
                );
            }
        }

        src.endBatch();

        RenderType.solid().setupRenderState();
        RenderSystem.depthFunc(GL11.GL_LEQUAL);

        RenderSystem.enableBlend();
        RenderSystem.blendFunc(
                GL11.GL_DST_COLOR,
                GL11.GL_ZERO
        );

        GameRenderer.getRendertypeSolidShader().apply();

        Tesselator tessel = Tesselator.getInstance();
        BufferBuilder builder = tessel.getBuilder();
        builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.BLOCK);

        for (Direction value : Direction.values()) {
            for (BakedQuad quad : model.getOverlay(mc, value, blend, frameShape)) {
                builder.putBulkData(
                        stk.last(), quad,
                        1, 1, 1,
                        LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY
                );
            }
        }

        tessel.end();

        GameRenderer.getRendertypeSolidShader().clear();

        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
        RenderSystem.depthFunc(GL11.GL_LESS);
        RenderType.solid().clearRenderState();

        stk.popPose();
    }

    public static void draw(Minecraft mc, Camera camera, PoseStack stk, MultiBufferSource.BufferSource src) {
        stk.pushPose();
        stk.translate(
                -camera.getPosition().x,
                -camera.getPosition().y,
                -camera.getPosition().z
        );

        String[] bases = new String[]{
                "dark_prismarine",
                "prismarine_bricks",
                "gold_block",
                "diamond_block",
                "stone",
                "obsidian",
                "cobblestone"
        };

        String[] lamps = new String[]{
                "blue_ice",
                "sea_lantern",
                "redstone_block",
                "emerald_block",
                "sculk"
        };

        BlockState state = Blocks.BLUE_ICE.defaultBlockState();

        BakedModel mdl = mc.getBlockRenderer().getBlockModel(state);
        MultiMatModel mmdl = new MultiMatModel(mc, mdl, new ResourceLocation("skyisles:block/lamp_base"), new ResourceLocation("skyisles:block/lamp_overlay"));

        for (int i = 0; i < lamps.length; i++) {
            for (int i1 = 0; i1 < bases.length; i1++) {
                drawBlock(
                        mmdl, mc, stk, src, new BlockPos(i1, 0, i * 3),
                        new ResourceLocation("minecraft:block/" + lamps[i]),
                        new ResourceLocation("skyisles:block/lamp_base"),
                        new ResourceLocation("minecraft:block/" + bases[i1]),
                        new ResourceLocation("skyisles:block/lamp_overlay"),
                        false
                );
                drawBlock(
                        mmdl, mc, stk, src, new BlockPos(i1, 0, 1 + i * 3),
                        new ResourceLocation("minecraft:block/" + lamps[i]),
                        new ResourceLocation("skyisles:block/lamp_base_on"),
                        new ResourceLocation("minecraft:block/" + bases[i1]),
                        new ResourceLocation("skyisles:block/lamp_overlay"),
                        true
                );
            }
        }

        int totalTex = lamps.length + bases.length + 3;
        int lampCount = lamps.length * bases.length;

        stk.popPose();
    }
}
