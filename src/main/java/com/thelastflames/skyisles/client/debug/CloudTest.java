package com.thelastflames.skyisles.client.debug;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.thelastflames.skyisles.utils.client.multimat.MultiMatModel;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

public class CloudTest {
    protected static void drawShape(Frustum frustration, PoseStack stk, VertexConsumer consumer, float alpha) {
        Random rng = new Random(87342);

        Vec3 color = Minecraft.getInstance().level.getCloudColor(0);

        for (int cI = 0; cI < 1000; cI++) {
            double cx = rng.nextDouble() * 800 - 400;
            double cy = rng.nextDouble() * 200 - 100;
            double cz = rng.nextDouble() * 800 - 400;

            double rw = rng.nextDouble() * 8 + 8;
            double rh = rng.nextDouble() * (rw / 2) + (rw / 2);

            AABB approxBounds = new AABB(
                    cx - (rw * 2 + 2), cy - (rh * 2 + 2), cz - (rw * 2 + 2),
                    cx + (rw * 2 + 2), cy + (rh * 2 + 2), cz + (rw * 2 + 2)
            );
            boolean visible = frustration.isVisible(approxBounds);

            for (int i = 0; i < rng.nextInt(4) + 4; i++) {
                double x = cx + rng.nextDouble() * rw - (rw / 2);
                double y = cy + rng.nextDouble() * rh - (rh / 2);
                double z = cz + rng.nextDouble() * rw - (rw / 2);

                double w = rng.nextDouble() * 2 + 2;
                double h = rng.nextDouble() * 2 + 2;
                double d = rng.nextDouble() * 2 + 2;

                double pMinX = x - w;
                double pMinY = y - h;
                double pMinZ = z - d;
                double pMaxX = x + w;
                double pMaxY = y + h;
                double pMaxZ = z + d;

                float mY = 0.9f;
                float mZ = 0.7f;
                float mX = 0.8f;

                if (visible) continue;

                consumer.vertex(stk.last().pose(), (float) pMinX, (float) pMinY, (float) pMinZ).uv(0, 0).color((float) color.x * mZ, (float) color.y * mZ, (float) color.z * mZ, alpha).normal(stk.last().normal(), -1, 0, 0).endVertex();
                consumer.vertex(stk.last().pose(), (float) pMinX, (float) pMaxY, (float) pMinZ).uv(0, 1).color((float) color.x * mZ, (float) color.y * mZ, (float) color.z * mZ, alpha).normal(stk.last().normal(), -1, 0, 0).endVertex();
                consumer.vertex(stk.last().pose(), (float) pMaxX, (float) pMaxY, (float) pMinZ).uv(1, 1).color((float) color.x * mZ, (float) color.y * mZ, (float) color.z * mZ, alpha).normal(stk.last().normal(), -1, 0, 0).endVertex();
                consumer.vertex(stk.last().pose(), (float) pMaxX, (float) pMinY, (float) pMinZ).uv(1, 0).color((float) color.x * mZ, (float) color.y * mZ, (float) color.z * mZ, alpha).normal(stk.last().normal(), -1, 0, 0).endVertex();

                consumer.vertex(stk.last().pose(), (float) pMaxX, (float) pMinY, (float) pMinZ).uv(0, 0).color((float) color.x * mX, (float) color.y * mX, (float) color.z * mX, alpha).normal(stk.last().normal(), 0, 0, 1).endVertex();
                consumer.vertex(stk.last().pose(), (float) pMaxX, (float) pMaxY, (float) pMinZ).uv(0, 1).color((float) color.x * mX, (float) color.y * mX, (float) color.z * mX, alpha).normal(stk.last().normal(), 0, 0, 1).endVertex();
                consumer.vertex(stk.last().pose(), (float) pMaxX, (float) pMaxY, (float) pMaxZ).uv(1, 1).color((float) color.x * mX, (float) color.y * mX, (float) color.z * mX, alpha).normal(stk.last().normal(), 0, 0, 1).endVertex();
                consumer.vertex(stk.last().pose(), (float) pMaxX, (float) pMinY, (float) pMaxZ).uv(1, 0).color((float) color.x * mX, (float) color.y * mX, (float) color.z * mX, alpha).normal(stk.last().normal(), 0, 0, 1).endVertex();

                consumer.vertex(stk.last().pose(), (float) pMinX, (float) pMaxY, (float) pMaxZ).uv(1, 0).color((float) color.x * mY, (float) color.y * mY, (float) color.z * mY, alpha).normal(stk.last().normal(), 0, 1, 0).endVertex();
                consumer.vertex(stk.last().pose(), (float) pMaxX, (float) pMaxY, (float) pMaxZ).uv(1, 1).color((float) color.x * mY, (float) color.y * mY, (float) color.z * mY, alpha).normal(stk.last().normal(), 0, 1, 0).endVertex();
                consumer.vertex(stk.last().pose(), (float) pMaxX, (float) pMaxY, (float) pMinZ).uv(0, 1).color((float) color.x * mY, (float) color.y * mY, (float) color.z * mY, alpha).normal(stk.last().normal(), 0, 1, 0).endVertex();
                consumer.vertex(stk.last().pose(), (float) pMinX, (float) pMaxY, (float) pMinZ).uv(0, 0).color((float) color.x * mY, (float) color.y * mY, (float) color.z * mY, alpha).normal(stk.last().normal(), 0, 1, 0).endVertex();

                consumer.vertex(stk.last().pose(), (float) pMinX, (float) pMinY, (float) pMaxZ).uv(1, 0).color((float) color.x * mX, (float) color.y * mX, (float) color.z * mX, alpha).normal(stk.last().normal(), 0, 0, -1).endVertex();
                consumer.vertex(stk.last().pose(), (float) pMinX, (float) pMaxY, (float) pMaxZ).uv(1, 1).color((float) color.x * mX, (float) color.y * mX, (float) color.z * mX, alpha).normal(stk.last().normal(), 0, 0, -1).endVertex();
                consumer.vertex(stk.last().pose(), (float) pMinX, (float) pMaxY, (float) pMinZ).uv(0, 1).color((float) color.x * mX, (float) color.y * mX, (float) color.z * mX, alpha).normal(stk.last().normal(), 0, 0, -1).endVertex();
                consumer.vertex(stk.last().pose(), (float) pMinX, (float) pMinY, (float) pMinZ).uv(0, 0).color((float) color.x * mX, (float) color.y * mX, (float) color.z * mX, alpha).normal(stk.last().normal(), 0, 0, -1).endVertex();

                consumer.vertex(stk.last().pose(), (float) pMaxX, (float) pMinY, (float) pMaxZ).uv(1, 0).color((float) color.x * mZ, (float) color.y * mZ, (float) color.z * mZ, alpha).normal(stk.last().normal(), 1, 0, 0).endVertex();
                consumer.vertex(stk.last().pose(), (float) pMaxX, (float) pMaxY, (float) pMaxZ).uv(1, 1).color((float) color.x * mZ, (float) color.y * mZ, (float) color.z * mZ, alpha).normal(stk.last().normal(), 1, 0, 0).endVertex();
                consumer.vertex(stk.last().pose(), (float) pMinX, (float) pMaxY, (float) pMaxZ).uv(0, 1).color((float) color.x * mZ, (float) color.y * mZ, (float) color.z * mZ, alpha).normal(stk.last().normal(), 1, 0, 0).endVertex();
                consumer.vertex(stk.last().pose(), (float) pMinX, (float) pMinY, (float) pMaxZ).uv(0, 0).color((float) color.x * mZ, (float) color.y * mZ, (float) color.z * mZ, alpha).normal(stk.last().normal(), 1, 0, 0).endVertex();

                consumer.vertex(stk.last().pose(), (float) pMinX, (float) pMinY, (float) pMinZ).uv(0, 0).color((float) color.x * mY, (float) color.y * mY, (float) color.z * mY, alpha).normal(stk.last().normal(), 0, -1, 0).endVertex();
                consumer.vertex(stk.last().pose(), (float) pMaxX, (float) pMinY, (float) pMinZ).uv(0, 1).color((float) color.x * mY, (float) color.y * mY, (float) color.z * mY, alpha).normal(stk.last().normal(), 0, -1, 0).endVertex();
                consumer.vertex(stk.last().pose(), (float) pMaxX, (float) pMinY, (float) pMaxZ).uv(1, 1).color((float) color.x * mY, (float) color.y * mY, (float) color.z * mY, alpha).normal(stk.last().normal(), 0, -1, 0).endVertex();
                consumer.vertex(stk.last().pose(), (float) pMinX, (float) pMinY, (float) pMaxZ).uv(1, 0).color((float) color.x * mY, (float) color.y * mY, (float) color.z * mY, alpha).normal(stk.last().normal(), 0, -1, 0).endVertex();
            }
        }
    }

