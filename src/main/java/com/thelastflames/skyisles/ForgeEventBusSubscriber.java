package com.thelastflames.skyisles;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.world.RegisterDimensionsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.thelastflames.skyisles.SkyIsles.ModID;

@Mod.EventBusSubscriber(modid = ModID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEventBusSubscriber {
	
	static ResourceLocation location=new ResourceLocation(ModID,"testdimension");
	
	@SubscribeEvent
	public static void onRegisterDimensionsEvent(final RegisterDimensionsEvent event) {
		System.out.println("hello from forge dimension registry");
	}
}
