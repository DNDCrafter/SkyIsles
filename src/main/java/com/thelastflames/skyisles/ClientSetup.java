package com.thelastflames.skyisles;

import com.thelastflames.skyisles.client.block.ChestTESR;
import com.thelastflames.skyisles.registry.SkyBlocks;
import com.thelastflames.skyisles.registry.SkyTileEntities;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@OnlyIn(Dist.CLIENT)
public class ClientSetup {
	public static void run(FMLClientSetupEvent event) {
		setupColors();
		setupRenderLayers();
	}
	
	public static void setupColors() {
	}
	
	public static void registerTERS(EntityRenderersEvent.RegisterRenderers event) {
//		ClientRegistry.bindTileEntityRenderer(SkyTileEntities.LAMPTE.get(), MultiMaterialTERenderer::new);
//		ClientRegistry.bindTileEntityRenderer(SkyTileEntities.SKYBOX.get(), SkyboxRenderer::new);
//		ClientRegistry.bindTileEntityRenderer(SkyTileEntities.ITEM_OBSERVER.get(), ItemObserverRenderer::new);
		event.registerBlockEntityRenderer(SkyTileEntities.CHEST_TE.get(), ChestTESR::new);
	}
	
	public static void setupRenderLayers() {
//		RenderTypeLookup.setRenderLayer(SkyBlocks.LAMP_BLOCK.getObject1().get(), RenderType.getCutout());
		ItemBlockRenderTypes.setRenderLayer(SkyBlocks.DRAWER_BLOCK.getObject1().get(), RenderType.cutout());
//		RenderTypeLookup.setRenderLayer(SkyBlocks.ITEM_OBSERVER.getObject1().get(), RenderType.getCutout());
	}
	
	public static void setupGUIs() {
//		ScreenManager.registerFactory(ForgeContainer.TYPE, ForgeScreen::new);
	}
	
	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
	public static final class GuiSetup {
		@SubscribeEvent
		public static void setup(FMLClientSetupEvent event) {
			setupGUIs();
		}
	}
}
