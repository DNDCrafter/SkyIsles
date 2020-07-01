package com.thelastflames.skyisles;

import com.thelastflames.skyisles.client.block.MultiMaterialTERenderer;
import com.thelastflames.skyisles.registry.SkyBlocks;
import com.thelastflames.skyisles.registry.SkyTileEntities;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@OnlyIn(Dist.CLIENT)
public class ClientSetup {
	public static void run(FMLClientSetupEvent event) {
		setupColors();
		registerTERS();
		setupRenderLayers();
	}
	public static void setupColors() {
	}
	public static void registerTERS() {
		ClientRegistry.bindTileEntityRenderer(SkyTileEntities.MULTIMATERIALTE.get(), MultiMaterialTERenderer::new);
		ClientRegistry.bindTileEntityRenderer(SkyTileEntities.LAMPTE.get(), MultiMaterialTERenderer::new);
	}
	public static void setupRenderLayers() {
		RenderTypeLookup.setRenderLayer(SkyBlocks.LAMP_BLOCK.getObject1().get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(SkyBlocks.DRAWER_BLOCK.getObject1().get(), RenderType.getCutout());
	}
}