    private static VertexBuffer buffer;

    public static void draw(Minecraft mc, Camera camera, PoseStack stk, MultiBufferSource.BufferSource src) {
//        if (true) return;

        if (buffer == null)
            buffer = new VertexBuffer(VertexBuffer.Usage.DYNAMIC);

        stk.pushPose();
        stk.translate(
                -camera.getPosition().x,
                -camera.getPosition().y,
                -camera.getPosition().z
        );

        stk.translate(80, 0, 0);

        Frustum frust = new Frustum(RenderSystem.getProjectionMatrix(), stk.last().pose());

        buffer.bind();
        // create geometry
//        if (false) {
            BufferBuilder builder = Tesselator.getInstance().getBuilder();
            builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR_NORMAL);
            drawShape(frust, stk, builder, 0.8f);
            BufferBuilder.RenderedBuffer bufferbuilder$renderedbuffer = builder.end();
            buffer.upload(bufferbuilder$renderedbuffer);
//        }

        RenderSystem.disableCull();
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.depthMask(true);

        RenderSystem.setShader(GameRenderer::getPositionTexColorNormalShader);
        RenderSystem.setShaderTexture(0, new ResourceLocation("skyisles:textures/sky/cloud.png"));
        FogRenderer.levelFogColor();

        // draw to depth
        RenderSystem.colorMask(false, false, false, true);
        buffer.drawWithShader(RenderSystem.getModelViewMatrix(), RenderSystem.getProjectionMatrix(), RenderSystem.getShader());

        // draw to color
        RenderSystem.colorMask(true, true, true, true);
        RenderSystem.depthFunc(GL11.GL_EQUAL);
        buffer.drawWithShader(RenderSystem.getModelViewMatrix(), RenderSystem.getProjectionMatrix(), RenderSystem.getShader());
        VertexBuffer.unbind();

        RenderSystem.depthFunc(GL11.GL_LEQUAL);
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();

        stk.popPose();
    }
}
