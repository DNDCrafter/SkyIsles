package com.thelastflames.skyisles.events;

import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class DimensionEvents {
	@SubscribeEvent
	public static void BreakSpeedEvent(PlayerEvent.BreakSpeed event) {
	}
}
