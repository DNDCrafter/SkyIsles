package com.thelastflames.skyisles.mixins.render.multimat;

import com.google.common.collect.ImmutableList;
import com.thelastflames.skyisles.utils.client.multimat.MultiMatBakedModel;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

@Mixin(RenderType.class)
public class RenderTypesMixin {
    @Shadow
    @Final
    @Mutable
    private static ImmutableList<RenderType> CHUNK_BUFFER_LAYERS;

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderType;chunkBufferLayers()Ljava/util/List;", shift = At.Shift.BEFORE), method = "<clinit>")
    private static void postInit(CallbackInfo ci) {
        ArrayList<RenderType> IWantToMutateItLol = new ArrayList<>(CHUNK_BUFFER_LAYERS);

        for (int i = 0; i < IWantToMutateItLol.size(); i++) {
            if (IWantToMutateItLol.get(i) == RenderType.cutoutMipped())
                IWantToMutateItLol.add(MultiMatBakedModel.MULTIPLICATIVE);
        }

        CHUNK_BUFFER_LAYERS = ImmutableList.copyOf(IWantToMutateItLol);
    }
}
