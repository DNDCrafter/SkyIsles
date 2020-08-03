package com.thelastflames.skyisles.API;

import net.minecraftforge.eventbus.EventBus;
import net.minecraftforge.eventbus.api.BusBuilder;

public class SkyIslesAPI extends EventBus {
	public static final SkyIslesAPI INSTANCE = new SkyIslesAPI(new BusBuilder());
	
	public SkyIslesAPI(BusBuilder busBuilder) {
		super(busBuilder);
	}
}
