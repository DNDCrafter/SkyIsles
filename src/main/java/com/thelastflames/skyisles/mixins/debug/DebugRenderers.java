package com.thelastflames.skyisles.mixins.debug;

import com.mojang.blaze3d.vertex.PoseStack;
import com.thelastflames.skyisles.client.debug.CloudTest;
import com.thelastflames.skyisles.client.debug.MultiMaterialTest;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class DebugRenderers {
    @Shadow @Final private Minecraft minecraft;

    @Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;checkPoseStack(Lcom/mojang/blaze3d/vertex/PoseStack;)V", ordinal = 0))
    public void doDraw(PoseStack pPoseStack, float pPartialTick, long pFinishNanoTime, boolean pRenderBlockOutline, Camera pCamera, GameRenderer pGameRenderer, LightTexture pLightTexture, Matrix4f pProjectionMatrix, CallbackInfo ci) {
        MultiMaterialTest.draw(minecraft, pCamera, pPoseStack, minecraft.renderBuffers().bufferSource());
        CloudTest.draw(minecraft, pCamera, pPoseStack, minecraft.renderBuffers().bufferSource());
    }
}
