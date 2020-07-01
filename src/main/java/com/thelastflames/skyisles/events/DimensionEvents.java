package com.thelastflames.skyisles.events;

import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class DimensionEvents {
	@SubscribeEvent
	public static void BreakSpeedEvent(PlayerEvent.BreakSpeed event) {
//		boolean test=false;
//		boolean test2=true;
//		System.out.println(test?"hello":test2?"world":"!");
//		System.out.println(SkyDimensions.TESTDIM.get());
//		event.setNewSpeed(0);
	}
}
