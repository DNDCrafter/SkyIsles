package com.thelastflames.skyisles;

import com.thelastflames.skyisles.Client.Block.MultiMaterialTERenderer;
import com.thelastflames.skyisles.Registry.SkyTileEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@OnlyIn(Dist.CLIENT)
public class ClientSetup {
	public static void run(FMLClientSetupEvent event) {
		registerTERS();
	}
	public static void setupColors() {
	}
	public static void registerTERS() {
		ClientRegistry.bindTileEntityRenderer(SkyTileEntities.MULTIMATERIALTE.get(), MultiMaterialTERenderer::new);
	}
}
