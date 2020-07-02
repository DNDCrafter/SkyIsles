package com.thelastflames.skyisles.API.events;

import net.minecraftforge.eventbus.api.Event;

public class SkyIslesSetupEvent extends Event {
	private final SkyIslesSetupContext context;
	
	public SkyIslesSetupEvent(SkyIslesSetupContext context) {
		this.context = context;
	}
	
	public SkyIslesSetupContext getContext() {
		return context;
	}
}
