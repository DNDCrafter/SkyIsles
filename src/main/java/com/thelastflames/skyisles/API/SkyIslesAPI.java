package com.thelastflames.skyisles.API;

import net.minecraftforge.eventbus.BusBuilderImpl;
import net.minecraftforge.eventbus.EventBus;
import net.minecraftforge.eventbus.api.BusBuilder;

public class SkyIslesAPI extends EventBus {
	public static final SkyIslesAPI INSTANCE = new SkyIslesAPI(BusBuilder.builder());
	
	public SkyIslesAPI(BusBuilder busBuilder) {
		super((BusBuilderImpl) busBuilder);
	}
}
